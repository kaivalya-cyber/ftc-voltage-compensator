#!/bin/sh
# Compile and run the VoltageCompensator unit tests locally.
# Requires only a JDK (Java 11+); uses the lightweight FTC SDK stubs in test/.
set -e

cd "$(dirname "$0")"

rm -rf build
mkdir -p build

# Glob over the two production sources at the repo root + every
# .java file under test/ (stub interfaces + VoltageCompensatorTest).
# Mirrors .github/workflows/build.yml so adding a new stub interface
# or test class no longer requires editing this script.
SOURCES="$(find . -maxdepth 1 -name '*.java') $(find test -name '*.java')"
if [ -z "$SOURCES" ]; then
    echo >&2 "No .java sources found (production + test/ tree)."
    exit 1
fi

javac -d build \
  -Xlint:all \
  $SOURCES

echo "Compilation OK."
echo

java -ea -cp build org.firstinspires.ftc.teamcode.util.VoltageCompensatorTest
