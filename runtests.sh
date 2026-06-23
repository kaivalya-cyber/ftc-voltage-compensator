#!/bin/sh
# Compile and run the VoltageCompensator unit tests locally.
# Requires only a JDK (Java 11+); uses the lightweight FTC SDK stubs in test/.
set -e

cd "$(dirname "$0")"

rm -rf build
mkdir -p build

javac -d build \
  -Xlint:all \
  VoltageCompensator.java \
  test/com/qualcomm/robotcore/hardware/VoltageSensor.java \
  test/com/qualcomm/robotcore/util/Range.java \
  test/org/firstinspires/ftc/robotcore/external/Telemetry.java \
  test/org/firstinspires/ftc/teamcode/util/VoltageCompensatorTest.java

echo "Compilation OK."
echo

java -ea -cp build org.firstinspires.ftc.teamcode.util.VoltageCompensatorTest
