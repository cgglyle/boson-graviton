package io.github.cgglyle.boson.graviton.service.printf;

import io.github.cgglyle.boson.graviton.api.GravitonLogSpEL;
import io.github.cgglyle.boson.graviton.model.LogContext;
import io.github.cgglyle.boson.graviton.model.LogInfo;
import io.github.cgglyle.boson.graviton.model.LogTemplate;
import io.github.cgglyle.boson.graviton.service.mq.AbstractLogPrintfConsume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.concurrent.CompletableFuture;

/**
 * @author Lyle
 * @since 2022/11/21
 */
@Slf4j
@RequiredArgsConstructor
public class BusinessLogPrintfConsume extends AbstractLogPrintfConsume<LogContext> {
    private final GravitonLogSpEL gravitonLogSpEl;
    @Override
    protected void consume(LogContext logContext) {
        LogInfo logInfo = logContext.getLogInfo();
        LogTemplate businessLogTemplate = logContext.getBusinessLogTemplate();
        if (logContext.isStatus()) {
            try {
                Object parser = gravitonLogSpEl.parser(logContext.getJoinPoint(),
                        businessLogTemplate.getSuccessTemplate(), logInfo.getOutParameter(),
                        null, Object.class);
                logContext.setBusinessLog(parser);
            } catch (Exception e) {
                log.error("SpEL解析出现异常" + e.getMessage(), e);
            }
        } else {
            try {
                Object parser = gravitonLogSpEl.parser(logContext.getJoinPoint(),
                        businessLogTemplate.getFailureTemplate(), null,
                        logInfo.getErrorMsg(), Object.class);
                logContext.setBusinessLog(parser);
            } catch (Exception e) {
                log.error("SpEL解析出现异常" + e.getMessage(), e);
            }
        }
        // 判断是否启用了业务日志打印，同时成功状态或者失败状态下是否有相应的业务日志模版
        if (logContext.isEnableBusiness()
                && (StringUtils.hasText(businessLogTemplate.getSuccessTemplate()) && logContext.isStatus())
                || (StringUtils.hasText(businessLogTemplate.getFailureTemplate()) && !logContext.isStatus())) {
            String info;
            if (logContext.isEnableOrderNo()) {
                info = logInfo.getOrderNo() + logContext.getBusinessLog().toString();
            } else {
                info = logContext.getBusinessLog().toString();
            }
            // 判断执行结果，并判断结果时候为空
            if (logContext.isStatus() && businessLogTemplate.getSuccessTemplate() != null
                    && StringUtils.hasText(businessLogTemplate.getSuccessTemplate())) {
                PrintfUtils.printf(businessLogTemplate.getSuccessLogLevel(), info);
            } else if (!logContext.isStatus() && businessLogTemplate.getSuccessTemplate() != null
                    && StringUtils.hasText(businessLogTemplate.getSuccessTemplate())) {
                PrintfUtils.printf(businessLogTemplate.getFailureLogLevel(), info);
            }
        }
    }
}
