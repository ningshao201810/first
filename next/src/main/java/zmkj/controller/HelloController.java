package zmkj.controller;

import com.jfinal.core.Controller;

public class HelloController extends Controller {
    public void index(String text){
        renderText("参数传递"+text);
    }
}
