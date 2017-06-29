package com.zoulf.factory.presenter.account;

import com.zoulf.factory.presenter.BasePresenter;
import com.zoulf.factory.presenter.account.LoginContract.View;

/**
 * 登录的逻辑实现
 *
 * @author Zoulf.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements
    LoginContract.Presenter {

  public LoginPresenter(View view) {
    super(view);
  }

  @Override
  public void login(String phone, String password) {

  }
}
