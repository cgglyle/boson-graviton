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

package com.cgglyle.boson.graviton.api;

import org.aspectj.lang.JoinPoint;
import org.springframework.lang.Nullable;

import java.util.function.Supplier;

/**
 * 日志SpEL解析模板
 *
 * @author Lyle
 * @since 2022/10/07
 */
public interface GravitonLogSpEL {
    /**
     * 解析SpEL
     *
     * @param point             切入点
     * @param getExpression     解释模板
     * @param proceed           执行结果
     * @param errorMsg          错误信息
     * @param parserResultClass 返回类型
     * @param <T>               类型
     * @return 解析后参数
     */
    <T> T parser(JoinPoint point, Supplier<String> getExpression,
                 @Nullable Object proceed, @Nullable String errorMsg, Class<T> parserResultClass);

    /**
     * 解析SpEL
     *
     * @param point             切入点
     * @param getExpression     解释模板
     * @param proceed           执行结果
     * @param errorMsg          错误信息
     * @param parserResultClass 返回类型
     * @param <T>               类型
     * @return 解析后参数
     */
    <T> T parser(JoinPoint point, String getExpression,
                 @Nullable Object proceed, @Nullable String errorMsg, Class<T> parserResultClass);
}
