package io.github.cgglyle.boson.graviton.boot;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Lyle Liu
 */
@Component
public class LoggerTestSecond {
    private final LoggerAsyncTest loggerAsyncTest;

    public LoggerTestSecond(LoggerAsyncTest loggerAsyncTest) {
        this.loggerAsyncTest = loggerAsyncTest;
    }

    @EnableLogger
    public String loggerTest(String s) throws ExecutionException, InterruptedException {
        CompletableFuture<String> loggerAsync = loggerAsyncTest.getLoggerAsync(s);
        return s + loggerAsync.get();
    }
}
