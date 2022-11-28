package io.github.cgglyle.boson.graviton.model;

import lombok.Data;

/**
 * 日志信息模版
 *
 * @author Lyle
 * @since 2022/11/27
 */
@Data
public abstract class LogTemplate {
    private String successTemplate;
    private String failureTemplate;
    private LogLevelEnum failureLogLevel;
    private LogLevelEnum successLogLevel;
    private String orderNoTemplate;
    private String timeFormat;
}
