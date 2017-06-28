package com.zoulf.jianliao.frags.account;


import android.content.Context;
import com.zoulf.common.app.MyFragment;
import com.zoulf.jianliao.R;

/**
 * 注册的界面.
 */
public class RegisterFragment extends MyFragment {

  private AccountTrigger mAccountTrigger;

  public RegisterFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // 拿到Activity的引用
    mAccountTrigger = (AccountTrigger) context;

  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.fragment_register;
  }

}
