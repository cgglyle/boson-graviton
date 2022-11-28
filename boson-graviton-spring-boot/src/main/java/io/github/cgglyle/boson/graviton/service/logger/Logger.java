package io.github.cgglyle.boson.graviton.service.logger;

import io.github.cgglyle.boson.graviton.annotaion.EnableGravitonOrderNo;
import io.github.cgglyle.boson.graviton.annotaion.GravitonLog;
import io.github.cgglyle.boson.graviton.api.GravitonLogInfoSpEL;
import io.github.cgglyle.boson.graviton.api.LogControllerService;
import io.github.cgglyle.boson.graviton.api.LogUserService;
import io.github.cgglyle.boson.graviton.model.LogContext;
import io.github.cgglyle.boson.graviton.model.LogInfo;
import io.github.cgglyle.boson.graviton.model.LogTemplate;
import io.github.cgglyle.boson.graviton.service.spel.GravitonLogContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 抽象记录器
 *
 * @author Lyle
 * @since 2022/11/27
 */
@RequiredArgsConstructor
public abstract class Logger implements LogControllerService {
    protected final LogUserService logUserService;
    protected final GravitonLogInfoSpEL logInfoSpEL;

    @Override
    public void preprocessing(JoinPoint joinPoint, GravitonLog gravitonLog, LogContext logContext) {
        this.systemLogTemplate(logContext,gravitonLog);
        this.businessLogTemplate(logContext,gravitonLog);
        logContext.setEnableSystem(gravitonLog.enableSystem());
        logContext.setEnableBusiness(gravitonLog.enableBusiness());
        logContext.setJoinPoint(joinPoint);

        LogInfo logInfo = logContext.getLogInfo();
        if (logUserService != null) {
            logInfo.setUsername(logUserService.getUsername());
        }
        logInfo.setClassName(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logInfo.setInParameter(Arrays.asList(joinPoint.getArgs()));

        EnableGravitonOrderNo annotation = AnnotationUtils.findAnnotation(joinPoint.getSignature().getDeclaringType(),
                EnableGravitonOrderNo.class);
        if (annotation != null | gravitonLog.enableOrderNo()) {
            logContext.setEnableOrderNo(true);
        } else {
            logContext.setBusinessLog(false);
        }
    }

    @Override
    public void postprocessing(Object body, LogContext logContext) {
        logContext.setStatus(true);
        LogInfo logInfo = logContext.getLogInfo();
        logInfo.setOutParameter(body);
//        if (StringUtils.hasText(logContext.getBusinessLogTemplate().getSuccessTemplate())) {
//            GravitonLogContext.putVariable(logContext);
//            logContext.setSpELFuture(logInfoSpEL.parser(logContext, Object.class));
//        }
    }

    @Override
    public void exceptionProcessing(Throwable throwable, LogContext logContext) {
        LogInfo logInfo = logContext.getLogInfo();
        logInfo.setException(throwable);
        logInfo.setErrorMsg(throwable.getMessage());
        logContext.setStatus(false);
//        if (StringUtils.hasText(logContext.getFailure())) {
//            GravitonLogContext.putVariable(logContext);
//            logContext.setSpELFuture(logInfoSpEL.parser(logContext, Object.class));
//        }
    }

    protected void systemLogTemplate(LogContext logContext, GravitonLog gravitonLog) {
        LogTemplate systemLogTemplate = logContext.getSystemLogTemplate();
        systemLogTemplate.setSuccessLogLevel(gravitonLog.systemSuccessLogLevel());
        systemLogTemplate.setFailureLogLevel(gravitonLog.systemFailureLogLevel());
        systemLogTemplate.setSuccessTemplate(gravitonLog.systemSuccessTemplate());
        systemLogTemplate.setFailureTemplate(gravitonLog.systemFailureTemplate());
        systemLogTemplate.setTimeFormat(gravitonLog.timeFormat());
        systemLogTemplate.setOrderNoTemplate(gravitonLog.orderNo());
    }

    protected void businessLogTemplate(LogContext logContext, GravitonLog gravitonLog) {
        LogTemplate businessLogTemplate = logContext.getBusinessLogTemplate();
        businessLogTemplate.setSuccessLogLevel(gravitonLog.businessSuccessLogLevel());
        businessLogTemplate.setFailureLogLevel(gravitonLog.businessFailureLogLevel());
        businessLogTemplate.setSuccessTemplate(gravitonLog.businessSuccessTemplate());
        businessLogTemplate.setFailureTemplate(gravitonLog.businessFailureTemplate());
        businessLogTemplate.setTimeFormat(gravitonLog.timeFormat());
        businessLogTemplate.setOrderNoTemplate(gravitonLog.orderNo());
    }
}
