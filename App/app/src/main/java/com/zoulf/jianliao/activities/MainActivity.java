package com.zoulf.jianliao.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.zoulf.common.app.MyActivity;
import com.zoulf.common.widget.PortraitView;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.frags.main.ActiveFragment;
import com.zoulf.jianliao.frags.main.ContactFragment;
import com.zoulf.jianliao.frags.main.GroupFragment;
import com.zoulf.jianliao.helper.NavHelper;
import com.zoulf.jianliao.helper.NavHelper.Tab;
import java.util.Objects;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

public class MainActivity extends MyActivity
    implements BottomNavigationView.OnNavigationItemSelectedListener,
    NavHelper.OnTabChangedListener<Integer> {

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

  private NavHelper<Integer> mNavHelper;

  /**
   * MainActivity显示的入口
   * @param context Context
   */
  public static void show(Context context) {
    context.startActivity(new Intent(context, MainActivity.class));
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_main;
  }

  @Override
  protected void initWidget() {
    super.initWidget();

    // 初始化底部辅助工具类
    mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);
    mNavHelper
        .add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
        .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
        .add(R.id.action_contact,
            new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));

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
    // 从底部导航中接管我们的Menu，然后进行手动的触发第一次点击
    Menu menu = mNavigation.getMenu();
    // 触发首次选中Home
    menu.performIdentifierAction(R.id.action_home, 0);
  }

  @OnClick(R.id.im_search)
  void onSearchMenuClick() {

  }

  @OnClick(R.id.btn_action)
  void onActionClick() {
    AccountActivity.show(this);
  }

  /**
   * 当我们的底部导航被点击的时候触发
   *
   * @param item MenuItem
   * @return True 代表我们能够处理这个点击
   */
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // 转接事件流到工具类中
    return mNavHelper.performClickMenu(item.getItemId());
  }

  /**
   * NavHelper处理后回调的方法
   *
   * @param newTab 新的Tab
   * @param oldTab 旧的Tab
   */
  @Override
  public void onTabChanged(Tab<Integer> newTab, Tab<Integer> oldTab) {
    // 从额外字段中取出我们的Title资源Id
    mTitle.setText(newTab.extra);

    // 对浮动按钮进行隐藏和显示的操作
    float transY = 0;
    float rotation = 0;
    if (Objects.equals(newTab.extra, R.string.title_home)) {
      // 主界面时隐藏
      transY = Ui.dipToPx(getResources(), 76);
    } else {
      // transY默认为0，是显示
      if (Objects.equals(newTab.extra, R.string.title_group)) {
        // 群
        mAction.setImageResource(R.drawable.ic_group_add);
        rotation = -360;
      } else {
        // 联系人
        mAction.setImageResource(R.drawable.ic_contact_add);
        rotation = 360;
      }
    }

    // 开始动画
    // 旋转，Y轴位移，弹性差值器，时间
    mAction.animate()
        .rotation(rotation)
        .translationY(transY)
        .setInterpolator(new AnticipateOvershootInterpolator(2))
        .setDuration(480)
        .start();
  }
}
