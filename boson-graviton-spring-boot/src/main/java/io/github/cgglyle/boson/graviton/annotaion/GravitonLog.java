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

package io.github.cgglyle.boson.graviton.annotaion;


import io.github.cgglyle.boson.graviton.api.OrderNoGenerate;
import io.github.cgglyle.boson.graviton.model.LogInfo;
import io.github.cgglyle.boson.graviton.model.LogLevelEnum;
import io.github.cgglyle.boson.graviton.service.UUIDOrderNo;
import org.springframework.core.annotation.AliasFor;

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
     * 可以在{@link LogInfo}中找到支持的标签。 可以使用{@code {{time}}}实现对标签的引用（“{{xxx}}”外围的"{{"和"}}"是必须的）， 你也可以继承{@code LogInfo}来添加标签
     */
    String successTemplate() default "";

    /**
     * 失败模板
     * <p>
     * 不填写此项将默认使用内置的日志模板
     * <p>
     * 可以在{@link LogInfo}中找到支持的标签。 可以使用{@code {{time}}}实现对标签的引用（“{{xxx}}”外围的"{{"和"}}"是必须的）， 你也可以继承{@code LogInfo}来添加标签
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

    /**
     * 业务日志
     * <p>
     * 支持SpEL, SpEl 使用 "#{}" 作为模板，请按照下方示例编写SpEL语句
     * <p>
     * {@code @GravitonLog(success = "Service 操作人 #{#userName} 将 #{#testContext} 变更为 #{#str} #{#root.methodName},
     * #{#result}")}
     */
    @AliasFor("value")
    String success() default "";

    /**
     * 业务日志
     * <p>
     * 支持SpEL
     */
    @AliasFor("success")
    String value() default "";

    /**
     * 业务失败日志
     * <p>
     * 支持SpEL, SpEl 使用 "#{}" 作为模板，请按照下方示例编写SpEL语句
     * <p>
     * {@code @GravitonLog(failure = "Service 操作人 #{#userName} 将 #{#testContext} 变更为 #{#str} 操作失败")
     */
    String failure() default "";

    /**
     * 是否开启系统日志
     * <p>
     * 默认开启
     */
    boolean enableSystem() default true;

    /**
     * 是否开启业务日志
     * <p>
     * 默认开启
     */
    boolean enableBusiness() default true;

    /**
     * 标识号
     * <p>
     * 同一次的调用将使用统一的标识号，方便后续检查日志。可以使用{@link GravitonLog#enableOrderNo()} 进行控制是否启用orderNo。
     * <p>
     * 默认情况下是关闭的，启用后可以选择使用雪花算法或者UUID进行填充或者是自定义填充（支持SpEL）。
     * <p>
     * 如果没有设置{@code orderNo}且开启了{@link GravitonLog#enableOrderNo()}，该情况下将会使用
     * {@link GravitonLog#orderNoClass()}来实现{@code orderNo}。
     * <p>
     * TODO: 实现 对于转变前日志的调用
     *
     * @see #orderNoClass()
     * @see #enableOrderNo()
     */
    String orderNo() default "";

    /**
     * 标识号实现类选择
     * <p>
     * 如果没有设置{@link GravitonLog#orderNo()}且开启了{@link GravitonLog#enableOrderNo()}，该情况下将会使用
     * {@code orderNoClass}来实现{@code orderNo}。
     * <p>
     * 默认实现为{@link UUIDOrderNo}
     *
     * @see #orderNo()
     * @see #enableOrderNo()
     */
    Class<? extends OrderNoGenerate> orderNoClass() default UUIDOrderNo.class;

    /**
     * 是否启用orderNo，默认为关闭
     */
    boolean enableOrderNo() default false;

    /**
     * 系统日志失败打印级别
     */
    LogLevelEnum systemErrorLogLevel() default LogLevelEnum.ERROR;

    /**
     * 系统日志成功打印级别
     */
    LogLevelEnum systemSuccessLogLevel() default LogLevelEnum.INFO;

    /**
     * 业务日志失败打印级别
     */
    LogLevelEnum businessErrorLogLevel() default LogLevelEnum.WARN;

    /**
     * 业务日志成功打印级别
     */
    LogLevelEnum businessSuccessLogLevel() default LogLevelEnum.INFO;
}
