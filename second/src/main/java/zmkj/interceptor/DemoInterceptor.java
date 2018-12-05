package zmkj.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class DemoInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        System.out.println("执行前");
        invocation.invoke();
        System.out.println("执行后");
    }
}
