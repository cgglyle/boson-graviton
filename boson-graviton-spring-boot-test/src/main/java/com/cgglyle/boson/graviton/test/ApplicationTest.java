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

package com.cgglyle.boson.graviton.test;

import com.cgglyle.boson.graviton.test.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
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
@ComponentScan(basePackages = "com.cgglyle.boson.graviton.*")
public class ApplicationTest {
    @Autowired
    private TestService service;

    @Async
    @PostConstruct
    public void test() {
        String s = service.testString("test");
        log.info(s);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class, args);
        log.info("系统启动成功");
    }
}
