package com.zoulf.jianliao.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.zoulf.common.app.MyActivity;
import com.zoulf.common.app.MyFragment;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.frags.account.AccountTrigger;
import com.zoulf.jianliao.frags.account.LoginFragment;
import com.zoulf.jianliao.frags.account.RegisterFragment;
import net.qiujuer.genius.ui.compat.UiCompat;

public class AccountActivity extends MyActivity implements AccountTrigger {

  private MyFragment mCruFragment;
  private MyFragment mLoginFragment;
  private MyFragment mRegisterFragment;

  @BindView(R.id.im_bg)
  ImageView mBg;

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

    // 初始化Fragment
    mCruFragment = mLoginFragment = new LoginFragment();
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.lay_container, mCruFragment)
        .commit();

    // 初始化背景
    Glide.with(this)
        .load(R.drawable.bg_src_haibian)
        .centerCrop()
        .into(new ViewTarget<ImageView, GlideDrawable>(mBg) {
          @Override
          public void onResourceReady(GlideDrawable resource,
              GlideAnimation<? super GlideDrawable> glideAnimation) {

            // 拿到Glide的drawable
            Drawable drawable = resource.getCurrent();
            // 使用适配类进行包装
            drawable = DrawableCompat.wrap(drawable);
            drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent),
                Mode.SCREEN); // 设置着色的效果与颜色，蒙版模式

            // 设置给ImageView
            this.view.setImageDrawable(drawable);
          }
        });
  }

  @Override
  public void triggerView() {
    MyFragment fragment;
    if (mCruFragment == mLoginFragment) {
      if (mRegisterFragment == null) {
        // 默认情况下为null ，第一次之后就不为空了
        mRegisterFragment = new RegisterFragment();
      }
      fragment = mRegisterFragment;
    } else {
      // 无需判空，因为initWidget已经赋值
      fragment = mLoginFragment;
    }

    // 切换显示
    mCruFragment = fragment;
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.lay_container, fragment)
        .commit();

  }
}
