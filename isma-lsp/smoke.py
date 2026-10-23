#!/usr/bin/env python3
"""LSP stdio smoke test for the LISMA language server CLI.

Usage: python3 smoke.py /path/to/isma-lsp/bin/isma-lsp

Exercises the full LSP lifecycle over stdio:
  initialize -> initialized -> didOpen -> semanticTokens/full ->
  didChange -> semanticTokens/full -> didClose -> shutdown -> exit

Exits 0 on success, 1 on any assertion failure.
"""
import json
import re
import subprocess
import sys

SOURCE = (
    "const a = 1.5;\n"
    "x(t0) = 2;\n"
    "state Main (x0) {\n"
    "    /* block\n"
    "       comment */\n"
    "    x' = -a * x;\n"
    "}\n"
    "// line comment\n"
)

# Absolute (line, startChar, length, typeIndex) — keyword=0, comment=1, number=2
EXPECTED_TOKENS = [
    (0, 0, 5, 0),    # const
    (0, 10, 3, 2),   # 1.5
    (1, 8, 1, 2),    # 2
    (2, 0, 5, 0),    # state
    (3, 4, 9, 1),    #     /* block (to end of line)
    (4, 0, 17, 1),   #        comment */
    (7, 0, 16, 1),   # // line comment (incl. trailing newline)
]


def frame(obj: dict) -> bytes:
    body = json.dumps(obj).encode("utf-8")
    return f"Content-Length: {len(body)}\r\n\r\n".encode("ascii") + body


class Reader:
    def __init__(self, stream):
        self.stream = stream

    def message(self) -> dict:
        header = b""
        while not header.endswith(b"\r\n\r\n"):
            chunk = self.stream.read(1)
            if not chunk:
                raise EOFError("server closed the stream")
            header += chunk
        match = re.search(rb"Content-Length: (\d+)", header)
        if not match:
            raise ValueError(f"missing Content-Length in header: {header!r}")
        length = int(match.group(1))
        body = self.stream.read(length)
        if len(body) != length:
            raise EOFError("truncated message body")
        return json.loads(body.decode("utf-8"))


def decode(data: list) -> list:
    tokens = []
    line, start = 0, 0
    for i in range(0, len(data), 5):
        delta_line, delta_start, length, token_type, _mods = data[i:i + 5]
        if delta_line == 0:
            start += delta_start
        else:
            line += delta_line
            start = delta_start
        tokens.append((line, start, length, token_type))
    return tokens


def main() -> int:
    if len(sys.argv) != 2:
        print(f"usage: {sys.argv[0]} <path-to-lisma-lsp-cli>", file=sys.stderr)
        return 1

    proc = subprocess.Popen(
        [sys.argv[1]],
        stdin=subprocess.PIPE,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
    )
    reader = Reader(proc.stdout)
    next_id = iter(range(1, 1000))

    def request(method: str, params: dict) -> dict:
        rid = next(next_id)
        proc.stdin.write(frame({"jsonrpc": "2.0", "id": rid, "method": method, "params": params}))
        proc.stdin.flush()
        msg = reader.message()
        if msg.get("id") != rid:
            raise AssertionError(f"unexpected response id: {msg}")
        if "error" in msg:
            raise AssertionError(f"{method} failed: {msg['error']}")
        return msg["result"]

    def notify(method: str, params: dict) -> None:
        proc.stdin.write(frame({"jsonrpc": "2.0", "method": method, "params": params}))
        proc.stdin.flush()

    try:
        # 1. initialize
        result = request("initialize", {
            "processId": None,
            "rootUri": None,
            "capabilities": {},
            "clientInfo": {"name": "smoke-test", "version": "1.0"},
        })
        capabilities = result["capabilities"]
        assert capabilities["textDocumentSync"] == 1, capabilities
        provider = capabilities["semanticTokensProvider"]
        assert provider["legend"]["tokenTypes"] == ["keyword", "comment", "number"], provider
        assert provider["full"] == {}, provider
        assert result["serverInfo"]["name"] == "lisma-lsp", result["serverInfo"]
        print("ok: initialize")

        notify("initialized", {})

        # 2. didOpen + semantic tokens
        uri = "file:///smoke.lisma"
        notify("textDocument/didOpen", {
            "textDocument": {"uri": uri, "languageId": "lisma", "version": 1, "text": SOURCE},
        })
        result = request("textDocument/semanticTokens/full", {"textDocument": {"uri": uri}})
        tokens = decode(result["data"])
        assert tokens == EXPECTED_TOKENS, f"\nexpected {EXPECTED_TOKENS}\ngot      {tokens}"
        print(f"ok: semanticTokens/full ({len(tokens)} tokens)")

        # 3. didChange + re-request
        notify("textDocument/didChange", {
            "textDocument": {"uri": uri, "version": 2},
            "contentChanges": [{"text": "const b = 7;"}],
        })
        result = request("textDocument/semanticTokens/full", {"textDocument": {"uri": uri}})
        tokens = decode(result["data"])
        assert tokens == [(0, 0, 5, 0), (0, 10, 1, 2)], tokens
        print("ok: didChange + semanticTokens/full")

        # 4. didClose
        notify("textDocument/didClose", {"textDocument": {"uri": uri}})
        result = request("textDocument/semanticTokens/full", {"textDocument": {"uri": uri}})
        assert decode(result["data"]) == [], result["data"]
        print("ok: didClose")

        # 5. shutdown + exit
        assert request("shutdown", {}) is None
        notify("exit", {})
        proc.stdin.close()
        exit_code = proc.wait(timeout=10)
        assert exit_code == 0, f"exit code {exit_code}"
        print("ok: shutdown + exit(0)")

        print("SMOKE TEST PASSED")
        return 0
    except AssertionError as e:
        print(f"FAILED: {e}", file=sys.stderr)
        return 1
    finally:
        if proc.poll() is None:
            proc.kill()
            stderr = proc.stderr.read().decode("utf-8", "replace")
            if stderr.strip():
                print(f"server stderr:\n{stderr}", file=sys.stderr)


if __name__ == "__main__":
    sys.exit(main())
