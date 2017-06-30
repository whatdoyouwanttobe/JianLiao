package com.zoulf.factory.data.helper;

import com.zoulf.factory.Factory;
import com.zoulf.factory.R;
import com.zoulf.factory.data.DataSource;
import com.zoulf.factory.model.api.RspModel;
import com.zoulf.factory.model.api.account.AccountRspModel;
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
    RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
    // 得到一个Call
    Call<RspModel<AccountRspModel>> call = service.accountRegister(model);

    call.enqueue(new Callback<RspModel<AccountRspModel>>() {
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
          // 进行的是数据库写入和缓存绑定
          // 第一种，直接保存
          user.save();

//          第二种通过ModelAdapter
//          FlowManager.getModelAdapter(User.class)
//              .save(user);
//          第三种，事务中
//          DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
//          definition.beginTransactionAsync(new ITransaction() {
//            @Override
//            public void execute(DatabaseWrapper databaseWrapper) {
//              FlowManager.getModelAdapter(User.class)
//                  .save(user);
//            }
//          }).build().execute();

          // 同步到XML持久化中
          Account.login(accountRspModel);

          // 判断绑定状态，是否绑定设备
          if (accountRspModel.isBind()) {
            // 直接返回
            callback.onDataLoaded(user);
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
        callback.onDataNotAvailableLoaded(R.string.data_network_error);
      }
    });
  }

  /**
   * 对设备Id进行绑定的操作
   *
   * @param callback Callback
   */
  public static void bindPush(final DataSource.Callback<User> callback) {
    Account.setBind(true);
  }
}
