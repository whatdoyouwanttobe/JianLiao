package com.zoulf.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.zoulf.common.widget.convention.PlaceHolderView;

/**
 * @author Zoulf
 */

public abstract class MyFragment extends Fragment {

  protected View mRoot;
  protected Unbinder mRootUnBinder;
  protected PlaceHolderView mPlaceHolderView;
  // 标识是否是第一次初始化数据
  protected boolean mIsFristInitData = true;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    initArgs(getArguments());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    if (mRoot == null) {
      int layId = getContentLayoutId();
      // 初始化当前的根部局，但是不再创建时就添加到 container 中
      View root = inflater.inflate(layId, container, false);
      initWidget(root);
      mRoot = root;
    } else {
      if (mRoot.getParent() != null) {
        // 把当前 root 从其父控件中移除
        ((ViewGroup) mRoot.getParent()).removeView(mRoot);
      }
    }

    return mRoot;
  }

  /**
   * 初始化相关参数
   */
  protected void initArgs(Bundle bundle) {

  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (mIsFristInitData) {
      // 触发一次后就不会触发
      mIsFristInitData = false;
      onFirstInit();
    }
    // 当 View 创建完成后初始化数据
    initData();
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
  protected void initWidget(View root) {
    mRootUnBinder = ButterKnife.bind(this, root);
  }

  /**
   * 初始化数据
   */
  protected void initData() {

  }

  /**
   * 当首次初始化数据时调用
   */
  protected void onFirstInit() {

  }

  /**
   * 按下返回按键时调用
   *
   * @return 返回true表示已经处理返回逻辑，Activity不用自己再finish 返回false表示未处理逻辑，Activity走自己的逻辑
   */
  public boolean onBackPressed() {
    return false;
  }

  /**
   * 设置占位布局
   * @param placeHolderView 继承了占位布局规范的View
   */
  public void setPlaceHolderView(PlaceHolderView placeHolderView) {
     this.mPlaceHolderView = placeHolderView;
  }
}
