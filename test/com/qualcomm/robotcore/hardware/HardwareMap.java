package com.qualcomm.robotcore.hardware;

import java.util.Collections;
import java.util.List;

/**
 * Minimal stub of the FTC SDK's {@code HardwareMap} class, used only by the
 * local test harness in {@code test/}.  Only the surface exercised by
 * {@code VoltageCompensatedTeleOp} is declared.
 *
 * <p>{@link #voltageSensor} returns an empty list by default \u2014 the real
 * SDK wires the Control Hub's battery sensor here at runtime.  The unit
 * tests never exercise that lookup because {@code update(...)} takes a
 * {@link VoltageSensor} directly.
 */
public class HardwareMap {

    public List<VoltageSensor> voltageSensor = Collections.emptyList();

    public <T> T get(Class<T> clazz, String name) {
        return null;
    }
}
