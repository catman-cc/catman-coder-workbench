package cc.catman.coder.workbench.core.message.subscriber.manager;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageMatch;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.subscriber.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DefaultMessageSubscriberManager implements IMessageSubscriberManager {
    protected List<IMessageSubscriber> subscribers;

    protected List<IMessageSubscriber> noMatchMessageSubscriber;

    protected List<IMessageErrorSubscriber> exceptionMessageSubscriber;

    protected List<IMessageSubscribeWatcher> watchers;

    protected List<IMessageFilter> filters;

    protected IMessageSubscriberFilterFactory filterFactory;

    public DefaultMessageSubscriberManager() {
        this.subscribers=new ArrayList<>();
        this.noMatchMessageSubscriber=new ArrayList<>();
        this.exceptionMessageSubscriber=new ArrayList<>();
        this.watchers=new ArrayList<>();
        this.filters=new ArrayList<>();
        this.filterFactory=new DefaultMessageSubscriberFilterFactory();
        // 默认添加一次性订阅者观察者
        this.addWatcher(new OnceMessageSubscribeWatcher());

    }

    @Override
    public IMessageSubscriberFilterFactory getFilterFactory() {
        return filterFactory;
    }

    @Override
    public void addSubscriber(IMessageSubscriber subscriber) {
        subscribers.add(subscriber);
        this.watchers.forEach(watcher -> watcher.afterAddSubscriber(subscriber,this));
    }

    @Override
    public void removeSubscriber(IMessageSubscriber subscriber) {
        this.watchers.forEach(watcher -> watcher.beforeRemoveSubscriber(subscriber,this));
        subscribers.remove(subscriber);
    }

    @Override
    public List<IMessageSubscriber> list() {
        return subscribers;
    }

    @Override
    public List<IMessageSubscriber> list(Message<?> message) {
        List<IMessageSubscriberFilter> filters = this.filterFactory.createFilters(message);
        if(filters.isEmpty()){
           return this.list();
        }
        List<IMessageSubscriber> subscribers = new ArrayList<>(this.list());
        for (IMessageSubscriberFilter filter : filters) {
            subscribers =filter.filter(subscribers);
        }
        return subscribers;
    }

    @Override
    public List<IMessageFilter> filters() {
        return this.filters;
    }

    @Override
    public List<IMessageSubscriber> noMatchMessageSubscriber() {
        return noMatchMessageSubscriber;
    }

    @Override
    public List<IMessageErrorSubscriber> exceptionMessageSubscriber() {
        return exceptionMessageSubscriber;
    }

    @Override
    public void triggerWatchBefore(Message<?> message, IMessageSubscriber subscriber) {
        for (IMessageSubscribeWatcher watcher : watchers) {
            watcher.onWatchBefore(message, subscriber, this);
        }
    }

    @Override
    public void triggerWatchAfter(Message<?> message, IMessageSubscriber subscriber) {
        for (IMessageSubscribeWatcher watcher : watchers) {
            watcher.onWatchAfter(message, subscriber, this);
        }
    }

    @Override
    public void addWatcher(IMessageSubscribeWatcher watcher) {
        this.watchers.add(watcher);
        watcher.start();
    }

    @Override
    public void removeWatcher(IMessageSubscribeWatcher watcher) {
        this.watchers.remove(watcher);
        watcher.stop();
    }

    @Override
    public void add(IMessageFilter filter) {
        this.filters.add(filter);
    }

    @Override
    public IMessageSubscriber add(MessageMatch match, Function<Message<?>, MessageResult> func) {
        IMessageSubscriber subscriber = new IMessageSubscriber() {
            @Override
            public boolean isMatch(Message<?> message) {
                return match.match(message);
            }

            @Override
            public MessageResult onReceive(Message<?> message) {
                return func.apply(message);
            }

            @Override
            public void close() {
                DefaultMessageSubscriberManager.this.removeSubscriber(this);
            }
        };
        this.addSubscriber(subscriber);
        return subscriber;
    }

    @Override
    public void remove(IMessageFilter filter) {
        this.filters.remove(filter);
    }
}
