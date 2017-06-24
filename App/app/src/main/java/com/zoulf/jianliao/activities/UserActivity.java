package com.zoulf.jianliao.activities;

import static android.R.attr.data;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.zoulf.common.app.MyActivity;
import com.zoulf.jianliao.R;

public class UserActivity extends MyActivity {

  private Fragment mCurFragment;

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_user;
  }

  @Override
  protected void initWidget() {
    super.initWidget();

    mCurFragment = new UpdateInfoFragment();
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.lay_container, mCurFragment)
        .commit();
  }

  // Activity中收到剪切图片成功的回调
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    mCurFragment.onActivityResult(requestCode, resultCode, data);
  }
}
