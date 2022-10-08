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

package com.cgglyle.boson.graviton.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Lyle
 * @since 2022/10/07
 */
@Service
public class GravitonLogContext {
    private static final Stack<Map<String, Object>> LOG_CONTEXT = new Stack<>();
    private static Long threadId;

    private GravitonLogContext() {
    }

    public static void putVariable(String key, Object value) {
        if (threadId == null) {
            threadId = Thread.currentThread().getId();
            HashMap<String, Object> logMap = new HashMap<>();
            LOG_CONTEXT.push(logMap);
        } else if (threadId != Thread.currentThread().getId()) {
            HashMap<String, Object> logMap = new HashMap<>();
            LOG_CONTEXT.push(logMap);
        }
        Map<String, Object> logMap = LOG_CONTEXT.peek();
        logMap.put(key, value);
    }

    public static Map<String, Object> getLogMap() {
        if (LOG_CONTEXT.isEmpty()) {
            return null;
        }
        threadId = null;
        return LOG_CONTEXT.pop();
    }
}
