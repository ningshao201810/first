package zmkj.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.redis.Redis;
import redis.clients.jedis.Jedis;
import zmkj.interceptor.RedisEvictInterceptor;

public class Test {
    @Before(RedisEvictInterceptor.class)
    @CacheName("test")
    public static void main(String[] args) {
        Jedis jedis=new Jedis();
        System.out.println("main:"+jedis.get("a"));
        jedis.set("a","更改之后");
        System.out.println("main后"+jedis.get("a"));
        jedis.close();
        System.out.println(Redis.use("bbs"));
        Redis.use("bbs").set("a","更改之后");
    }
}
