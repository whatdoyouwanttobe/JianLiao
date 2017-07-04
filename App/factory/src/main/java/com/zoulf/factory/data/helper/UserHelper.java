package com.zoulf.factory.data.helper;

import com.zoulf.factory.Factory;
import com.zoulf.factory.R;
import com.zoulf.factory.data.DataSource;
import com.zoulf.factory.model.api.RspModel;
import com.zoulf.factory.model.api.user.UserUpdateModel;
import com.zoulf.factory.model.card.UserCard;
import com.zoulf.factory.model.db.User;
import com.zoulf.factory.net.NetWork;
import com.zoulf.factory.net.RemoteService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Zoulf.
 */

public class UserHelper {

  // 更新用户信息的操作，异步的
  public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
    // 调用Retrofit对我们的网络请求接口做代理
    RemoteService service = NetWork.remote();
    // 得到一个Call
    Call<RspModel<UserCard>> call = service.userUpdate(model);
    // 网络请求
    call.enqueue(new Callback<RspModel<UserCard>>() {
      @Override
      public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
        RspModel<UserCard> rspModel = response.body();
        if (rspModel.success()) {
          UserCard userCard = rspModel.getResult();
          // 数据库的存储操作，需要把UserCard转换为User
          // 保存用户信息
          User user = userCard.build();
          user.save();
          // 返回成功
          callback.onDataLoaded(userCard);
        } else {
          // 错误情况下进行错误分配
          Factory.decodeRspCode(rspModel, callback);
        }
      }

      @Override
      public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
        callback.onDataNotAvailableLoaded(R.string.data_network_error);
      }
    });
  }

  public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
    RemoteService service = NetWork.remote();
    Call<RspModel<List<UserCard>>> call = service.userSearch(name);

    call.enqueue(new Callback<RspModel<List<UserCard>>>() {
      @Override
      public void onResponse(Call<RspModel<List<UserCard>>> call,
          Response<RspModel<List<UserCard>>> response) {

        RspModel<List<UserCard>> rspModel = response.body();
        if (rspModel.success()) {
          // 返回数据
          callback.onDataLoaded(rspModel.getResult());
        } else {
          Factory.decodeRspCode(rspModel, callback);
        }
      }

      @Override
      public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
        callback.onDataNotAvailableLoaded(R.string.data_network_error);
      }
    });

    // 把当前的调度者返回
    return call;
  }

  // 关注的网络请求
  public static void follow(String id, final DataSource.Callback<UserCard> callback) {
    RemoteService service = NetWork.remote();
    Call<RspModel<UserCard>> call = service.userFollow(id);

    call.enqueue(new Callback<RspModel<UserCard>>() {
      @Override
      public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
        RspModel<UserCard> rspModel = response.body();
        if (rspModel.success()) {
          UserCard userCard = rspModel.getResult();
          // 保存到本地数据库
          User user = userCard.build();
          user.save();
          // TODO 通知联系人列表刷新

          // 返回数据
          callback.onDataLoaded(rspModel.getResult());
        } else {
          Factory.decodeRspCode(rspModel, callback);
        }
      }

      @Override
      public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
        callback.onDataNotAvailableLoaded(R.string.data_network_error);
      }
    });

  }
}

