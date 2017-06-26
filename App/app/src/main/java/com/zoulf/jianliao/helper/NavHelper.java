package com.zoulf.jianliao.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * 解决对 Fragment 的调度与重用问题
 * 达到最优的 Fragment 的切换
 *
 * @author Zoulf.
 */

public class NavHelper<T> {

  // 所有的Tab集合，采用轻量级的SparseArray
  private final SparseArray<Tab<T>> tabs = new SparseArray<>();

  // 用于初始化的必须参数
  private final Context context;
  private final int containerId;
  private final FragmentManager fragmentManager;
  private OnTabChangedListener<T> listener;

  // 当前的一个选中的Tab
  private Tab<T> currentTab;

  public NavHelper(Context context, int containerId,
      FragmentManager fragmentManager,
      OnTabChangedListener<T> listener) {
    this.context = context;
    this.containerId = containerId;
    this.fragmentManager = fragmentManager;
    this.listener = listener;
  }

  /**
   * 流式添加Tab
   *
   * @param menuId Tab对应的菜单Id
   * @param tab Tab
   */
  public NavHelper<T> add(int menuId, Tab<T> tab) {
    tabs.put(menuId, tab);
    return this;
  }

  /**
   * 获取当前显示Tab
   *
   * @return 当前的Tab
   */
  public Tab<T> getCurrentTab() {
    return currentTab;
  }

  /**
   * 执行点击菜单的操作
   *
   * @param menuId 菜单的Id
   * @return 是否能够处理这个点击
   */
  public boolean performClickMenu(int menuId) {
    Tab<T> tab = tabs.get(menuId);
    if (tab != null) {
      doSelect(tab);
      return true;
    }
    return false;
  }

  /**
   * 进行真实的Tab选择操作
   *
   * @param tab Tab
   */
  private void doSelect(Tab<T> tab) {
    Tab<T> oldTab = null;

    if (currentTab != null) {
      oldTab = currentTab;
      if (oldTab == tab) {
        // 如果说当前的Tab就是点击的Tab，
        // 那么我们不做处理
        notifyReselect(tab);
        return;
      }
    }
    // 赋值并调用切换方法
    currentTab = tab;
    doTabChanged(currentTab, oldTab);
  }

  /**
   * 进行Fragment的真实的调度操作
   *
   * @param newTab 新的
   * @param oldTab 旧的
   */
  private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
    FragmentTransaction ft = fragmentManager.beginTransaction();

    if (oldTab != null) {
      if (oldTab.fragment != null) {
        // 从界面中移除但是还在Fragment的缓存空间中
        ft.detach(oldTab.fragment);
      }
    }

    if (newTab != null) {
      if (newTab.fragment == null) {
        // 首次新建
        Fragment fragment = Fragment.instantiate(context, newTab.cls.getName(), null);
        // 缓存起来
        newTab.fragment = fragment;
        // 提交到FragmentManager
        ft.add(containerId, fragment, newTab.cls.getName());
      } else {
        // 从FragmentManger的缓存空间中重新加载到界面中
        ft.attach(newTab.fragment);
      }
    }
    // 提交事务
    ft.commit();
    // 通知回调
    notifyTabSelect(newTab, oldTab);
  }

  /**
   * 回调我们的监听器
   *
   * @param newTab 新的Tab<T>
   * @param oldTab 旧的Tab<T>
   */
  private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
    if (listener != null) {
      listener.onTabChanged(newTab, oldTab);
    }
  }

  private void notifyTabReselect(Tab<T> tab) {
    // TODO 二次点击Tab所做的操作
  }

  /**
   * 我们所有的Tab基础属性
   *
   * @param <T> 泛型的额外参数
   */
  public static class Tab<T> {

    // Fragment对应的Class信息
    public Class<?> cls;
    // 额外的字段，用户自己定义
    public T extra;

    // 内部缓存的对应的Fragment,
    // Package权限，外部无法实现
    Fragment fragment;

    public Tab(Class<?> cls, T extra) {
      this.cls = cls;
      this.extra = extra;
    }
  }

  /**
   * 定义事件处理完成后的回调接口
   *
   * @param <T> 泛型
   */
  public interface OnTabChangedListener<T> {

    void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
  }
}
