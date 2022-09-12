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

package com.cgglyle.boson.graviton.aop;


import com.cgglyle.boson.graviton.annotaion.GravitonLog;
import com.cgglyle.boson.graviton.model.LogInfo;
import com.cgglyle.boson.graviton.service.LogControllerService;
import com.cgglyle.boson.graviton.service.LogPrintfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 统一日志AOP
 *
 * @author lyle
 * @since 2022/08/13
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GravitonLogAspect {
    private final LogControllerService logControllerService;
    private final LogPrintfService logPrintfService;
    private final LogInfo logInfo;
    private long serviceStartTime;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.cgglyle.boson.graviton.annotaion.GravitonLog)")
    public void unityLogCut() {
    }

    /**
     * 切入前信息处理
     *
     * @param joinPoint 连节点
     */
    @Before(value = "unityLogCut()&&@annotation(gravitonLog)")
    public void unityLog(JoinPoint joinPoint, GravitonLog gravitonLog) {
        logControllerService.preprocessing(joinPoint, gravitonLog, logInfo);
    }

    /**
     * 含参数验证环绕切入
     */
    @Around(value = "unityLogCut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logInfo.setStartTime(LocalDateTime.now());
        serviceStartTime = System.currentTimeMillis();
        return proceedingJoinPoint.proceed();
    }

    /**
     * 切入后信息处理
     */
    @AfterReturning(value = "unityLogCut()", returning = "body")
    public void doAfterReturning(Object body) {
        logControllerService.postprocessing(body, logInfo);
    }

    /**
     * 切入后信息处理，固有
     */
    @After(value = "unityLogCut()")
    public void doAfter() {
        logInfo.setEndTime(LocalDateTime.now());
        logInfo.setConsumeTime(System.currentTimeMillis() - serviceStartTime);
        logPrintfService.log(logInfo);
    }

    /**
     * 异常处理
     */
    @AfterThrowing(value = "unityLogCut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        logControllerService.exceptionProcessing(throwable, logInfo);
    }
}
