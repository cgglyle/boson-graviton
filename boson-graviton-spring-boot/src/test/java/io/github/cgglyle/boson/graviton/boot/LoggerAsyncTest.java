package io.github.cgglyle.boson.graviton.boot;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author Lyle Liu
 */
@Component
public class LoggerAsyncTest {

    @Async
    @EnableLogger
    public CompletableFuture<String> getLoggerAsync(String s){
        return CompletableFuture.completedFuture(s + " Async " + s);
    }
}
