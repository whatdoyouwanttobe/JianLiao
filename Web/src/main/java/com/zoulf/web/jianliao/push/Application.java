package com.zoulf.web.jianliao.push;

import com.zoulf.web.jianliao.push.provider.AuthRequestFilter;
import com.zoulf.web.jianliao.push.provider.GsonProvider;
import com.zoulf.web.jianliao.push.service.AccountService;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author Zoulf
 */
public class Application extends ResourceConfig {

  public Application() {
    // 注册逻辑处理的包名
    packages(AccountService.class.getPackage().getName());

    // 注册我们的全局请求拦截器
    register(AuthRequestFilter.class);
    // 注册Json解析器
    // register(JacksonJsonProvider.class);
    // 替换为Gson解析器
    register(GsonProvider.class);

    // 注册日志打印输出
    register(Logger.class);

  }
}
