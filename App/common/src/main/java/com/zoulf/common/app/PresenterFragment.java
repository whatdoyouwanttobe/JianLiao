package com.zoulf.common.app;

import android.content.Context;
import android.support.annotation.StringRes;
import com.zoulf.factory.presenter.BaseContract;

/**
 * @author Zoulf.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends MyFragment
    implements BaseContract.View<Presenter> {

  protected Presenter mPresenter;

  protected abstract Presenter initPresenter();

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // 在界面onAttach之后就触发
    initPresenter();
  }

  @Override
  public void showError(@StringRes int str) {
    // 显示错误
    MyApplication.showToast(str);
  }

  @Override
  public void showLoading() {
    // TODO 显示一个Loading
  }

  @Override
  public void setPresenter(Presenter presenter) {
    // View中赋值Presenter
    mPresenter = presenter;
  }
}
