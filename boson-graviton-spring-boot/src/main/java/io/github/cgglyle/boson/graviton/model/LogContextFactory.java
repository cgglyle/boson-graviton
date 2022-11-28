package io.github.cgglyle.boson.graviton.model;

/**
 * 日志山下文工厂
 *
 * @author Lyle
 * @since 2022/11/27
 */
public class LogContextFactory {

    public static LogContext getContext(){
        LogContext logContext = new LogContext();
        logContext.setLogInfo(new LogInfo());
        logContext.setBusinessLogTemplate(new BusinessLogTemplate());
        logContext.setSystemLogTemplate(new SystemLogTemplate());
        return logContext;
    }
}
