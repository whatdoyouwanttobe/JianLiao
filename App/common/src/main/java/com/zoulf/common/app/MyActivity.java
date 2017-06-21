package com.zoulf.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import java.util.List;

/**
 * @author Zoulf.
 */

public abstract class MyActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 在界面未初始化之前调用的初始化窗口
    initWindows();

    if (initArgs(getIntent().getExtras())) {
      // 得到界面Id并设置到Activity界面中
      int layId = getContentLayoutId();
      setContentView(layId);
      initWidget();
      initData();
    } else {
      finish();
    }
  }

  /**
   * 初始化窗口
   */
  protected void initWindows() {

  }

  /**
   * 初始化相关参数
   *
   * @param bundle 参数Bundle
   * @return 如果参数正确返回 true ，错误返回 false
   */
  protected boolean initArgs(Bundle bundle) {
    return true;
  }

  /**
   * 得到当前界面的资源文件Id
   *
   * @return 资源文件Id
   */
  protected abstract int getContentLayoutId();

  /**
   * 初始化控件
   */
  protected void initWidget() {
    ButterKnife.bind(this);
  }

  /**
   * 初始化数据
   */
  protected void initData() {

  }

  @Override
  public boolean onSupportNavigateUp() {
    // 当点击界面导航返回时，finish 当前界面
    finish();
    return super.onSupportNavigateUp();
  }

  @Override
  public void onBackPressed() {
    // 得到当前Activity下的所有Fragment
    List<Fragment> fragments = getSupportFragmentManager().getFragments();
    // 判断是否为空
    if (fragments != null && fragments.size() > 0) {
      for (Fragment fragment : fragments) {
        // 判断是否为我们能够处理的Fragment类型
        if (fragment instanceof MyFragment) {
          // 判断是否拦截返回按钮，如果有直接return
          if (((MyFragment) fragment).onBackPressed()) {
            return;
          }
        }
      }
    }

    super.onBackPressed();
    finish();
  }
}
