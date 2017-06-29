package com.zoulf.factory.presenter;

import android.support.annotation.StringRes;

/**
 * MVP模式中公共的基本契约
 * @author Zoulf.
 */

public interface BaseContract {

  interface View<T extends Presenter> {

    // @StringRes ， 返回的是一个字符串的int值，公共的显示一个字符串错误
    void showError(@StringRes int str);

    // 显示进度条
    void showLoading();

    // 支持设置一个Presenter
    void setPresenter(T presenter);
  }

  interface Presenter {

    // 公用的开始触发
    void start();

    // 公用的销毁触发
    void destroy();

  }

}
