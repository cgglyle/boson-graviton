package io.github.cgglyle.boson.graviton.boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @author Lyle Liu
 */
@RestController
@RequestMapping("/")
public class BosonGravitonController {
    private final LoggerTestSecond loggerTestSecond;

    public BosonGravitonController(LoggerTestSecond loggerTestSecond) {
        this.loggerTestSecond = loggerTestSecond;
    }

    @EnableLogger
    @GetMapping
    public String get(String s) throws ExecutionException, InterruptedException {
        String s1 = loggerTestSecond.loggerTest(s);
        if (s1.equals("ssssss Async sss")){
            throw new RuntimeException();
        }
        return s1;
    }
}
