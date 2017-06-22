package com.zoulf.jianliao;

import android.text.TextUtils;

/**
 * @author Zoulf.
 */

public class Presenter implements IPresenter {

  private IView mView;

  public Presenter(IView view) {
    this.mView = view;
  }

  @Override
  public void search() {
    // 开启界面 Loading
    String inputString = mView.getInputString();
    if (TextUtils.isEmpty(inputString)) {
      //为空直接返回
      return;
    }

    int hashCode = inputString.hashCode();

    IUserService service = new UserService();

    String serviceResult = service.search(hashCode);

    String result = "Result: " + inputString + "-" + serviceResult;

    // 关闭界面 Loading

    mView.setResultString(result);
  }


}
