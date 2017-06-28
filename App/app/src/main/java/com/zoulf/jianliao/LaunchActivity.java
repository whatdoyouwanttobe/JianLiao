package com.zoulf.jianliao;

import com.zoulf.common.app.MyActivity;
import com.zoulf.jianliao.activities.MainActivity;
import com.zoulf.jianliao.frags.assist.PermissionFragment;

public class LaunchActivity extends MyActivity {

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_launch;
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (PermissionFragment.haveAll(this, getSupportFragmentManager())) {
      MainActivity.show(this);
      finish();
    }
  }

}
