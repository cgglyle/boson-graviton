package io.github.cgglyle.boson.graviton.boot.model;

import io.github.cgglyle.boson.graviton.boot.exception.IllegalParameterException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Lyle Liu
 */
@Slf4j
public class LogContext {
    protected final UUID traceId;
    protected final Deque<LogInfo> logInfoDeque = new ArrayDeque<>();
    protected final AtomicLong callSequence = new AtomicLong();
    protected final Deque<String> printfInfo = new ArrayDeque<>();
    protected final boolean isPolymerization;
    protected boolean isAllNormal;

    public LogContext(boolean isPolymerization) {
        this.traceId = UUID.randomUUID();
        this.isPolymerization = isPolymerization;
    }

    public void initLog() {
        logInfoDeque.push(new LogInfo());
    }

    public void addLogJoinPoint(JoinPoint joinPoint) {
        final LogInfo loginfo = this.getLoginfo();
        loginfo.setJoinPoint(joinPoint);
    }

    public void addMethodResult(Object result) {
        final LogInfo loginfo = this.getLoginfo();
        this.isAllNormal = true;
        loginfo.setMethodResult(result);
    }

    public void addThrowing(Throwable throwable) {
        final LogInfo loginfo = this.getLoginfo();
        this.isAllNormal = false;
        loginfo.setThrowable(throwable);
    }

    public void methodStop() {
        final LogInfo loginfo = this.getLoginfo();
        loginfo.methodStop();
    }

    @Async
    public void printLogInfo() {
        final LogInfo logInfo = logInfoDeque.pop();
        if (logInfo == null) {
            throw new IllegalParameterException("Add method stop but deque log info is null");
        }
        final String printLogInfoString = logInfo.getLogInfoString();
        this.printfInfo.push(printLogInfoString);
        if (isPolymerization) {
            if (logInfoDeque.isEmpty()) {
                final String collect = String.join("\n => ", this.printfInfo);
                if (this.isAllNormal) {
                    log.info("[{}]\n => {}", this.traceId, collect);
                } else {
                    log.warn("[{}]\n => {}", this.traceId, collect);
                }

            }
        } else {
            if (Boolean.TRUE.equals(logInfo.isNormal())) {
                log.info("[{}] [{}] {}", this.callSequence.decrementAndGet(), this.traceId, printLogInfoString);
            } else {
                log.warn("[{}] [{}] {}", this.callSequence.decrementAndGet(), this.traceId, printLogInfoString);
            }
        }
    }

    public boolean isEmpty() {
        return this.logInfoDeque.isEmpty();
    }

    protected LogInfo getLoginfo(){
        Optional<LogInfo> logInfo = Optional.ofNullable(logInfoDeque.peek());
        return logInfo.orElseThrow(() -> new IllegalParameterException("log info is null"));
    }
}
