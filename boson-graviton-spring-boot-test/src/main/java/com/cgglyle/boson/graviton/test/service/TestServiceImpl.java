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
import org.springframework.stereotype.Service;

/**
 * @author Lyle
 * @since 2022/09/17
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    @GravitonLog
    public String testString(String str) {
        return str;
    }
}
