package io.github.cgglyle.boson.graviton.service.mq;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * 抽象日志打印消费类
 *
 * @author Lyle
 * @since 2022/11/20
 */
public abstract class AbstractLogPrintfConsume<T> implements EventHandler<LogEvent<T>>, WorkHandler<LogEvent<T>> {
    @Override
    public void onEvent(LogEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(LogEvent<T> event) throws Exception {
        this.consume(event.getObject());
    }

    /**
     * 消费函数
     *
     * @param event 事件
     */
    protected abstract void consume(T event);
}
