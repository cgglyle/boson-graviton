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

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志评估上下文
 *
 * @author Lyle
 * @since 2022/10/05
 */
public class GravitonLogEvaluationContext extends MethodBasedEvaluationContext {
    /**
     * 保存结果对象的变量的名称。
     */
    public static final String RESULT_VARIABLE = "result";
    public static final String ERROR_MSG_VARIABLE = "errorMsg";

    /**
     * 把方法的参数都放到 SpEL 解析的 RootObject 中
     */
    public GravitonLogEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
        Map<String, Object> logMap = GravitonLogContext.getLogMap();
        if (logMap != null) {
            logMap.forEach(super::setVariable);
        }
    }

    /**
     * 把方法的返回值放到 RootObject 中
     *
     * @param result 返回值
     */
    public void addResult(Object result) {
        //把方法的返回值放到 RootObject 中
        setVariable(RESULT_VARIABLE, result);
    }

    /**
     * 把方法的ErrorMsg放到 RootObject 中
     *
     * @param errorMsg 错误信息
     */
    public void addErrorMsg(String errorMsg) {
        setVariable(ERROR_MSG_VARIABLE, errorMsg);
    }
}
