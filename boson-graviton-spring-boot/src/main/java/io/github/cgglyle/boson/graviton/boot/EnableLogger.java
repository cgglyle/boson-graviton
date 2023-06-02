package io.github.cgglyle.boson.graviton.boot;

import java.lang.annotation.*;

/**
 * @author Lyle Liu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableLogger {
}
