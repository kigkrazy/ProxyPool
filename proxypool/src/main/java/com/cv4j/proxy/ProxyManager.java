package com.cv4j.proxy;

import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.http.HttpManager;
import com.cv4j.proxy.task.ProxyPageCallable;
import com.safframework.tony.common.utils.Preconditions;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by tony on 2017/10/25.
 */
@Slf4j
public class ProxyManager {

    private ProxyManager() {
    }

    public static ProxyManager get() {
        return ProxyManager.Holder.MANAGER;
    }

    private static class Holder {
        private static final ProxyManager MANAGER = new ProxyManager();
    }

    /**
     * 抓取代理，成功的代理存放到ProxyPool中
     */
    public void start() {

        Flowable.fromIterable(ProxyPool.proxyMap.keySet())
                .parallel(ProxyPool.proxyMap.size())
                .map(s -> new ProxyPageCallable(s).call())
                .flatMap((Function<List<Proxy>, Publisher<Proxy>>) proxies -> {
                    if (Preconditions.isNotBlank(proxies)) {
                        List<Proxy> result = proxies
                                .stream()
                                .parallel()
                                .filter(proxy -> {
                                    HttpHost httpHost = new HttpHost(proxy.getIp(), proxy.getPort(), proxy.getType());
                                    boolean result1 = HttpManager.get().checkProxy(httpHost);
                                    if(result1) log.info("checkProxy " + proxy.getProxyStr() +", "+ result1);
                                    return result1;
                                }).collect(Collectors.toList());

                        return Flowable.fromIterable(result);
                    }
                    return Flowable.empty();
                })
                .runOn(Schedulers.io())
                .sequential()
                .subscribe(proxy -> {
                    if (proxy!=null) {
                        log.info("accept " + proxy.getProxyStr());
                        proxy.setLastSuccessfulTime(new Date().getTime());
                        ProxyPool.proxyList.add(proxy);
                    }
                }, throwable -> log.error("ProxyManager is error: "+throwable.getMessage()));
    }
}
