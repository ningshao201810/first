package zmkj.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.upload.UploadFile;
import zmkj.interceptor.DemoInterceptor;
import zmkj.interceptor.RedisInterceptor;
import zmkj.model.Address;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import zmkj.model.Fuser;
import zmkj.model.User;

import java.io.File;
@Before(DemoInterceptor.class)
public class AddressController extends Controller {

    @ActionKey("/get")
    public void get1() {
        Page<Address> list = Address.dao.paginate(1, 5, "select *", "from address where id <5");
        setAttr("list",list);
        Object list1 = getAttr("list");
        System.out.println(list1);
        renderJsp("/index.jsp");
    }

    @ActionKey("/upload")
    public void upload1() {
        UploadFile file = getFile("myFile");
        renderJson("aa");
    }

    @ActionKey("/download")
    public void download1() {
        System.out.println(new File("aa.txt").isFile());
        renderFile(new File("aa.txt"),"aa.txt");
    }

    Cache cache= Redis.use("redis");
    @Before(RedisInterceptor.class)
    @CacheName("test")
    @ActionKey("/add")
    public void add1(Fuser user) {
        Prop prop = PropKit.use("prop.properties");
        System.out.println("a:"+prop.get("a"));
        System.out.println("b:"+prop.get("b"));
        cache.set("user",getModel(Fuser.class,"user",true));
        //getModel(Fuser.class,"user",true).save();
        renderJson("aa");
        System.out.println("重定向");
        redirect("/add2");

    }
    @Clear
    @ActionKey("/add2")
    public void add3(User user) {
        Cache cache= Redis.use("redis");
        Object user1 = cache.get("user");
        System.out.println("缓存："+user1);
        renderJson("aa");
    }
}
