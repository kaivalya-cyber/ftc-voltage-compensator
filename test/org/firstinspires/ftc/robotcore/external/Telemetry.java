package org.firstinspires.ftc.robotcore.external;

/**
 * Minimal stub of the FTC SDK's {@code Telemetry} interface, used only by the
 * local test harness in {@code test/}.  Only the {@code addData} overloads
 * exercised by {@code VoltageCompensator} are declared here.  This stub is at
 * the same fully-qualified name as the real class so the production source
 * compiles against either this stub or the real SDK without modification.
 */
public interface Telemetry {

    Telemetry addData(String key, String value);

    Telemetry addData(String key, Object value);

    Telemetry addData(String key, String format, Object... args);

    void update();
}
