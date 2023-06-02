package io.github.cgglyle.boson.graviton.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Lyle Liu
 */
@SpringBootApplication
@EnableAsync
public class BosonGravitonTest {

    public static void main(String[] args) {
        SpringApplication.run(BosonGravitonTest.class, args);
    }
}
