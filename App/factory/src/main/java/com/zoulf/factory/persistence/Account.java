package com.zoulf.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import com.zoulf.factory.Factory;

/**
 * @author Zoulf.
 */

public class Account {

  private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
  // 设备的推送Id
  private static String pushId;

  /**
   * 存储数据到XML文件，持久化
   */
  private static void save(Context context) {
    // 获取数据持久化的SP
    SharedPreferences sp = context
        .getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
    // 存储数据
    sp.edit()
        .putString(KEY_PUSH_ID, pushId)
        .apply();

  }

  /**
   * 进行数据加载
   *
   * @param context Context
   */
  public static void load(Context context) {
    // 获取数据持久化的SP
    SharedPreferences sp = context
        .getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
    pushId = sp.getString(KEY_PUSH_ID, "");
  }

  /**
   * 设置并存储设备的Id
   *
   * @param pushId 设备推送ID
   */
  public static void setPushId(String pushId) {
    Account.pushId = pushId;
    Account.save(Factory.app());
  }

  /**
   * 获取推送Id
   *
   * @return 推送Id
   */
  public static String getPushId() {
    return pushId;
  }

  /**
   * 返回当前账号是否登录
   *
   * @return True已登录
   */
  public static boolean isLogin() {
    return true;
  }

  /**
   * 返回当前账号是否绑定
   *
   * @return True已绑定
   */
  public static boolean isBind() {
    return false;
  }
}
