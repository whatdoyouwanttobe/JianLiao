package com.zoulf.jianliao.activities;

import android.content.Context;
import android.content.Intent;
import com.zoulf.common.app.MyActivity;
import com.zoulf.common.app.MyFragment;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.frags.account.UpdateInfoFragment;

public class AccountActivity extends MyActivity {

  private MyFragment mCruFragment;

  /**
   * 账户 Activity 显示的入口
   *
   * @param context Context
   */
  public static void show(Context context) {
    context.startActivity(new Intent(context, AccountActivity.class));
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_account;
  }

  @Override
  protected void initWidget() {
    super.initWidget();
    mCruFragment = new UpdateInfoFragment();
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.lay_container, mCruFragment)
        .commit();

  }

  /**
   * Activity收到图片剪切成功的回调
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    mCruFragment.onActivityResult(requestCode, resultCode, data);
  }
}
