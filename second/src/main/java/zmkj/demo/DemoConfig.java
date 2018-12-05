package zmkj.demo;

import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.plugin.redis.serializer.JdkSerializer;
import com.jfinal.template.Engine;
import redis.embedded.RedisServer;
import zmkj.controller.AddressController;
import zmkj.interceptor.DemoInterceptor;
import zmkj.model.Address;
import zmkj.model.Fuser;

import java.io.IOException;

public class DemoConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants constants) {
        constants.setUrlParaSeparator(":");
        constants.setDevMode(true);
        constants.setBaseUploadPath("uploadDemo/");
    }

    @Override
    public void configRoute(Routes routes) {
        /*routes.add("/index", FuserController.class,"jsp");
        routes.add("/add", FuserController.class,"jsp");*/
        routes.add("/get", AddressController.class);
        routes.addInterceptor(new DemoInterceptor());
    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {
        DruidPlugin dp=new DruidPlugin("jdbc:postgresql://127.0.0.1:5432/test2","postgres","tm123wdwlbsm");
        DruidPlugin dp2=new DruidPlugin("jdbc:postgresql://127.0.0.1:5432/postgres","postgres","tm123wdwlbsm");
        plugins.add(dp);
        plugins.add(dp2);
        ActiveRecordPlugin arp=new ActiveRecordPlugin(dp);
        ActiveRecordPlugin arp2=new ActiveRecordPlugin("aa",dp2);
        //arp.addMapping("fuser","id",Fuser.class);
        arp.addMapping("address","id", Address.class);
        arp2.addMapping("fuser", Fuser.class);
        arp.setDialect(new PostgreSqlDialect());
        arp2.setDialect(new PostgreSqlDialect());

        plugins.add(arp);
        plugins.add(arp2);

        RedisServer redisServer = null;
        try {
            redisServer = new RedisServer(6379);
        } catch (IOException e) {
            e.printStackTrace();
        }
        redisServer.start();
        RedisPlugin redisPlugin=new RedisPlugin("redis","127.0.0.1");
        redisPlugin.setSerializer(JdkSerializer.me);
        plugins.add(redisPlugin);
        /*SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cron4jPlugin plugin=new Cron4jPlugin();
        plugin.addTask("* * * * *", new Runnable() {
            @Override
            public void run() {
                System.out.println("这是定时任务"+simpleDateFormat.format(new Date()));
            }
        });
        plugins.add(plugin);*/
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {
        interceptors.add(new DemoInterceptor());
    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
