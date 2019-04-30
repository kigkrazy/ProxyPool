package com.cv4j.proxy;

import cn.hutool.core.util.RandomUtil;
import com.cv4j.proxy.domain.Proxy;
import com.safframework.tony.common.utils.Preconditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 代理池
 * Created by tony on 2017/10/19.
 */
public class ProxyPool {

    /**
     * CopyOnWriteArrayList 线程安全,支持一边写，一边读。
     */
    public static CopyOnWriteArrayList<Proxy> proxyList = new CopyOnWriteArrayList<>();
    public static Map<String, Class> proxyMap = new HashMap<>();

    /**
     * 采用round robin算法来获取Proxy
     *
     * @return
     */
    public static Proxy getProxy() {
        Proxy proxy = RandomUtil.randomEle(proxyList);
        return proxy;
    }

    public static List<Proxy> getProxy(int count) {
        List<Proxy> proxies = RandomUtil.randomEles(proxyList, count);
        proxyList.removeAll(proxies);
        return proxies;
    }


    public static void addProxy(Proxy proxy) {
        if (Preconditions.isNotBlank(proxy)) {
            proxyList.add(proxy);
        }
    }

    public static void addProxyList(List<Proxy> proxies) {
        if (Preconditions.isNotBlank(proxies)) {
            proxyList.addAll(proxies);
        }
    }

    public static void clearProxyList() {
        proxyList.clear();
    }
}
