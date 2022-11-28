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

package io.github.cgglyle.boson.graviton.test.service;

import io.github.cgglyle.boson.graviton.annotaion.EnableGravitonOrderNo;
import io.github.cgglyle.boson.graviton.annotaion.GravitonLog;
import io.github.cgglyle.boson.graviton.service.spel.GravitonLogContext;
import io.github.cgglyle.boson.graviton.test.entity.TestEntity;
import org.springframework.stereotype.Service;

/**
 * @author Lyle
 * @since 2022/09/17
 */
@Service
@EnableGravitonOrderNo
public class TestServiceImpl implements TestService {

    @Override
    @GravitonLog(businessSuccessTemplate = "Service 操作人 #{#username} 将 #{#testContext} 变更为 #{#str} #{#root.methodName}, #{#result}",
            businessFailureTemplate = "Service 操作人 #{#username} 将 #{#testContext} 变更为 #{#str} 操作失败, 失败原因#{#errorMsg}")
    public String testString(String str) {
        GravitonLogContext.createLogContext();
        GravitonLogContext.putVariable("testContext", str + "OK!");
        GravitonLogContext.putVariable("username", "testStringService 小王");
        if (str.equals("sException")) {
            throw new RuntimeException("graviton test service exception");
        }
        return str;
    }

    @Override
    @GravitonLog(businessSuccessTemplate = "Service 操作人 #{#username} 将 #{#testContext} 变更为 #{#str} #{#root.methodName}, #{#result.username}",
            businessFailureTemplate = "Service 操作人 #{#username} 将 #{#testContext} 变更为 #{#str} 操作失败, 失败原因#{#errorMsg}")
    public TestEntity testObj(TestEntity entity) {
        GravitonLogContext.createLogContext();
        GravitonLogContext.putVariable("testContext", "testObjService OK!");
//        GravitonLogContext.putVariable("username", "testObjService 小王");
        return entity;
    }
}
