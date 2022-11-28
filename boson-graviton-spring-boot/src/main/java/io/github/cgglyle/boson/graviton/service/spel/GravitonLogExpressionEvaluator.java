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

import io.github.cgglyle.boson.graviton.service.spel.AbstractCachedTemplateExpressionEvaluator;
import io.github.cgglyle.boson.graviton.service.spel.GravitonLogEvaluationContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志评估器
 *
 * @author Lyle
 * @since 2022/10/05
 */
public class GravitonLogExpressionEvaluator extends AbstractCachedTemplateExpressionEvaluator {
    private final Map<ExpressionKey, Expression> messageCache = new ConcurrentHashMap<>(64);
    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);
    private final Map<ExpressionKey, Expression> unlessCache = new ConcurrentHashMap<>(64);

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    /**
     * 解析指定表达式。
     */
    public <T> T parse(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext, Class<T> tClass) {
        return getExpression(this.messageCache, methodKey, expression).getValue(evalContext, tClass);
    }

    /**
     * 为指定方法的指定事件处理创建合适的 {@link EvaluationContext}。
     */
    public EvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Class<?> targetClass,
                                                     @Nullable Object result, @Nullable String errorMsg, @Nullable BeanFactory beanFactory) {
        ExpressionRootObject rootObject = new ExpressionRootObject(method, args, target, targetClass);
        GravitonLogEvaluationContext logEvaluationContext = new GravitonLogEvaluationContext(rootObject, getTargetMethod(targetClass, method), args, getParameterNameDiscoverer());
        logEvaluationContext.addResult(result);
        logEvaluationContext.addErrorMsg(errorMsg);
        if (beanFactory != null) {
            logEvaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return logEvaluationContext;
    }

    /**
     * 获取真正的具体的方法并缓存
     *
     * @param targetClass 目标class
     * @param method      来自接口或者父类的方法签名
     * @return 目标class实现的具体方法
     */
    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

    @Getter
    @AllArgsConstructor
    public static class ExpressionRootObject {
        private final Method method;

        private final Object[] args;

        private final Object target;

        private final Class<?> targetClass;

        /**
         * 使用#{#root.methodName}时调用
         */
        public String getMethodName() {
            return this.method.getName();
        }
    }
}
