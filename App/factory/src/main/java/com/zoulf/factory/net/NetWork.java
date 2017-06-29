package com.zoulf.factory.net;

import com.zoulf.common.Common.Constance;
import com.zoulf.factory.Factory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 *
 * @author Zoulf.
 */

public class NetWork {

  // 构建一个Retrofit
  public static Retrofit getRetrofit() {

    // 得到一个OK Client
    OkHttpClient client = new OkHttpClient.Builder()
        .build();

    Retrofit.Builder builder = new Retrofit.Builder();

    // 设置电脑链接
    return builder.baseUrl(Constance.API_URL)
        .client(client) // 设置client
        .addConverterFactory(GsonConverterFactory.create(Factory.getGson())) // 设置Json解析器
        .build();
  }
}
