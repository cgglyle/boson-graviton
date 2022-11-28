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

package io.github.cgglyle.boson.graviton.service.spel;

import io.github.cgglyle.boson.graviton.model.LogContext;
import lombok.extern.slf4j.Slf4j;

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
public final class GravitonLogContext {
    private static final Stack<Map<String, Object>> LOG_CONTEXT = new Stack<>();
    private static final Map<String, Field[]> FIELD_CACHE = new HashMap<>();

    private GravitonLogContext() {
    }

    /**
     * 获取日志MAP
     *
     * @return 日志MAP
     */
    public static Map<String, Object> getLogMap() {
        if (LOG_CONTEXT.isEmpty()) {
            return null;
        }
        return LOG_CONTEXT.pop();
    }

    /**
     * 因为存在连续调用情况，如果是同一个类中的调用，请使用此方法创建一个新的栈
     * <p>
     * 连续调用不使用此方法可能会导致数据混乱！
     */
    public static void createLogContext() {
        LOG_CONTEXT.push(new HashMap<>());
    }

    /**
     * 存入一个对象
     * <p>
     * 对象会根据成员名称被存入上下文中
     * <p>
     * 注意！此方法只是建议被内部使用，并不建议外部使用
     *
     * @param value LogInfo对象
     */
    public static void putVariable(LogContext value) {
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
                putVariable(name, o);
            } catch (IllegalAccessException e) {
                log.error("GravitonLogContext put variable error:" + e.getMessage(), e);
            }

        }
    }

    /**
     * 存入一条数据，会将变量存入GravitonSpEL上下文变量中
     * <p>
     * 注意！请不要使用{@link LogContext}中存在的成员变量名，否则会在解析过程中被日志信息覆盖！
     *
     * @param key   变量名
     * @param value 数据
     */
    public static void putVariable(String key, Object value) {
        if (LOG_CONTEXT.isEmpty()) {
            LOG_CONTEXT.push(new HashMap<>());
        }
        Map<String, Object> logMap = LOG_CONTEXT.peek();
        logMap.put(key, value);
    }
}
