package com.qualcomm.robotcore.util;

/**
 * Minimal stub of the FTC SDK's {@code Range} utility, used only by the local
 * test harness in {@code test/}.  Only {@link #clip(double, double, double)}
 * is exercised by {@code VoltageCompensator}.
 */
public final class Range {

    private Range() { }

    /** Clamps {@code value} into the inclusive range {@code [min, max]}. */
    public static double clip(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
