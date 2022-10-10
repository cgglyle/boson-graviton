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

package io.github.cgglyle.boson.graviton.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 日志上下文静态方法
 *
 * @author Lyle
 * @since 2022/10/07
 */
@Slf4j
@Service
public class GravitonLogContext {
    private static final Stack<Map<String, Object>> LOG_CONTEXT = new Stack<>();
    private static final Map<String, Field[]> FIELD_CACHE = new HashMap<>();
    private static Long threadId;

    private GravitonLogContext() {
    }

    /**
     * 存入一条数据，会将变量存入GravitonSpEL上下文变量中
     *
     * @param key   变量名
     * @param value 数据
     */
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

    /**
     * 存入一个对象
     * <p>
     * 对象会根据成员名称被存入上下文中
     *
     * @param value 对象
     */
    public static void putVariable(Object value) {
        Field[] fields;
        if (FIELD_CACHE.containsKey(value.getClass().getName())) {
            fields = FIELD_CACHE.get(value.getClass().getName());
        } else {
            fields = value.getClass().getDeclaredFields();
            FIELD_CACHE.put(value.getClass().getName(), fields);
        }
        for (Field field : fields) {
            String name = field.getName();
            field.setAccessible(true);
            try {
                Object o = field.get(value);
                GravitonLogContext.putVariable(name, o);
            } catch (IllegalAccessException e) {
                log.error("GravitonLogContext put variable error:" + e.getMessage(), e);
            }

        }
    }

    public static Map<String, Object> getLogMap() {
        if (LOG_CONTEXT.isEmpty()) {
            return null;
        }
        threadId = null;
        return LOG_CONTEXT.pop();
    }
}
