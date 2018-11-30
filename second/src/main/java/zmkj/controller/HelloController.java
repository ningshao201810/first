package zmkj.controller;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class HelloController extends Controller {
    @ActionKey("/index1")
    public void index1() throws UnsupportedEncodingException {
        renderText("参数传递"+getPara(0)+" ; 这是第二个:"+ URLDecoder.decode(getPara(1),"utf-8"));
    }

    @ActionKey("/index")
    public void index() {
        renderJsp("/index.jsp");
    }
}
