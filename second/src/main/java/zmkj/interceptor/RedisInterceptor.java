package zmkj.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.RenderInfo;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.render.Render;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RedisInterceptor implements Interceptor {
    private static final String renderKey = "_renderKey";
    private static ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap(512);

    public RedisInterceptor() {
    }

    private ReentrantLock getLock(String key) {
        ReentrantLock lock = (ReentrantLock)lockMap.get(key);
        if (lock != null) {
            return lock;
        } else {
            lock = new ReentrantLock();
            ReentrantLock previousLock = (ReentrantLock)lockMap.putIfAbsent(key, lock);
            return previousLock == null ? lock : previousLock;
        }
    }

    public final void intercept(Invocation inv) {
        Controller controller = inv.getController();
        String cacheName = this.buildCacheName(inv, controller);
        System.out.println("拦截器:"+cacheName);
        String cacheKey = this.buildCacheKey(inv, controller);
        System.out.println("拦截器:"+cacheKey);
        System.out.println("redis:"+Redis.use("bbs"));
        Map<String, Object> cacheData = Redis.use("bbs").hget(cacheName, cacheKey.substring(1));
        if (cacheData == null) {
            label49: {
                Lock lock = this.getLock(cacheName);
                lock.lock();

                try {
                    cacheData = Redis.use("bbs").hget(cacheName, cacheKey);
                    if (cacheData != null) {
                        break label49;
                    }

                    inv.invoke();
                    this.cacheAction(cacheName, cacheKey, controller);
                } finally {
                    lock.unlock();
                }

                return;
            }
        }

        this.useCacheDataAndRender(cacheData, controller);
    }

    protected String buildCacheName(Invocation inv, Controller controller) {
        CacheName cacheName = (CacheName)inv.getMethod().getAnnotation(CacheName.class);
        if (cacheName != null) {
            return cacheName.value();
        } else {
            cacheName = (CacheName)controller.getClass().getAnnotation(CacheName.class);
            return cacheName != null ? cacheName.value() : inv.getActionKey();
        }
    }

    protected String buildCacheKey(Invocation inv, Controller controller) {
        StringBuilder sb = new StringBuilder(inv.getActionKey());
        String urlPara = controller.getPara();
        if (urlPara != null) {
            sb.append('/').append(urlPara);
        }

        String queryString = controller.getRequest().getQueryString();
        if (queryString != null) {
            sb.append('?').append(queryString);
        }

        return sb.toString();
    }

    protected RenderInfo createRenderInfo(Render render) {
        return new RenderInfo(render);
    }

    protected void cacheAction(String cacheName, String cacheKey, Controller controller) {
        HttpServletRequest request = controller.getRequest();
        Map<String, Object> cacheData = new HashMap();
        Enumeration names = request.getAttributeNames();

        while(names.hasMoreElements()) {
            String name = (String)names.nextElement();
            cacheData.put(name, request.getAttribute(name));
        }

        Render render = controller.getRender();
        if (render != null) {
            cacheData.put("_renderKey", this.createRenderInfo(render));
        }

        Redis.use().hset(cacheName, cacheKey, cacheData);
    }

    protected void useCacheDataAndRender(Map<String, Object> cacheData, Controller controller) {
        HttpServletRequest request = controller.getRequest();
        Set<Map.Entry<String, Object>> set = cacheData.entrySet();
        Iterator it = set.iterator();

        while(it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)it.next();
            request.setAttribute((String)entry.getKey(), entry.getValue());
        }

        request.removeAttribute("_renderKey");
        RenderInfo renderInfo = (RenderInfo)cacheData.get("_renderKey");
        if (renderInfo != null) {
            controller.render(renderInfo.createRender());
        }

    }
}
