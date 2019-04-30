package com.cv4j.proxy;

import com.cv4j.proxy.domain.Proxy;
import com.safframework.tony.common.utils.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 代理池
 * Created by tony on 2017/10/19.
 */
public class ProxyPool {

    /**
     * CopyOnWriteArrayList 线程安全,支持一边写，一边读。
     */
    public static BlockingQueue<Proxy> proxyList = new ArrayBlockingQueue<>(1000);
    public static Map<String, Class> proxyMap = new HashMap<>();

    /**
     * 获取代理，获取代理完了之后会顺便将代理踢出
     *
     * @return
     */
    public static Proxy getProxy() {
        return proxyList.poll();
    }

    public static List<Proxy> getProxy(int count) {
        List<Proxy> proxies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Proxy proxy = proxyList.poll();
            if (proxy == null)
                break;
            proxies.add(proxy);
        }
        return proxies;
    }


    public static void addProxy(Proxy proxy) {
        if (Preconditions.isNotBlank(proxy)) {
            proxyList.offer(proxy);
        }
    }

    public static void addProxyList(List<Proxy> proxies) {
        if (Preconditions.isNotBlank(proxies)) {
            for (Proxy proxy : proxies)
                proxyList.offer(proxy);
        }
    }

    public static void clearProxyList() {
        proxyList.clear();
    }
}
