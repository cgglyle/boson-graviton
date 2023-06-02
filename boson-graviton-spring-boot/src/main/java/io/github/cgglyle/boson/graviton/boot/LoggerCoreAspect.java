package io.github.cgglyle.boson.graviton.boot;

import io.github.cgglyle.boson.graviton.boot.model.LogContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Logger aspect
 *
 * <p> When class
 *
 * @author Lyle Liu
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggerCoreAspect {
    private static final ThreadLocal<LogContext> LOG_CONTEXT_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> new LogContext(true));

    /**
     * Point cut
     *
     * <p>Use {@link EnableLogger} enable logger
     */
    @Pointcut("@within(EnableLogger) || @annotation(EnableLogger)")
    public void methodCut() {
    }

    @Before(value = "methodCut()")
    public void unityLog(final JoinPoint joinPoint) {
        final LogContext logContext = LOG_CONTEXT_THREAD_LOCAL.get();
        logContext.addLogJoinPoint(joinPoint);
    }

    @Around(value = "methodCut()")
    public Object doAround(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final LogContext logContext = LOG_CONTEXT_THREAD_LOCAL.get();
        logContext.initLog();
        return proceedingJoinPoint.proceed();
    }

    @AfterReturning(value = "methodCut()", returning = "body")
    public void doAfterReturning(Object body) {
        final LogContext logContext = LOG_CONTEXT_THREAD_LOCAL.get();
        logContext.addMethodResult(body);
    }

    @AfterThrowing(value = "methodCut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        final LogContext logContext = LOG_CONTEXT_THREAD_LOCAL.get();
        logContext.addThrowing(throwable);
    }

    @After(value = "methodCut()")
    public void doAfter() {
        final LogContext logContext = LOG_CONTEXT_THREAD_LOCAL.get();
        logContext.methodStop();
        logContext.printLogInfo();
        if (logContext.isEmpty()) {
            LOG_CONTEXT_THREAD_LOCAL.remove();
        }
    }
}
