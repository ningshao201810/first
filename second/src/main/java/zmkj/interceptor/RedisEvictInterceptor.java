package zmkj.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.redis.Redis;

public class RedisEvictInterceptor implements Interceptor {
    public RedisEvictInterceptor() {
    }

    public final void intercept(Invocation inv) {
        inv.invoke();
        Redis.use("bbs").hdel(this.buildCacheName(inv));
    }

    private String buildCacheName(Invocation inv) {
        CacheName cacheName = (CacheName)inv.getMethod().getAnnotation(CacheName.class);
        if (cacheName != null) {
            return cacheName.value();
        } else {
            cacheName = (CacheName)inv.getController().getClass().getAnnotation(CacheName.class);
            if (cacheName == null) {
                throw new RuntimeException("EvictInterceptor need CacheName annotation in controller.");
            } else {
                return cacheName.value();
            }
        }
    }
}
