package io.github.cgglyle.boson.graviton.service.printf;

import io.github.cgglyle.boson.graviton.model.LogContext;
import io.github.cgglyle.boson.graviton.model.LogInfo;
import io.github.cgglyle.boson.graviton.model.LogTemplate;
import io.github.cgglyle.boson.graviton.service.mq.AbstractLogPrintfConsume;
import lombok.RequiredArgsConstructor;

/**
 * @author Lyle
 * @since 2022/11/20
 */
@RequiredArgsConstructor
public class SystemLogPrintfConsume extends AbstractLogPrintfConsume<LogContext> {
    private final TemplateInterpreter templateInterpreter;

    @Override
    protected void consume(LogContext logContext) {
        if (logContext.isEnableSystem()) {
            LogInfo logInfo = logContext.getLogInfo();
            LogTemplate systemLogTemplate = logContext.getSystemLogTemplate();
            String info;
            if (logContext.isEnableOrderNo()) {
                info = logInfo.getOrderNo() + templateInterpreter.interpreter(logContext);
            } else {
                info = templateInterpreter.interpreter(logContext);
            }
            // 判断执行状态，成功还是失败
            if (logContext.isStatus()) {
                PrintfUtils.printf(systemLogTemplate.getSuccessLogLevel(), info);
            } else {
                PrintfUtils.printf(systemLogTemplate.getFailureLogLevel(), info);
            }
        }
    }
}
