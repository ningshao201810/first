package zmkj.demo;

import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.template.Engine;
import zmkj.controller.AddressController;
import zmkj.interceptor.DemoInterceptor;
import zmkj.model.Address;
import zmkj.model.Fuser;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        // 用于缓存bbs模块的redis服务
        RedisPlugin bbsRedis = new RedisPlugin("bbs", "localhost");
        plugins.add(bbsRedis);

        // 用于缓存news模块的redis服务
        RedisPlugin newsRedis = new RedisPlugin("news", "192.168.3.9");
        plugins.add(newsRedis);

       /* //任务调度
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
