package cc.catman.coder.workbench.core.message;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * 分组消息队列,用于将消息按照分组进行处理
 * <p>
 * 每个分组对应一个消息队列,从而确保分组内的消息是有序的,当然也可以共用一个队列
 * <p>
 * 同时通过分组的方式,可以确保不同分组的消息是并行处理的,提高处理效率
 *
 * @param <T> 消息
 */
@Slf4j
public class GroupedMessageQueue<T> {
    /**
     * key:分组标识
     * value:该分组对应的消息队列
     */
    private final Map<String, BlockingQueue<T>> queueMap = new ConcurrentHashMap<>();
    private final Map<String,Lock> queueCreateOrRemoveLockMap = new ConcurrentHashMap<>();
    /**
     * key:分组标识
     * value:该分组对应的锁
     */
    private final Map<String, Lock> lockMap = new ConcurrentHashMap<>();
    /**
     * key:分组标识
     * value:该分组对应的消费者
     */
    private final Map<String, Consumer<T>> futureMap = new ConcurrentHashMap<>();
    /**
     * 线程池
     */
    private final ExecutorService executorService;

    /**
     * 消息队列消费者创建器,一个函数式接口
     */
    private final MessageQueueConsumerCreator<T> messageQueueConsumerCreator;

    private final Object lock = new Object();

    /**
     * 创建分组消息队列
     *
     * @param messageQueueConsumerCreator 消息队列消费者创建器
     * @param <T>                         消息类型
     * @return 分组消息队列
     */
    public static <T> GroupedMessageQueue<T> create(MessageQueueConsumerCreator<T> messageQueueConsumerCreator) {
        return new GroupedMessageQueue<T>(messageQueueConsumerCreator);
    }

    public static <T> GroupedMessageQueue<T> create(MessageQueueConsumerCreator<T> messageQueueConsumerCreator, ExecutorService executorService) {
        return new GroupedMessageQueue<T>(messageQueueConsumerCreator, executorService);
    }

    public GroupedMessageQueue(MessageQueueConsumerCreator<T> messageQueueConsumerCreator) {
        this(messageQueueConsumerCreator, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));
    }

    public GroupedMessageQueue(MessageQueueConsumerCreator<T> messageQueueConsumerCreator, ExecutorService executorService) {
        this.messageQueueConsumerCreator = messageQueueConsumerCreator;
        this.executorService = executorService;
        start();
    }

    public void start(){
        // 尝试执行任务
       while (!this.queueMap.isEmpty()){
           // 调度任务
              for (String key : queueMap.keySet()) {
                scheduleConsumer(key);
              }
       }
       synchronized (this.lock){
           try {
               this.lock.wait();
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
           }
       }
       start();
    }

    /**
     * 添加消息到分组消息队列
     *
     * @param key     分组标识
     * @param message 消息
     */
    public void put(String key, T message) {
        queueMap.compute(key, (k, v) -> {
            if (v == null) {
                log.info("Create new queue for key: {}", key);
                v = new ArrayBlockingQueue<>(10);
            }
            log.info("Adding message to queue for key: {},{}", key, message);
            v.add(message);
            return v;
        });
        // 为该分组调度消费者线程
        scheduleConsumer(key);
    }

    /**
     * 为消息分组调度消费者线程,确保每个分组的消息都能被消费
     *
     * @param key 分组标识
     */
    private void scheduleConsumer(String key) {
        // 此处通过锁和futureMap确保每个分组只有一个消费者线程
        Consumer<T> consumer = futureMap.compute(key, (k, v) -> {
            if (v != null) {
                return v;
            }
            return messageQueueConsumerCreator.create(key, queueMap.get(key).peek(), GroupedMessageQueue.this);
        });
        consume(key, consumer);
    }

    /**
     * 消费消息
     *
     * @param key      分组标识
     * @param consumer 消费者
     */
    @SneakyThrows
    public void consume(String key, Consumer<T> consumer) {
        // 尝试读取或创建指定分组对应的消息队列
        BlockingQueue<T> queue = queueMap.computeIfAbsent(key, k -> new ArrayBlockingQueue<>(10));
        // 尝试读取或创建指定分组对应的锁
        Lock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        // 尝试获取锁,并执行消费任务
        if (lock.tryLock(3000, TimeUnit.MILLISECONDS)) {
            // 获取锁成功,执行消费任务
            this.executorService.submit(() -> {
                try {
                    while (!queue.isEmpty()) {
                        T message = queue.poll();
                        if (message != null) {
                            consumer.accept(message);
                        }
                    }
                } finally {
//                    queueMap.remove(key);
                    lockMap.remove(key);
                    futureMap.remove(key);
                    lock.unlock();
                    // Check if there are more messages; if so, schedule another consumer
                    if (!queue.isEmpty()) {
                        scheduleConsumer(key);
                    }
                }
            });
        } else {
            // 获取锁失败,重新调度消费者线程
            System.out.println("获取锁失败,重新调度消费者线程");
            scheduleConsumer(key);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 此处创建的集合不适用线程安全的集合因此,没有使用ConcurrentHashMap和CopyOnWriteArrayList
        int groupCount = 100;

        Map<String, List<Integer>> messageMap = new ConcurrentHashMap<>(groupCount);
        for (int i = 0; i < groupCount; i++) {
            messageMap.put(String.valueOf(i), new CopyOnWriteArrayList<>());
        }
        // 创建分组消息队列
        GroupedMessageQueue<Integer> messageQueue = new GroupedMessageQueue<>((key, message, groupedMessageQueue) -> (m) -> {
            messageMap.get(key).add(message);
//            System.out.println("Consuming message for key: " + key + "," + message);
        });
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 多线程生产消息,此处会产生100个分组,每个分组100条消息
        for (int i = 0; i < groupCount; i++) {
            String key = String.valueOf(i);
            executorService.submit(new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    messageQueue.put(key, j);
                }
            }));
        }
        Thread.sleep(3000);
        for (int i = 0; i < groupCount; i++) {
            List<Integer> messages = messageMap.get(String.valueOf(i));
            if (messages.size() != 100) {
                throw new IllegalStateException("Messages not consumed for group " + i + ",size:" + messages.size());
            }
        }
    }
}
