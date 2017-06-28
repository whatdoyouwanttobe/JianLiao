package com.zoulf.jianliao.activities;

import android.content.Intent;
import com.zoulf.common.app.MyActivity;
import com.zoulf.common.app.MyFragment;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.frags.user.UpdateInfoFragment;

public class UserActivity extends MyActivity {

  private MyFragment mCruFragment;

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_user;
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
