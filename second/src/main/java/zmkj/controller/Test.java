package zmkj.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.ehcache.CacheName;
import redis.clients.jedis.Jedis;
import zmkj.interceptor.RedisEvictInterceptor;

public class Test {
    @Before(RedisEvictInterceptor.class)
    @CacheName("test")
    public static void main(String[] args) {
        Jedis jedis=new Jedis();
        jedis.set("a","更改之后");
        System.out.println("main后"+jedis.get("a"));
        jedis.close();
    }
}
