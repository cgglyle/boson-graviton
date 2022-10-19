/*
 * Copyright 2022 Cgglyle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cgglyle.boson.graviton.test.autoconfigure;

import io.github.cgglyle.boson.graviton.test.autoconfigure.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

/**
 * @author Lyle
 * @since 2022/09/17
 */
@Slf4j
@EnableAsync
@RequiredArgsConstructor
@SpringBootApplication
public class ApplicationTest {
    private final TestService service;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class, args);
        log.info("系统启动成功");
    }

    @PostConstruct
    public String test() {
        return service.testString("etsestset");
    }
}
