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

package com.cgglyle.boson.graviton.test.autoconfigure.controller;

import com.cgglyle.boson.graviton.annotaion.GravitonLog;
import com.cgglyle.boson.graviton.test.autoconfigure.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lyle
 * @since 2022/09/18
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TestController {

    private final TestService service;

    @GravitonLog(success = "操作人 #{#userName} 将 #{#testContext} 变更为 #{#str} #{#root.methodName}, #{#result}",
            failure = "操作人 #{#userName} 将 #{#testContext} 变更为 #{#str} 操作失败")
    @GetMapping("test/{str}")
    public String test(@PathVariable String str) {
        return service.testString(str);
    }

    @GravitonLog(success = "操作人 #{#userName} 将 #{#testContext} 变更为 #{#str} #{#root.methodName}, #{#result}",
            failure = "操作人 #{#userName} 将 #{#testContext} 变更为 #{#str} 操作失败")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @GetMapping("test/Exception")
    public void testException() {
        service.testException();
    }
}
