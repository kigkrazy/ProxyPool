package com.cv4j.proxy;

import com.cv4j.proxy.domain.Proxy;

import java.util.List;
import java.util.Map;

/**
 * 对外接口
 */
public class ProxyPoolUtil {
    private static long sMinInterval = 10 * 60 * 100;//扫描最少间隔，默认10分钟

    /**
     * 设置代理map
     *
     * @param proxyMap
     */
    public static void setProxyMap(Map<String, Class> proxyMap) {
        ProxyPool.proxyMap = proxyMap;
    }

    /**
     * 添加代理源
     *
     * @param host        请求地址
     * @param clazzParser 解析类
     */
    public static void addProxyMap(String host, Class clazzParser) {
        ProxyPool.proxyMap.put(host, clazzParser);
    }


    /**
     * 获取1个代理
     *
     * @return
     */
    public static Proxy getProxy() {
        return ProxyPool.getProxy();
    }


    /**
     * 获取指定个数代理
     *
     * @param count 个数
     * @return
     */
    public static List<Proxy> getProxy(int count) {
        return ProxyPool.getProxy(count);
    }

    /**
     * 设置扫描时间间隔，默认为10分钟
     *
     * @param interval
     */
    public static void setMinInterval(long interval) {
        ProxyPoolUtil.sMinInterval = interval;
    }


    /**
     * 清空代理池
     */
    public static void clearProxyList() {
        ProxyPool.clearProxyList();
    }

    /**
     * 开始扫描
     */
    public static void startScan() {
        ProxyManager.get().start();
    }
}
