package cc.catman.coder.workbench.core.message;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Slf4j
public class GroupedMessageQueue<T> {
    private final Map<String, BlockingQueue<T>> queueMap = new ConcurrentHashMap<>();
    private final Map<String, Lock> lockMap = new ConcurrentHashMap<>();
    private final Map<String, Future<?>> futureMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService;

    private final MessageQueueConsumerCreator<T> messageQueueConsumerCreator;


    public static <T> GroupedMessageQueue<T> create(MessageQueueConsumerCreator<T> messageQueueConsumerCreator){
        return new GroupedMessageQueue<T>(messageQueueConsumerCreator);
    }

    public static <T> GroupedMessageQueue<T> create(MessageQueueConsumerCreator<T> messageQueueConsumerCreator,ExecutorService executorService){
        return new GroupedMessageQueue<T>(messageQueueConsumerCreator,executorService);
    }

    public GroupedMessageQueue(MessageQueueConsumerCreator<T> messageQueueConsumerCreator) {
      this(messageQueueConsumerCreator,Executors.newCachedThreadPool());
    }

    public GroupedMessageQueue(MessageQueueConsumerCreator<T> messageQueueConsumerCreator,ExecutorService executorService) {
        this.messageQueueConsumerCreator = messageQueueConsumerCreator;
        this.executorService= executorService;
    }

    public void put(String key, T message) {
        queueMap.compute(key, (k, v) -> {
            if (v == null) {
                log.info("Create new queue for key: {}", key);
                v = new LinkedBlockingQueue<>();
            }
            log.info("Adding message to queue for key: {},{}", key, message);
            v.add(message);
            return v;
        });

        scheduleConsumer(key);
    }

    private void scheduleConsumer(String key) {
        futureMap.compute(key, (k, v) -> {
            if (v != null && !v.isDone()) {
                return v;
            }
            return  executorService.submit(new Thread(() -> {
                consume(key, messageQueueConsumerCreator.create(key,queueMap.get(key).peek(),this));
            }));
        });
    }

    public void consume(String key,Consumer<T> consumer) {
        BlockingQueue<T> queue = queueMap.computeIfAbsent(key, k -> new LinkedBlockingQueue<>());
        Lock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        if (lock.tryLock()){
            try {
                while (!queue.isEmpty()) {
                    T message = queue.poll();
                    if (message != null) {
                        consumer.accept(message);
                    }
                }
            } finally {
                lock.unlock();
                // Check if there are more messages; if so, schedule another consumer
                if (!queue.isEmpty()) {
                    scheduleConsumer(key);
                } else {
                    // No more messages, remove from futureMap
                    futureMap.remove(key);
                }
            }
        }
    }

    public void take(Consumer<T> consumer) {
        for (Map.Entry<String, BlockingQueue<T>> entry : queueMap.entrySet()) {
            String groupKey = entry.getKey();
            consume(groupKey,consumer);
        }
    }

    public static void main(String[] args) {
        GroupedMessageQueue<String> messageQueue = new GroupedMessageQueue<>((key, message, groupedMessageQueue) -> (m) -> {
            System.out.println(Thread.currentThread().getId()+"Consumed: " + m);
        });
        ExecutorService executorService = Executors.newCachedThreadPool();

        // Producer thread
            for (int i = 0; i <= 100; i++) {
                String groupKey = "Group"+i;
                executorService.submit(new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        String message = "Message " + j;
                        messageQueue.put(groupKey, message);
                        System.out.println("Produced: " + message);
                    }
                }));
            }
    }
}
