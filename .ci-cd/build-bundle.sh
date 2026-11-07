#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
BUNDLE_DIR="$PROJECT_ROOT/build/bundle"

rm -rf "$BUNDLE_DIR"
mkdir -p "$BUNDLE_DIR"

cd "$PROJECT_ROOT"

# Ensure app submodules are checked out
if [ ! -f isma-ui/settings.gradle.kts ] || [ ! -f grin/settings.gradle.kts ]; then
    git submodule update --init --recursive
fi

# isma-ui and grin are standalone Gradle projects (git submodules)
(cd isma-ui && ./gradlew :isma-ui:app:installDist)
(cd grin && ./gradlew :grin:gui:app:installDist)

# Daemon, language server, and standalone CLIs are built in the main repo
./gradlew :isma-daemon:app:installDist
./gradlew :lisma-compiler:lisma-lsp:installDist
./gradlew :lisma-compiler:cli:installDist
./gradlew :isma-solver:cli:installDist

cp -r "$PROJECT_ROOT/isma-ui/isma-ui/app/build/install/app" "$BUNDLE_DIR/isma-ui-app"
cp -r "$PROJECT_ROOT/isma-daemon/app/build/install/app" "$BUNDLE_DIR/isma-daemon-app"
cp -r "$PROJECT_ROOT/grin/grin/gui/app/build/install/app" "$BUNDLE_DIR/grin-app"
cp -r "$PROJECT_ROOT/lisma-compiler/lisma-lsp/build/install/isma-lsp" "$BUNDLE_DIR/isma-lsp-app"
cp -r "$PROJECT_ROOT/lisma-compiler/cli/build/install/lisma-compiler-cli" "$BUNDLE_DIR/lisma-compiler-cli-app"
cp -r "$PROJECT_ROOT/isma-solver/cli/build/install/simulation-runner-cli" "$BUNDLE_DIR/simulation-runner-cli-app"

cat > "$BUNDLE_DIR/run-ui.sh" << 'SCRIPT'
#!/bin/bash
set -e
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"
exec env \
  "ISMA_DAEMON_SCRIPT=$SCRIPT_DIR/isma-daemon-app/bin/app" \
  "ISMA_GRIN_SCRIPT=$SCRIPT_DIR/grin-app/bin/app" \
  "ISMA_LSP_SCRIPT=$SCRIPT_DIR/isma-lsp-app/bin/isma-lsp" \
  "ISMA_COMPILER_CLI=$SCRIPT_DIR/lisma-compiler-cli-app/bin/lisma-compiler-cli" \
  "ISMA_RUNNER_CLI=$SCRIPT_DIR/simulation-runner-cli-app/bin/simulation-runner-cli" \
  "$SCRIPT_DIR/isma-ui-app/bin/app"
SCRIPT
chmod +x "$BUNDLE_DIR/run-ui.sh"

echo "Bundles created in $BUNDLE_DIR"
ls -la "$BUNDLE_DIR"
