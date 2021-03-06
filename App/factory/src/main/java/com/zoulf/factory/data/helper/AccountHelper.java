package com.zoulf.factory.data.helper;

import android.text.TextUtils;
import com.zoulf.factory.Factory;
import com.zoulf.factory.R;
import com.zoulf.factory.data.DataSource;
import com.zoulf.factory.model.api.RspModel;
import com.zoulf.factory.model.api.account.AccountRspModel;
import com.zoulf.factory.model.api.account.LoginModel;
import com.zoulf.factory.model.api.account.RegisterModel;
import com.zoulf.factory.model.db.User;
import com.zoulf.factory.net.NetWork;
import com.zoulf.factory.net.RemoteService;
import com.zoulf.factory.persistence.Account;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Zoulf.
 */

public class AccountHelper {

  /**
   * 注册的接口，异步的调用
   *
   * @param model 传递一个注册的model
   * @param callback 成功与失败的接口回送
   */
  public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
    // 调用Retrofit对我们的网络请求接口作代理
    RemoteService service = NetWork.remote();
    // 得到一个Call
    Call<RspModel<AccountRspModel>> call = service.accountRegister(model);

    call.enqueue(new AccountRspCallback(callback));
  }

  /**
   * 登录的接口，异步的调用
   *
   * @param model 传递一个登录 的model
   * @param callback 成功与失败的接口回送
   */
  public static void login(final LoginModel model, final DataSource.Callback<User> callback) {
    // 调用Retrofit对我们的网络请求接口做代理
    RemoteService service = NetWork.remote();
    // 得到一个Call
    Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
    // 异步的请求
    call.enqueue(new AccountRspCallback(callback));
  }

  /**
   * 对设备Id进行绑定的操作
   *
   * @param callback Callback
   */
  public static void bindPush(final DataSource.Callback<User> callback) {
    // 得到pushId并检查是否为空
    String pushId = Account.getPushId();
    if (TextUtils.isEmpty(pushId)) {
      return;
    }

    // 调用Retrofit对我们的网络请求接口作代理
    RemoteService service = NetWork.remote();
    // 得到一个Call
    Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
    call.enqueue(new AccountRspCallback(callback));
  }

  /**
   * 请求的回调部分封装
   */
  private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {

    final DataSource.Callback<User> callback;

    AccountRspCallback(DataSource.Callback<User> callback) {
      this.callback = callback;
    }

    @Override
    public void onResponse(Call<RspModel<AccountRspModel>> call,
        Response<RspModel<AccountRspModel>> response) {

      // 请求成功返回
      // 从返回中得到我们的全局Model，内部是使用的Gson进行解析
      RspModel<AccountRspModel> rspModel = response.body();

      if (rspModel.success()) {
        // 拿到实体
        AccountRspModel accountRspModel = rspModel.getResult();
        // 获取我的信息
        User user = accountRspModel.getUser();
        DbHelper.save(User.class, user);

        // 同步到XML持久化中
        Account.login(accountRspModel);

        // 判断绑定状态，是否绑定设备
        if (accountRspModel.isBind()) {
          // 设置绑定状态
          Account.setBind(true);
          // 直接返回
          if (callback != null) {
            callback.onDataLoaded(user);
          }
        } else {
          // 进行绑定的唤起
          bindPush(callback);
        }
      } else {
        Factory.decodeRspCode(rspModel, callback);
      }
    }

    @Override
    public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
      // 网络请求失败
      if (callback != null) {
        callback.onDataNotAvailableLoaded(R.string.data_network_error);
      }
    }

  }


}

