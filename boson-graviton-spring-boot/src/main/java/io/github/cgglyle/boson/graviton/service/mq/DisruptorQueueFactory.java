package io.github.cgglyle.boson.graviton.service.mq;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * Disruptor 工厂
 *
 * @author Lyle
 * @since 2022/11/20
 */
public class DisruptorQueueFactory {

    /**
     * 得到一个消息队列
     *
     * @param queueSize      队列环大小
     * @param isMoreProducer 是否是多个事件发布到同一个环中
     * @param consumers      消费者
     * @param <T>            具体事件
     * @return 一个经过配置的消息队列
     */
    public static <T> DisruptorQueue<T> getWorkPoolQueue(int queueSize, boolean isMoreProducer,
                                                         AbstractLogPrintfConsume<T>... consumers) {
        Disruptor<LogEvent<T>> disruptor = new Disruptor<>(new LogEventFactory<>(), queueSize,
                Executors.defaultThreadFactory(), isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new SleepingWaitStrategy());
        disruptor.handleEventsWithWorkerPool(consumers);
        return new DisruptorQueue<>(disruptor);
    }

    /**
     * 得到一个发布订阅模式的消息队列
     *
     * @param queueSize      队列环大小
     * @param isMoreProducer 是否是多个事件发布到同一个环中
     * @param consumers      消费者
     * @param <T>            具体事件
     * @return 一个经过配置的消息队列
     */
    public static <T> DisruptorQueue<T> getHandleEventsQueue(int queueSize, boolean isMoreProducer,
                                                         AbstractLogPrintfConsume<T>... consumers) {
        Disruptor<LogEvent<T>> disruptor = new Disruptor<>(new LogEventFactory<>(), queueSize,
                Executors.defaultThreadFactory(), isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new SleepingWaitStrategy());
        disruptor.handleEventsWith(consumers);
        return new DisruptorQueue<>(disruptor);
    }
}
