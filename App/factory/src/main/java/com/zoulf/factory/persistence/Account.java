package com.zoulf.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.zoulf.factory.Factory;
import com.zoulf.factory.model.api.account.AccountRspModel;
import com.zoulf.factory.model.db.User;
import com.zoulf.factory.model.db.User_Table;

/**
 * @author Zoulf.
 */

public class Account {

  private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
  private static final String KEY_IS_BIND = "KEY_IS_BIND";
  private static final String KEY_TOKEN = "KEY_IS_BIND";
  private static final String KEY_USER_ID = "KEY_IS_BIND";
  private static final String KEY_IS_ACCOUNT = "KEY_IS_BIND";

  // 设备的推送Id
  private static String pushId;
  private static boolean isBind;
  // 登录状态的token，用来接口请求
  private static String token;
  // 登录的用户ID
  private static String userId;
  // 登录的账户
  private static String account;

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
        .putBoolean(KEY_IS_BIND, isBind)
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
    isBind = sp.getBoolean(KEY_IS_BIND, false);
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
    // 用户Id 和 Token 不为空
    return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token);
  }

  /**
   * 是否已经完善了用户信息
   *
   * @return True 是完成了
   */
  public static boolean isComplete() {
//    // 首先保证登录成功
//    if (isLogin()) {
//      User self = getUser();
//      return !TextUtils.isEmpty(self.getDesc())
//          && !TextUtils.isEmpty(self.getPortrait())
//          && self.getSex() != 0;
//    }
//    // 未登录返回信息不完全
//    return false;
    return isLogin();
  }

  /**
   * 返回当前账号是否绑定
   *
   * @return True已绑定
   */
  public static boolean isBind() {
    return isBind;
  }

  /**
   * 设置绑定状态
   */
  public static void setBind(boolean isBind) {
    Account.isBind = isBind;
    Account.save(Factory.app());
  }

  /**
   * 保存我自己的信息到持久化XML中
   *
   * @param model AccountRspModel
   */
  public static void login(AccountRspModel model) {
    // 存储当前登录的token，用户Id，方便从数据库中查询我的信息
    Account.token = model.getToken();
    Account.account = model.getAccount();
    Account.userId = model.getUser().getId();
    save(Factory.app());
  }

  /**
   * 获取当前登录的用户信息
   *
   * @return User
   */
  public static User getUser() {
    return TextUtils.isEmpty(userId) ? new User() :
        SQLite.select()
            .from(User.class)
            .where(User_Table.id.eq(userId))
            .querySingle();
  }
}

