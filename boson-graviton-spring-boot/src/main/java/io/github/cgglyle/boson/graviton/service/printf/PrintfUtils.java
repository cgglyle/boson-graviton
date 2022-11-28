package io.github.cgglyle.boson.graviton.service.printf;

import io.github.cgglyle.boson.graviton.model.LogLevelEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Lyle
 * @since 2022/11/20
 */
@Slf4j
public class PrintfUtils {
    public static void printf(LogLevelEnum logLevelEnum, String str) {
        switch (logLevelEnum) {
            case INFO -> log.info(str);
            case WARN -> log.warn(str);
            case ERROR -> log.error(str);
            case DEBUG -> log.debug(str);
        }
    }
}
