package com.zoulf.web.jianliao.push;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
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

    // 注册Json解析器
    // register(JacksonJsonProvider.class);
    // 替换为Gson解析器
    register(GsonProvider.class);

    // 注册日志打印输出
    register(Logger.class);

  }
}
