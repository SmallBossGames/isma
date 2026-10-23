# isma-lsp — LSP Protocol Details

## Framing

LSP messages are JSON-RPC 2.0 documents preceded by a header:

```
Content-Length: N\r\n
\r\n
<N UTF-8 bytes of JSON>
```

`LspMessages.read` / `LspMessages.write` in `protocol/LspMessages.kt` implement the framing. stdout is reserved for the protocol; anything written to stdout outside the framing corrupts the stream, so all logging goes to stderr.

## Handshake

### initialize

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": { "processId": null, "rootUri": null, "capabilities": {}, "clientInfo": { "name": "isma-ui", "version": "1.0" } }
}
```

Response:

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "capabilities": {
      "textDocumentSync": 1,
      "semanticTokensProvider": {
        "legend": { "tokenTypes": ["keyword", "comment", "number"] },
        "full": {}
      }
    },
    "serverInfo": { "name": "lisma-lsp", "version": "1.0.0" }
  }
}
```

- `textDocumentSync: 1` — full document sync.
- `semanticTokensProvider.full: {}` — the server supports full-document requests with no options.
- The client must follow with the `initialized` notification.

## Document Sync

| Method | Direction | Effect |
|--------|-----------|--------|
| `textDocument/didOpen` | client → server | Stores `params.textDocument.text` under `params.textDocument.uri` |
| `textDocument/didChange` | client → server | Replaces the stored text with the **last** entry of `params.contentChanges[].text` |
| `textDocument/didClose` | client → server | Removes the stored text |

The server keeps one full text per URI in a `ConcurrentHashMap<String, String>`. URIs are opaque; UI clients use `file:///isma/<documentId>.lisma` with a per-editor document id.

## Semantic Tokens (pull)

### Request

```json
{ "jsonrpc": "2.0", "id": 2, "method": "textDocument/semanticTokens/full", "params": { "textDocument": { "uri": "file:///isma/<documentId>.lisma" } } }
```

### Response

```json
{ "jsonrpc": "2.0", "id": 2, "result": { "data": [0, 0, 5, 0, 0, 0, 10, 3, 2, 0] } }
```

`data` is a flat array of integers: **5 per token** — `[deltaLine, deltaStart, length, tokenType, tokenModifiers]`.

### Integer Encoding

- `deltaLine` — cumulative: `line += deltaLine`.
- `deltaStart` — relative to the previous token's start **when `deltaLine == 0`**, absolute otherwise:
  ```
  if (deltaLine == 0) start += deltaStart
  else { line += deltaLine; start = deltaStart }
  ```
- `tokenType` — index into the legend (`0` = keyword, `1` = comment, `2` = number).
- `tokenModifiers` — always `0` (no modifiers used).

Tokens are emitted sorted by `(line, startChar)`. The encoder starts from baseline `line = 0, start = 0`.

### Worked Example

Source (line numbers in comments):

```
const a = 1.5;      // line 0
x(t0) = 2;          // line 1
state Main (x0) {   // line 2
    /* block        // line 3
       comment */   // line 4
    x' = -a * x;    // line 5
}                   // line 6
// line comment     // line 7
```

Tokens (absolute): `(0,0,5,keyword)`, `(0,10,3,number)`, `(1,8,1,number)`, `(2,0,5,keyword)`, `(3,4,9,comment)`, `(4,0,17,comment)`, `(7,0,16,comment)`.

Encoded `data`:

```
[0,0,5,0,0,   0,10,3,2,0,   1,8,1,2,0,   1,0,5,0,0,
 1,4,9,1,0,   1,0,17,1,0,   3,0,16,1,0]
```

Note the transition from token 2 to 3: `deltaLine = 1`, so `deltaStart` is the absolute column `8`. The two block-comment segments (line 3 col 4, line 4 col 0) are on different lines, so the second is encoded with `deltaLine = 1` and absolute `deltaStart = 0`.

### Lexer Facts Affecting Tokens

- 8 keyword tokens: `const`, `state`, `for`, `if`, `else`, `from`, `macro`, `set`.
- `SL_COMMENT` includes the trailing newline — a line comment token spans to end of line including `\n`.
- Multi-line block comments: every line except the last extends to end of line **including** the newline; the last line ends at the token end.
- `x0`, `t0` are single `Identifier` tokens — no number token inside identifiers.

## Lifecycle

| Method | Direction | Behavior |
|--------|-----------|----------|
| `shutdown` | client → server | Responds with `null`; the server marks shutdown requested |
| `exit` | client → server | `exit(0)` if shutdown was requested, `exit(1)` otherwise |

The `exit` notification is sent after the `shutdown` response; clients treat a missing response as a timeout (10 s in both UI clients) and fall back to killing the process.

## Smoke Test

`isma-lsp/smoke.py` drives the installed binary over a real stdio subprocess:

1. `initialize` → asserts capabilities and legend
2. `textDocument/didOpen` with the 8-line example above
3. `textDocument/semanticTokens/full` → asserts the exact 7-token encoding
4. `textDocument/didChange` to `const b = 7;` → asserts `[(0,0,5,keyword), (0,10,1,number)]`
5. `textDocument/didClose`, `shutdown`, `exit` → asserts exit code 0

```bash
./gradlew :isma-lsp:installDist
python3 isma-lsp/smoke.py isma-lsp/build/install/isma-lsp/bin/isma-lsp
```
