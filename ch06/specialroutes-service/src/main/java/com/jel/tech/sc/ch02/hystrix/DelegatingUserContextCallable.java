package com.jel.tech.sc.ch02.hystrix;

import com.jel.tech.sc.ch02.utils.UserContext;
import com.jel.tech.sc.ch02.utils.UserContextHolder;

import java.util.concurrent.Callable;

/**
 * @Author zhenhua.xu
 * @Date 2020/11/5 18:00
 * @Description
 **/
public final class DelegatingUserContextCallable<V> implements Callable<V> {

    private final Callable<V> delegate;
    private UserContext originalUserContext;

    public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {

        this.delegate = delegate;
        this.originalUserContext = userContext;
    }

    /**
     * The call() function is invoked before the method protected by the @HystrixCommand annotation
     * @return
     * @throws Exception
     */
    @Override
    public V call() throws Exception {

        // 设置上下文到子线程中（hystrix分配的）
        UserContextHolder.setContext(originalUserContext);

        try {
            return delegate.call();
        } finally {
            this.originalUserContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delegate,
                                         UserContext userContext) {

        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }
}
