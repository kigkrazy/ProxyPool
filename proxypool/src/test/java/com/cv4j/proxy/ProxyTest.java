package com.cv4j.proxy;

import com.cv4j.proxy.domain.Proxy;
import com.cv4j.proxy.site.XiaoHeXiaProxyListPageParser;
import org.junit.Test;

public class ProxyTest {
    @Test
    public void test() {
        ProxyPoolUtil.addProxyMap("http://www.xiaohexia.cn/", XiaoHeXiaProxyListPageParser.class);
        ProxyPoolUtil.startScan();
        Proxy pr1 = ProxyPoolUtil.getProxy();
    }
}
