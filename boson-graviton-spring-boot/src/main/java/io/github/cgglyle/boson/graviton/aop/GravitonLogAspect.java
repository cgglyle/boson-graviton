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

package io.github.cgglyle.boson.graviton.aop;


import io.github.cgglyle.boson.graviton.annotaion.EnableGravitonOrderNo;
import io.github.cgglyle.boson.graviton.annotaion.GravitonAsync;
import io.github.cgglyle.boson.graviton.annotaion.GravitonLog;
import io.github.cgglyle.boson.graviton.api.GravitonLogSpEL;
import io.github.cgglyle.boson.graviton.api.LogControllerService;
import io.github.cgglyle.boson.graviton.api.LogScheduler;
import io.github.cgglyle.boson.graviton.api.OrderNoGenerate;
import io.github.cgglyle.boson.graviton.model.LogInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Stack;

/**
 * 统一日志AOP
 *
 * @author lyle
 * @since 2022/08/13
 */
@Aspect
@RequiredArgsConstructor
@Slf4j
@Order(-114514)
public class GravitonLogAspect {
    static final ThreadLocal<Stack<LogInfo>> THREAD_LOCAL_STACK_LOG_INFO = ThreadLocal.withInitial(Stack::new);
    static final ThreadLocal<String> THREAD_LOCAL_ORDER_NO = new ThreadLocal<>();
    private final LogControllerService logControllerService;
    private final LogScheduler logScheduler;
    private final GravitonLogSpEL gravitonLogSpEl;
    @Autowired
    private ApplicationContext context;

    /**
     * 切入点
     */
    @Pointcut("@annotation(io.github.cgglyle.boson.graviton.annotaion.GravitonLog)")
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
        Stack<LogInfo> logInfoStack = THREAD_LOCAL_STACK_LOG_INFO.get();
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
        Stack<LogInfo> logInfoStack = THREAD_LOCAL_STACK_LOG_INFO.get();
        // 判断栈中是否有信息，没有信息代表是第一次进入，添加一个OrderNo
        if (logInfoStack.size() == 0) {
            EnableGravitonOrderNo annotation = AnnotationUtils.findAnnotation(proceedingJoinPoint.getSignature().getDeclaringType(), EnableGravitonOrderNo.class);
            if (annotation != null | gravitonLog.enableOrderNo() && StringUtils.hasText(gravitonLog.orderNo())) {
                try {
                    THREAD_LOCAL_ORDER_NO.set(gravitonLogSpEl.parser(proceedingJoinPoint, gravitonLog.orderNo()
                            , null, null, String.class));
                } catch (Exception e) {
                    log.error("OrderNo SpEL Error " + e.getMessage(), e);
                }
            } else if (annotation != null | gravitonLog.enableOrderNo()) {
                OrderNoGenerate bean = context.getBean(gravitonLog.orderNoClass());
                THREAD_LOCAL_ORDER_NO.set(bean.getOrderNo());
            }
        }
        logInfoStack.push(new LogInfo());
        LogInfo logInfo = logInfoStack.peek();
        logInfo.setOrderNo(THREAD_LOCAL_ORDER_NO.get());
        logInfo.setStartTime(LocalDateTime.now());
        return proceedingJoinPoint.proceed();
    }

    /**
     * 成功后信息处理
     */
    @AfterReturning(value = "unityLogCut()", returning = "body")
    public void doAfterReturning(Object body) {
        Stack<LogInfo> logInfoStack = THREAD_LOCAL_STACK_LOG_INFO.get();
        LogInfo logInfo = logInfoStack.peek();
        logControllerService.postprocessing(body, logInfo);
    }

    /**
     * 异常后置
     */
    @AfterThrowing(value = "unityLogCut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        Stack<LogInfo> logInfoStack = THREAD_LOCAL_STACK_LOG_INFO.get();
        LogInfo logInfo = logInfoStack.peek();
        logControllerService.exceptionProcessing(throwable, logInfo);
    }

    /**
     * 切入后信息处理，固有
     */
    @After(value = "unityLogCut()")
    public void doAfter() {
        Stack<LogInfo> logInfoStack = THREAD_LOCAL_STACK_LOG_INFO.get();
        LogInfo logInfo = logInfoStack.pop();
        logInfo.setEndTime(LocalDateTime.now());
        logInfo.setConsumeTime(Duration.between(logInfo.getStartTime(), logInfo.getEndTime()).toMillis());
        logScheduler.startPrintf(logInfo);
        // 如果栈中为空，就移除相关线程的所有信息
        if (logInfoStack.size() == 0) {
            THREAD_LOCAL_ORDER_NO.remove();
            THREAD_LOCAL_STACK_LOG_INFO.remove();
        }
    }
}
