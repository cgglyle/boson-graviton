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

package com.cgglyle.boson.graviton.annotaion;


import java.lang.annotation.*;

/**
 * 日志介入注解
 * <p>
 * 可以标注在函数或者类上，标注在类上将会使此类中的所有函数都会被日志服务接管。
 *
 * @author lyle
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GravitonLog {
    /**
     * 模块
     */
    String module() default "";

    /**
     * 解释
     */
    String explain() default "";

    /**
     * 成功模板
     * <p>
     * 不填写此项将默认使用内置的日志模板
     * <p>
     * 可以在{@link com.cgglyle.boson.graviton.model.LogInfo}中找到支持的标签。
     * 可以使用{@code {{time}}}实现对标签的引用（“{{xxx}}”外围的"{{"和"}}"是必须的）， 你也可以继承{@code LogInfo}来添加标签
     */
    String successTemplate() default "";

    /**
     * 失败模板
     * <p>
     * 不填写此项将默认使用内置的日志模板
     * <p>
     * 可以在{@link com.cgglyle.boson.graviton.model.LogInfo}中找到支持的标签。
     * 可以使用{@code {{time}}}实现对标签的引用（“{{xxx}}”外围的"{{"和"}}"是必须的）， 你也可以继承{@code LogInfo}来添加标签
     */
    String failureTemplate() default "";

    /**
     * 是否开启异步
     */
    boolean async() default true;

    /**
     * 日期格式
     */
    String timeFormat() default "";
}
