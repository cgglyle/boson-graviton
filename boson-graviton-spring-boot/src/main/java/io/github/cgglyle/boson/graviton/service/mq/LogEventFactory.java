package io.github.cgglyle.boson.graviton.service.mq;

import com.lmax.disruptor.EventFactory;

/**
 * 日志载体工厂
 *
 * @author Lyle
 * @since 2022/11/20
 */
public class LogEventFactory<T> implements EventFactory<LogEvent<T>> {
    @Override
    public LogEvent<T> newInstance() {
        return new LogEvent<T>();
    }
}
