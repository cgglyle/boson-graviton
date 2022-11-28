package io.github.cgglyle.boson.graviton.model;

import lombok.Data;
import org.aspectj.lang.JoinPoint;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * 需要打印的日志
 *
 * @author Lyle
 * @since 2022/11/27
 */
@Data
public class LogInfo {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long consumeTime;
    private String url;
    private String uri;
    private String ip;
    private String className;
    private Object inParameter;
    private Object outParameter;
    private Throwable exception;
    private String orderNo;
    private String username;
    private String errorMsg;
}
