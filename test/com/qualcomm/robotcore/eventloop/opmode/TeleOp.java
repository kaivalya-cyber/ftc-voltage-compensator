package com.qualcomm.robotcore.eventloop.opmode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Minimal stub of the FTC SDK's {@code @TeleOp} annotation, used only by
 * the local test harness in {@code test/}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TeleOp {
    String name() default "";
}
