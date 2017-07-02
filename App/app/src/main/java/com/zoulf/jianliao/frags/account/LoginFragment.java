package com.zoulf.jianliao.frags.account;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.zoulf.common.app.PresenterFragment;
import com.zoulf.factory.presenter.account.LoginContract;
import com.zoulf.factory.presenter.account.LoginContract.Presenter;
import com.zoulf.factory.presenter.account.LoginPresenter;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.activities.MainActivity;
import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

/**
 * 登录的界面
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter>
    implements LoginContract.View {

  @BindView(R.id.edit_phone)
  EditText mPhone;
  @BindView(R.id.edit_password)
  EditText mPassword;

  @BindView(R.id.loading)
  Loading mLoading;

  @BindView(R.id.btn_submit)
  Button mSubmit;

  private AccountTrigger mAccountTrigger;

  public LoginFragment() {
    // Required empty public constructor
  }

  @Override
  protected Presenter initPresenter() {
    return new LoginPresenter(this);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // 拿到Activity的引用
    mAccountTrigger = (AccountTrigger) context;

  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.fragment_login;
  }

  @OnClick(R.id.btn_submit)
  void onSubmitClick() {
    String phone = mPhone.getText().toString();
    String password = mPassword.getText().toString();

    // 调用P层进行注册
    mPresenter.login(phone, password);
  }

  @OnClick(R.id.txt_go_register)
  void onShowRegisterClick() {
    // 让AccountActivity进行界面切换
    mAccountTrigger.triggerView();
  }

  @Override
  public void showLoading() {
    super.showLoading();

    // 正在进行时，正在进行注册，界面不可操作
    // 开始Loading
    mLoading.start();
    // 让控件不可以输入
    mPhone.setEnabled(false);
    mPassword.setEnabled(false);
    // 提交按钮不可以继续点击
    mSubmit.setEnabled(false);

  }

  @Override
  public void showError(@StringRes int str) {
    super.showError(str);
    // 当需要显示错误的时候触发，一定是结束了
    // 停止Loading
    mLoading.stop();
    // 让控件可以输入
    mPhone.setEnabled(true);
    mPassword.setEnabled(true);
    // 提交按钮可以继续点击
    mSubmit.setEnabled(true);
  }

  @Override
  public void loginSuccess() {
    MainActivity.show(getContext());
    getActivity().finish();
  }
}
