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


import com.cgglyle.boson.graviton.annotaion.EnableGravitonOrderNo;
import com.cgglyle.boson.graviton.annotaion.GravitonAsync;
import com.cgglyle.boson.graviton.annotaion.GravitonLog;
import com.cgglyle.boson.graviton.api.LogControllerService;
import com.cgglyle.boson.graviton.api.LogScheduler;
import com.cgglyle.boson.graviton.api.LogSpELExpressionParser;
import com.cgglyle.boson.graviton.api.OrderNoGenerate;
import com.cgglyle.boson.graviton.model.LogInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 统一日志AOP
 *
 * @author lyle
 * @since 2022/08/13
 */
//@Aspect("pertarget(@annotation(com.cgglyle.boson.graviton.annotaion.GravitonLog))")
@Aspect
@RequiredArgsConstructor
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class GravitonLogAspect {
    private final LogControllerService logControllerService;
    private final LogScheduler logScheduler;
    private final LogSpELExpressionParser logSpElExpressionParser;
    private final Map<Long, Stack<LogInfo>> logInfoStackMap = new HashMap<>();
    private final Map<Long, String> orderNoMap = new HashMap<>();
    @Autowired
    private ApplicationContext context;

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
        GravitonAsync gravitonAsync = AnnotationUtils.findAnnotation(joinPoint.getSignature().getDeclaringType(), GravitonAsync.class);
        Stack<LogInfo> logInfoStack = logInfoStackMap.get(Thread.currentThread().getId());
        LogInfo logInfo = logInfoStack.peek();
        if (gravitonAsync != null) {
            logInfo.setAsync(gravitonAsync.async());
        } else {
            logInfo.setAsync(gravitonLog.async());
        }
        logControllerService.preprocessing(joinPoint, gravitonLog, logInfo);
    }

    /**
     * 含参数验证环绕切入
     */
    @Around(value = "unityLogCut()&&@annotation(gravitonLog)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, GravitonLog gravitonLog) throws Throwable {
        // 判断该线程是否在容器中有栈
        long id = Thread.currentThread().getId();
        if (!logInfoStackMap.containsKey(id)) {
            Stack<LogInfo> logInfoStack = new Stack<>();
            logInfoStackMap.put(id, logInfoStack);
        }
        Stack<LogInfo> logInfoStack = logInfoStackMap.get(id);
        // 判断栈中是否有信息，没有信息代表是第一次进入，添加一个OrderNo
        if (logInfoStack.size() == 0) {
            EnableGravitonOrderNo annotation = AnnotationUtils.findAnnotation(proceedingJoinPoint.getSignature().getDeclaringType(), EnableGravitonOrderNo.class);
            if (annotation != null | gravitonLog.enableOrderNo() && StringUtils.hasText(gravitonLog.orderNo())) {
                orderNoMap.put(id, logSpElExpressionParser.parser(gravitonLog.orderNo(), proceedingJoinPoint));
            } else if (annotation != null | gravitonLog.enableOrderNo()) {
                OrderNoGenerate bean = context.getBean(gravitonLog.orderNoClass());
                orderNoMap.put(id, bean.getOrderNo());
            }
        }
        logInfoStack.push(new LogInfo());
        LogInfo logInfo = logInfoStack.peek();
        logInfo.setOrderNo(orderNoMap.get(id));
        logInfo.setContent(gravitonLog.content());
        if (StringUtils.hasText(logInfo.getContent())) {
            logInfo.setContent(logSpElExpressionParser.parser(logInfo.getContent(), proceedingJoinPoint));
        }
        logInfo.setStartTime(LocalDateTime.now());
        return proceedingJoinPoint.proceed();
    }

    /**
     * 切入后信息处理
     */
    @AfterReturning(value = "unityLogCut()", returning = "body")
    public void doAfterReturning(Object body) {
        Stack<LogInfo> logInfoStack = logInfoStackMap.get(Thread.currentThread().getId());
        LogInfo logInfo = logInfoStack.peek();
        logControllerService.postprocessing(body, logInfo);
    }

    /**
     * 切入后信息处理，固有
     */
    @After(value = "unityLogCut()")
    public void doAfter() {
        long id = Thread.currentThread().getId();
        Stack<LogInfo> logInfoStack = logInfoStackMap.get(id);
        LogInfo logInfo = logInfoStack.pop();
        logInfo.setEndTime(LocalDateTime.now());
        logInfo.setConsumeTime(Duration.between(logInfo.getStartTime(), logInfo.getEndTime()).toMillis());
        logScheduler.startPrintf(logInfo);
        // 如果栈中为空，就移除相关线程的所有信息
        if (logInfoStack.size() == 0) {
            orderNoMap.remove(id);
            logInfoStackMap.remove(id);
        }
    }

    /**
     * 异常处理
     */
    @AfterThrowing(value = "unityLogCut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        Stack<LogInfo> logInfoStack = logInfoStackMap.get(Thread.currentThread().getId());
        LogInfo logInfo = logInfoStack.peek();
        logControllerService.exceptionProcessing(throwable, logInfo);
    }
}
