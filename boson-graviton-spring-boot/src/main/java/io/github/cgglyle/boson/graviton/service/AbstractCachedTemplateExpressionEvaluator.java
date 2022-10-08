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

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * {@link CachedExpressionEvaluator}的模板解析版本
 *
 * @author Lyle
 * @since 2022/10/05
 */
public abstract class AbstractCachedTemplateExpressionEvaluator {
    private static final String EXPRESSION_PREFIX = "#{";
    private static final String EXPRESSION_SUFFIX = "}";
    private final SpelExpressionParser parser;
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 使用默认的 {@link SpelExpressionParser} 创建一个新实例。
     */
    protected AbstractCachedTemplateExpressionEvaluator() {
        this(new SpelExpressionParser());
    }

    /**
     * 使用指定的 {@link SpelExpressionParser} 创建一个新实例。
     */
    protected AbstractCachedTemplateExpressionEvaluator(SpelExpressionParser parser) {
        Assert.notNull(parser, "SpElExpressionParser must not be null");
        this.parser = parser;
    }

    /**
     * 返回一个共享参数名称发现器，它在内部缓存数据。
     */
    protected ParameterNameDiscoverer getParameterNameDiscoverer() {
        return this.parameterNameDiscoverer;
    }

    /**
     * 返回指定 SpEL 值的 {@link Expression} 使用默认的 #{ 前缀和 } 后缀创建一个新的 TemplateParserContext。
     * <p>如果还没有解析表达式。
     *
     * @param cache      要使用的缓存
     * @param elementKey 定义表达式的元素
     * @param expression 要解析的表达式
     */
    protected Expression getExpression(Map<ExpressionKey, Expression> cache,
                                       AnnotatedElementKey elementKey, String expression) {

        ExpressionKey expressionKey = createKey(elementKey, expression);
        Expression expr = cache.get(expressionKey);
        if (expr == null) {
            expr = getParser().parseExpression(expression, new TemplateParserContext(EXPRESSION_PREFIX, EXPRESSION_SUFFIX));
            cache.put(expressionKey, expr);
        }
        return expr;
    }

    private ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
        return new ExpressionKey(elementKey, expression);
    }

    /**
     * 返回要使用的 {@link SpelExpressionParser}。
     */
    protected SpelExpressionParser getParser() {
        return this.parser;
    }

    /**
     * 一个表达式键。
     */
    protected static class ExpressionKey implements Comparable<ExpressionKey> {

        private final AnnotatedElementKey element;

        private final String expression;

        protected ExpressionKey(AnnotatedElementKey element, String expression) {
            Assert.notNull(element, "AnnotatedElementKey must not be null");
            Assert.notNull(expression, "Expression must not be null");
            this.element = element;
            this.expression = expression;
        }

        @Override
        public int hashCode() {
            return this.element.hashCode() * 29 + this.expression.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ExpressionKey otherKey)) {
                return false;
            }
            return (this.element.equals(otherKey.element) &&
                    ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
        }

        @Override
        public String toString() {
            return this.element + " with expression \"" + this.expression + "\"";
        }

        @Override
        public int compareTo(ExpressionKey other) {
            int result = this.element.toString().compareTo(other.element.toString());
            if (result == 0) {
                result = this.expression.compareTo(other.expression);
            }
            return result;
        }
    }
}
