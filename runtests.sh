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
javac -d build \
  -Xlint:all \
  $(find . -maxdepth 1 -name '*.java') \
  $(find test -name '*.java')

echo "Compilation OK."
echo

java -ea -cp build org.firstinspires.ftc.teamcode.util.VoltageCompensatorTest
