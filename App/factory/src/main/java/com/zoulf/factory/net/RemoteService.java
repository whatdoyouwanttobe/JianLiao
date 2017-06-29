package com.zoulf.factory.net;

import com.zoulf.factory.model.api.RspModel;
import com.zoulf.factory.model.api.account.AccountRspModel;
import com.zoulf.factory.model.api.account.RegisterModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Zoulf.
 */

public interface RemoteService {

  /**
   * 网络请求一个注册接口
   *
   * @param registerModel 传入的是RegisterModel
   * @return 返回的是RspModel<AccountRspModel>
   */
  @POST("account/register")
  Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel registerModel);

}
