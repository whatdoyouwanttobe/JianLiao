package com.zoulf.jianliao.activities;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.zoulf.common.app.MyActivity;
import com.zoulf.common.widget.a.PortraitView;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.helper.NavHelper;
import net.qiujuer.genius.ui.widget.FloatActionButton;

public class MainActivity extends MyActivity
    implements BottomNavigationView.OnNavigationItemSelectedListener {

  @BindView(R.id.appbar)
  View mLayAppbar;

  @BindView(R.id.im_portrait)
  PortraitView mPortrait;

  @BindView(R.id.txt_title)
  TextView mTitle;

  @BindView(R.id.lay_container)
  FrameLayout mContainer;

  @BindView(R.id.navigation)
  BottomNavigationView mNavigation;

  @BindView(R.id.btn_action)
  FloatActionButton mAction;

  private NavHelper mNavHelper;

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_main;
  }

  @Override
  protected void initWidget() {
    super.initWidget();

    // 初始化底部辅助工具类
    mNavHelper = new NavHelper();

    // 添加对底部按钮点击的监听
    mNavigation.setOnNavigationItemSelectedListener(this);

    // 设置标题栏的背景图片，直接在XML设置可能会导致图片变形
    Glide.with(this)
        .load(R.drawable.bg_src_morning)
        .centerCrop()
        .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
          @TargetApi(VERSION_CODES.JELLY_BEAN)
          @Override
          public void onResourceReady(GlideDrawable resource,
              GlideAnimation<? super GlideDrawable> glideAnimation) {
            this.view.setBackground(resource.getCurrent());
          }
        });
  }

  @Override
  protected void initData() {
    super.initData();
  }

  @OnClick(R.id.im_search)
  void onSearchMenuClick() {

  }

  @OnClick(R.id.btn_action)
  void onActionClick() {

  }

  /**
   * 当我们的底部导航被点击的时候触发
   *
   * @param item MenuItem
   * @return True 代表我们能够处理这个点击
   */
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    return mNavHelper.performClickMenu(item.getItemId());
  }
}
