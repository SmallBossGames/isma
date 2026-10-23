package ru.nstu.isma.intg.server.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.server.models.Message;
import ru.nstu.isma.intg.server.models.MessageType;

public class MessageService {

    private static final int MESSAGE_CYCLE_DELAY = 100;
    private static final TimeUnit MESSAGE_CYCLE_DELAY_UNIT = TimeUnit.MICROSECONDS;

    private final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final MpiService mpiService;

    private final Multimap<MessageType, Consumer<Message>> messageListeners =
            Multimaps.synchronizedMultimap(HashMultimap.create());
    private ScheduledExecutorService messagePumpExecutor;
    private final ExecutorService messageDispatcherExecutor = Executors.newSingleThreadExecutor();

    public MessageService(MpiService mpiService) {
        this.mpiService = mpiService;
    }

    public void addMessageListener(MessageType type, Consumer<Message> listener) {
        synchronized (messageListeners) {
            messageListeners.put(type, listener);
        }
    }

    public void setMessageListener(MessageType type, Consumer<Message> listener) {
        synchronized (messageListeners) {
            messageListeners.removeAll(type);
            messageListeners.put(type, listener);
        }
    }

    public void startMessagePump() {
        if (messagePumpExecutor == null) {
            int rank = mpiService.getRank();

            ThreadFactory pumpThreadFactory = new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setNameFormat("rank" + rank + "-msgpump")
                    .setUncaughtExceptionHandler((t, e) -> {
                        logger.error("Uncaught exception in thread " + t.getName(), e);
                        startMessagePump();
                    })
                    .build();
            this.messagePumpExecutor = Executors.newSingleThreadScheduledExecutor(pumpThreadFactory);
        }

        messagePumpExecutor.scheduleWithFixedDelay(
                this::pumpMpiMessages, 0, MESSAGE_CYCLE_DELAY, MESSAGE_CYCLE_DELAY_UNIT);

        logger.debug("Started a message pump ({})", Thread.currentThread().getName());
    }

    private void pumpMpiMessages() {
        Message message = mpiService.receiveMessageFromMaster();
        while (message != null) {
            logger.debug("Received a message {}", message.getType());
            dispatchMessage(message);
            message = mpiService.receiveMessageFromMaster();
        }
    }

    private void dispatchMessage(Message message) {
        messageDispatcherExecutor.execute(() -> {
            logger.debug("Dispatch {} message to listeners...", message.getType());
            Collection<Consumer<Message>> listeners = new ArrayList<>(messageListeners.get(message.getType()));
            for (Consumer<Message> listener : listeners) {
                notify(listener, message);
            }
        });
    }

    private void notify(Consumer<Message> listener, Message message) {
        try {
            logger.debug("Invoking a listener of message {} in rank #{}...", message.getType(), mpiService.getRank());
            listener.accept(message);
            logger.debug("Message {} is processed by rank #{}.", message.getType(), mpiService.getRank());
        } catch (Exception ex) {
            logger.error("Failed on running listeners while processing ClientRequest ", ex);
        }

    }

}
