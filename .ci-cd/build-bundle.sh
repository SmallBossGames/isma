#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
BUNDLE_DIR="$PROJECT_ROOT/build/bundle"

rm -rf "$BUNDLE_DIR"
mkdir -p "$BUNDLE_DIR"

cd "$PROJECT_ROOT"

./gradlew :isma-ui:app:installDist :isma-server:app:installDist

cp -r "$PROJECT_ROOT/isma-ui/app/build/install/app" "$BUNDLE_DIR/isma-ui-app"
cp -r "$PROJECT_ROOT/isma-server/app/build/install/app" "$BUNDLE_DIR/isma-server-app"

cat > "$BUNDLE_DIR/run-ui.sh" << 'SCRIPT'
#!/bin/bash
set -e
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"
exec env "ISMA_SERVER_SCRIPT=$SCRIPT_DIR/isma-server-app/bin/app" "$SCRIPT_DIR/isma-ui-app/bin/app"
SCRIPT
chmod +x "$BUNDLE_DIR/run-ui.sh"

echo "Bundles created in $BUNDLE_DIR"
ls -la "$BUNDLE_DIR"
