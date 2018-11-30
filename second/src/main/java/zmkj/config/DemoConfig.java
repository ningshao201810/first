package zmkj.config;

import com.jfinal.config.*;
import com.jfinal.template.Engine;
import zmkj.controller.HelloController;

public class DemoConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants constants) {
        constants.setUrlParaSeparator(":");
        constants.setDevMode(true);
        constants.setEncoding("GBK");
    }

    @Override
    public void configRoute(Routes routes) {
        routes.add("/index", HelloController.class);
        routes.add("/add", HelloController.class,"jsp");
    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
