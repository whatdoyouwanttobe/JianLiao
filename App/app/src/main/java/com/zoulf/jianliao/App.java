package com.zoulf.jianliao;

import com.igexin.sdk.PushManager;
import com.zoulf.common.app.MyApplication;
import com.zoulf.factory.Factory;

/**
 * @author Zoulf.
 */

public class App extends MyApplication {

  @Override
  public void onCreate() {
    super.onCreate();

    // 调用Factory进行初始化
    Factory.setup();
    // 推送进行初始化
    PushManager.getInstance().initialize(this);
  }
}
