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

package io.github.cgglyle.boson.graviton.test.controller;

import io.github.cgglyle.boson.graviton.annotaion.EnableGravitonOrderNo;
import io.github.cgglyle.boson.graviton.annotaion.GravitonLog;
import io.github.cgglyle.boson.graviton.model.LogLevelEnum;
import io.github.cgglyle.boson.graviton.service.GravitonLogContext;
import io.github.cgglyle.boson.graviton.test.entity.TestEntity;
import io.github.cgglyle.boson.graviton.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lyle
 * @since 2022/09/17
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@EnableGravitonOrderNo
public class TestController {

    private final TestService service;

    @GravitonLog(success = "Controoler 操作人 #{#username} 将 #{#testContext} 变更为 #{#str} #{#root.methodName}, #{#result}",
            failure = "Controoler 操作人 #{#username} 将 #{#testContext} 变更为 #{#str} 操作失败, 失败原因#{#errorMsg}",
            systemSuccessLogLevel = LogLevelEnum.WARN)
    @ResponseBody
    @GetMapping("test/{str}")
    public String test(@PathVariable String str) {
        GravitonLogContext.createLogContext();
        GravitonLogContext.putVariable("username", "testController 小王");
        GravitonLogContext.putVariable("testContext", "testController OK!");
        if (str.equals("cException")) {
            throw new RuntimeException("graviton test controller exception");
        }
        return service.testString(str) + " controller test";
    }

    @GravitonLog(success = "Controoler 操作人 #{#username} 将 #{#testContext} 变更为 #{#str} #{#root.methodName}, #{#result.username}",
            failure = "Controoler 操作人 #{#username} 将 #{#testContext} 变更为 #{#str} 操作失败, 失败原因#{#errorMsg}")
    @ResponseBody
    @GetMapping("test/obj/{str}")
    public TestEntity testObj(@PathVariable String str) {
        GravitonLogContext.createLogContext();
        GravitonLogContext.putVariable("username", "testObjController 小王");
        GravitonLogContext.putVariable("testContext", "testObjController OK!");
        TestEntity testEntity = new TestEntity();
        testEntity.setUsername(str);
        testEntity.setPassword("123456");
        return service.testObj(testEntity);
    }
}
