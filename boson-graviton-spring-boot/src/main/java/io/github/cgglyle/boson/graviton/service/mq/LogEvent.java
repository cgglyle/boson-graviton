package io.github.cgglyle.boson.graviton.service.mq;

import lombok.Data;

/**
 * 日志载体
 *
 * @author Lyle
 * @since 2022/11/20
 */
@Data
public class LogEvent<T> {
    private T object;
}
