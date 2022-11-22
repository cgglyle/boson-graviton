package io.github.cgglyle.boson.graviton.service.mq;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.List;

/**
 * @author Lyle
 * @since 2022/11/20
 */
public class DisruptorQueue<T> {
    private final Disruptor<LogEvent<T>> disruptor;
    private final RingBuffer<LogEvent<T>> ringBuffer;

    public DisruptorQueue(Disruptor<LogEvent<T>> disrupt){
        this.disruptor = disrupt;
        this.ringBuffer = disrupt.getRingBuffer();
        this.disruptor.start();
    }

    /**
     * 发布一个事件
     *
     * @param event 事件载体，如果为空将会直接无视。
     */
    public void publish(T event){
        if (event == null){
            return;
        }
        long sequence = this.ringBuffer.next();
        try {
            LogEvent<T> logEvent = this.ringBuffer.get(sequence);
            logEvent.setObject(event);
        } finally {
            this.ringBuffer.publish(sequence);
        }
    }

    /**
     * 发布一个队列事件
     *
     * @param eventList 事件载体列表，如果为空将会直接无视。
     */
    public void publishAll(List<T> eventList){
        if (eventList == null){
            return;
        }
        eventList.forEach(this::publish);
    }

    public long cursor(){
        return this.disruptor.getRingBuffer().getCursor();
    }

    public void shutdown(){
        this.disruptor.shutdown();
    }
}
