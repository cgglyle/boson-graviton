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

package com.cgglyle.boson.graviton.test.controller;

import com.cgglyle.boson.graviton.annotaion.GravitonLog;
import com.cgglyle.boson.graviton.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lyle
 * @since 2022/09/17
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TestController {

    private final TestService service;

    @GravitonLog
    @GetMapping("test/{str}")
    public String test(@PathVariable String str) {
        return service.testString(str) + " controller test";
    }
}
