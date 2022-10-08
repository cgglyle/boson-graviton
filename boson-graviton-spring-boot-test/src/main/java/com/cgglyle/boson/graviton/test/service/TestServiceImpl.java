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

package com.cgglyle.boson.graviton.test.service;

import com.cgglyle.boson.graviton.annotaion.GravitonLog;
import com.cgglyle.boson.graviton.service.GravitonLogContext;
import org.springframework.stereotype.Service;

/**
 * @author Lyle
 * @since 2022/09/17
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    @GravitonLog(success = "操作人 #{#userName} 将 #{#testContext} 变更为 #{#str} #{#root.methodName}, #{#result}",
            failure = "操作人 #{#userName} 将 #{#testContext} 变更为 #{#str} 操作失败")
    public String testString(String str) {
        GravitonLogContext.putVariable("testContext", str + "OK!");
        GravitonLogContext.putVariable("userName", str + "小王");
        if (str.equals("sException")) {
            throw new RuntimeException("graviton test service exception");
        }
        return str;
    }
}
