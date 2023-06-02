package io.github.cgglyle.boson.graviton.boot.model;

import io.github.cgglyle.boson.graviton.boot.exception.IllegalParameterException;
import io.github.cgglyle.boson.graviton.boot.exception.InputArgsException;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Lyle Liu
 */
@Slf4j
public class LogInfo {
    private JoinPoint joinPoint;
    private final Long startAt = System.currentTimeMillis();
    private Long stopAt;
    private String calledMethod;
    private List<String> inParameter;
    private Object outParameter;
    private Throwable throwable;
    @Getter
    private boolean isNormal;

    public LogInfo() {
    }

    public void setJoinPoint(JoinPoint joinPoint) {
        if (this.joinPoint == null) {
            this.joinPoint = joinPoint;
        } else {
            throw new IllegalParameterException(
                    String.format("Join point is not null: {%s}, it is forbidden to change: {%s}",
                            this.joinPoint, joinPoint)
            );
        }
    }

    public void setMethodResult(Object outParameter) {
        if (this.outParameter == null) {
            this.outParameter = outParameter;
            this.isNormal = Boolean.TRUE;
        } else {
            throw new IllegalParameterException(
                    String.format("Method result is not null: {%s}, it is forbidden to change: {%s}",
                            this.outParameter, outParameter)
            );
        }
    }

    public void setThrowable(Throwable throwable) {
        if (this.throwable == null) {
            this.throwable = throwable;
            this.isNormal = Boolean.FALSE;
        } else {
            throw new IllegalParameterException(
                    String.format("Method result is not null: {%s}, it is forbidden to change: {%s}",
                            this.throwable, throwable)
            );
        }
    }

    public void methodStop() {
        this.stopAt = System.currentTimeMillis();
    }

    public String getLogPrintInfoString() {
        if (isNormal) {
            return String.format("| SUCCESS | Start at %s | Stop at %s | Time consuming %sms | Called method => %s | Input args %s | Output result %s",
                    this.getStartAt(),
                    this.getStopAt(),
                    this.getTimeConsuming(),
                    this.calledMethod,
                    this.inParameter,
                    this.outParameter
            );
        } else {
            return String.format("| ERROR | Start at %s | Stop at %s | Time consuming %sms | Called method => %s | Input args %s | Exception cause %s",
                    this.getStartAt(),
                    this.getStopAt(),
                    this.getTimeConsuming(),
                    this.calledMethod,
                    this.inParameter,
                    this.throwable.getMessage()
            );
        }
    }


    private LocalDateTime toLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }

    protected void initLogInfo() {
        this.calledMethod = this.getCalledMethod(this.joinPoint);
        this.inParameter = this.getInputArgs(this.joinPoint);
    }

    public String getLogInfoString() {
        this.initLogInfo();
        return this.getLogPrintInfoString();
    }

    private String getCalledMethod(@Nonnull final JoinPoint joinPoint) {
        final String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        final String name = joinPoint.getSignature().getName();
        return declaringTypeName + "#" + name;
    }

    private List<String> getInputArgs(@Nonnull final JoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        final List<String> parameter;
        if (joinPoint.getSignature() instanceof MethodSignature methodSignature) {
            final String[] parameterNames = methodSignature.getParameterNames();
            if (args.length != parameterNames.length) {
                throw new InputArgsException("Number of arguments not equal to number of values!");
            }
            parameter = new ArrayList<>();
            for (int i = 0; i < parameterNames.length; i++) {
                parameter.add(parameterNames[i] + "=" + args[i]);
            }
        } else {
            parameter = null;
        }
        return parameter;
    }

    private LocalDateTime getStopAt() {
        return this.toLocalDateTime(this.stopAt);
    }

    private Long getTimeConsuming() {
        return this.stopAt - this.startAt;
    }

    private LocalDateTime getStartAt() {
        return this.toLocalDateTime(this.startAt);
    }
}
