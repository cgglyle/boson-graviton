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

import com.cgglyle.boson.graviton.annotaion.GravitonLog;
import com.cgglyle.boson.graviton.model.LogInfo;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 默认提供的日志处理服务
 *
 * @author lyle
 * @since 2022/09/10
 */
@Service
public class DefaultLogControllerServiceImpl implements LogControllerService{
    /**
     * 日志前置处理
     * <p>
     * 可以通过织入点获得被注解标记的函数的信息
     *
     * @param joinPoint   织入点信息
     * @param gravitonLog 注解信息
     * @param logInfo 日志信息
     */
    @Override
    public void preprocessing(JoinPoint joinPoint, GravitonLog gravitonLog, LogInfo logInfo) {
        logInfo.setSuccessTemplate(gravitonLog.successTemplate());
        logInfo.setFailureTemplate(gravitonLog.failureTemplate());
        logInfo.setClassName(joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName());
        List<Object> objects = Arrays.asList(joinPoint.getArgs());
        logInfo.setInParameter(objects);
        logInfo.setStatus(true);
    }

    /**
     * 日志后置处理
     * <p>
     * 可以通过body获得函数操作过后的出参
     * <h3>注意！</h3>
     * {@code body}信息将被直接诶返回，不建议对body做任何的处理，建议只用于提取信息。
     *
     * @param body        函数操作过后的出参
     * @param logInfo 包含前置处理信息的日志信息
     */
    @Override
    public void postprocessing(Object body, LogInfo logInfo) {
        logInfo.setOutParameter(body);
    }

    /**
     * 异常处理
     * <p>
     * 当被标记的函数发生异常，这个函数会被调用，不建议在此处做任何异常处理。请只提取信息。
     *
     * @param throwable   异常信息
     * @param logInfo 包含前置处理信息的日志信息
     */
    @Override
    public void exceptionProcessing(Throwable throwable, LogInfo logInfo) {
        logInfo.setException(throwable);
        logInfo.setStatus(false);
    }
}
