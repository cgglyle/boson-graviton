package io.github.cgglyle.boson.graviton.service.mq;

import io.github.cgglyle.boson.graviton.model.LogInfo;
import io.github.cgglyle.boson.graviton.service.TemplateInterpreter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Lyle
 * @since 2022/11/20
 */
@Component
@AllArgsConstructor
public class LogPrintfConsume extends AbstractLogPrintfConsume<LogInfo>{
    private final TemplateInterpreter templateInterpreter;

    @Override
    protected void consume(LogInfo logInfo) {
        if (logInfo.isEnableSystem()) {
            String info;
            if (logInfo.isEnableOrderNo()) {
                info = logInfo.getOrderNo() + templateInterpreter.interpreter(logInfo);
            } else {
                info = templateInterpreter.interpreter(logInfo);
            }
            // 判断执行状态，成功还是失败
            if (logInfo.isStatus()) {
                PrintfUtils.printf(logInfo.getSystemSuccessLogLevel(), info);
            } else {
                PrintfUtils.printf(logInfo.getSystemErrorLogLevel(), info);
            }
        }
    }
}
