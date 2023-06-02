package io.github.cgglyle.boson.graviton.boot;

import groovy.util.logging.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Lyle Liu
 */
//@Component
public class BosonGravitonLoggerTest implements ApplicationRunner {
    private final LoggerTestSecond loggerTestSecond;

    public BosonGravitonLoggerTest(LoggerTestSecond loggerTestSecond) {
        this.loggerTestSecond = loggerTestSecond;
    }

    @EnableLogger
    @Override
    public void run(ApplicationArguments args) throws Exception {
        loggerTestSecond.loggerTest("ssss");
    }
}
