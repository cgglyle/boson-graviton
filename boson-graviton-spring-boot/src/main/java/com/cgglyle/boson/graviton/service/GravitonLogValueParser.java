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

import com.cgglyle.boson.graviton.api.GravitonLogSpEL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * @author Lyle
 * @since 2022/10/05
 */
@Service
public class GravitonLogValueParser implements BeanFactoryAware, GravitonLogSpEL {
    private final GravitonLogExpressionEvaluator logExpressionEvaluator = new GravitonLogExpressionEvaluator();
    @Nullable
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public <T> T parser(JoinPoint point, Supplier<String> getExpression, @Nullable Object proceed, @Nullable String errorMsg, Class<T> parserResultClass) {
        return parserOf(logExpressionEvaluator::parse, getExpression, parserResultClass).parser(point, proceed, errorMsg);
    }

    @Override
    public <T> T parser(JoinPoint point, String getExpression, @Nullable Object proceed, @Nullable String errorMsg, Class<T> parserResultClass) {
        return parserOf(logExpressionEvaluator::parse, getExpression, parserResultClass).parser(point, proceed, errorMsg);
    }

    private <T> ParserByAop<T> parserOf(ParserTo parser, String getExpression, Class<T> parserResultClass) {
        return (point, proceed, errorMsg) -> {
            ExpressionArgs expressionArgs = getExpressionArgs(point, proceed, errorMsg);
            return parser.parser(getExpression, expressionArgs.getMethodKey(), expressionArgs.getContext(), parserResultClass);
        };
    }

    private <T> ParserByAop<T> parserOf(ParserTo parser, Supplier<String> getExpression, Class<T> parserResultClass) {
        return (point, proceed, errorMsg) -> {
            ExpressionArgs expressionArgs = getExpressionArgs(point, proceed, errorMsg);
            return parser.parser(getExpression.get(), expressionArgs.getMethodKey(), expressionArgs.getContext(), parserResultClass);
        };
    }

    private ExpressionArgs getExpressionArgs(JoinPoint point, @Nullable Object proceed, @Nullable String errorMsg) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Class<?> targetClass = point.getTarget().getClass();
        EvaluationContext evaluationContext = logExpressionEvaluator.createEvaluationContext(method, point.getArgs()
                , point.getTarget(), targetClass, proceed, errorMsg, beanFactory);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        return new ExpressionArgs(evaluationContext, methodKey);
    }

    @FunctionalInterface
    private interface ParserByAop<T> {
        T parser(JoinPoint point, @Nullable Object proceed, @Nullable String errorMsg);
    }

    @FunctionalInterface
    private interface Parser<T> {
        T parser(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext);
    }

    @FunctionalInterface
    private interface ParserTo {
        <T> T parser(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext, Class<T> parserResultClass);
    }

    @Getter
    @AllArgsConstructor
    private static class ExpressionArgs {
        private final EvaluationContext context;
        private final AnnotatedElementKey methodKey;
    }
}
