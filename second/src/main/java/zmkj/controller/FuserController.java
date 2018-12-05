package zmkj.controller;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import zmkj.model.Fuser;

import java.util.List;

public class FuserController extends Controller {
    public void add() {
        new Fuser().set("id","1").set("name","a").save();
        renderJsp("/index.jsp");
    }

    public void index() {
        renderJsp("/index.jsp");
    }

    @ActionKey("/get2")
    public void get() {
        List<Fuser> fusers = Fuser.dao.find("select * from  fuser");
        System.out.println(fusers);
        setAttr("list",fusers);
        Fuser.dao.paginate(1,5,"select *","from address where id <5");
        renderJson("list",fusers);
    }
}
