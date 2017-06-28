package com.zoulf.jianliao.frags.assist;


import android.Manifest.permission;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.zoulf.common.app.MyApplication;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.frags.media.GalleryFragment.TransStatusBottomSheetDialog;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

/**
 * 权限申请弹出框
 */
public class PermissionFragment extends BottomSheetDialogFragment
    implements PermissionCallbacks {

  //权限回调的标识
  private static final int RC = 0X0100;

  public PermissionFragment() {
    // Required empty public constructor
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new TransStatusBottomSheetDialog(getContext());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_permission, container, false);
    refreshState(root);
    // 找到按钮
    root.findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        // 点击时进行申请权限
        requestPerm();
      }
    });
    return root;
  }

  @Override
  public void onResume() {
    super.onResume();
    // 界面显示的时候进行刷新
    refreshState(getView());
  }

  /**
   * 刷新布局中的图片的状态
   *
   * @param root 根布局
   */
  private void refreshState(View root) {
    if (root == null) {
      return;
    }
    Context context = getContext();
    root.findViewById(R.id.im_state_permission_network)
        .setVisibility(haveNetWorkPerm(context) ? View.VISIBLE : View.GONE);
    root.findViewById(R.id.im_state_permission_read)
        .setVisibility(haveReadPerm(context) ? View.VISIBLE : View.GONE);
    root.findViewById(R.id.im_state_permission_write)
        .setVisibility(haveWritePerm(context) ? View.VISIBLE : View.GONE);
    root.findViewById(R.id.im_state_permission_record_audio)
        .setVisibility(haveRecordAudioPerm(context) ? View.VISIBLE : View.GONE);
  }

  /**
   * 检查是否有网络权限
   *
   * @param context 上下文
   * @return true则有
   */
  private static boolean haveNetWorkPerm(Context context) {
    // 准备需要检查的网络权限
    String[] perms = new String[]{
        permission.INTERNET,
        permission.ACCESS_NETWORK_STATE,
        permission.ACCESS_WIFI_STATE,
    };
    return EasyPermissions.hasPermissions(context, perms);
  }

  /**
   * 检查是否有外部存储读取权限
   *
   * @param context 上下文
   * @return true则有
   */
  private static boolean haveReadPerm(Context context) {
    // 准备需要检查的网络权限
    String[] perms = new String[]{
        permission.READ_EXTERNAL_STORAGE
    };
    return EasyPermissions.hasPermissions(context, perms);
  }

  /**
   * 检查是否有外部存储写入权限
   *
   * @param context 上下文
   * @return true则有
   */
  private static boolean haveWritePerm(Context context) {
    // 准备需要检查的网络权限
    String[] perms = new String[]{
        permission.WRITE_EXTERNAL_STORAGE
    };
    return EasyPermissions.hasPermissions(context, perms);
  }

  /**
   * 检查是否有录音权限
   *
   * @param context 上下文
   * @return true则有
   */
  private static boolean haveRecordAudioPerm(Context context) {
    // 准备需要检查的网络权限
    String[] perms = new String[]{
        permission.RECORD_AUDIO
    };
    return EasyPermissions.hasPermissions(context, perms);
  }

  /**
   * 私有的show方法
   */
  private static void show(FragmentManager manager) {
    // 调用BottomSheetDialogFragment已经准备好的显示方法
    new PermissionFragment()
        .show(manager, PermissionFragment.class.getName());
  }

  /**
   * 检查是否具有所有的权限
   *
   * @param context Context
   * @param manager FragmentManager
   * @return 是否有权限
   */
  public static boolean haveAll(Context context, FragmentManager manager) {
    // 检查是否具有所有的权限
    boolean haveAll = haveNetWorkPerm(context)
        && haveReadPerm(context)
        && haveWritePerm(context)
        && haveRecordAudioPerm(context);

    // 如果没有则显示当前申请权限的界面
    if (!haveAll) {
      show(manager);
    }
    return haveAll;
  }

  /**
   * 申请权限的方法
   */
  @AfterPermissionGranted(RC)
  private void requestPerm() {
    String[] perms = new String[]{
        permission.INTERNET,
        permission.ACCESS_NETWORK_STATE,
        permission.ACCESS_WIFI_STATE,
        permission.READ_EXTERNAL_STORAGE,
        permission.WRITE_EXTERNAL_STORAGE,
        permission.RECORD_AUDIO
    };

    if (EasyPermissions.hasPermissions(getContext(), perms)) {
      MyApplication.showToast(R.string.label_permission_ok);
      // Fragment 调用getView()得到根布局，前提是在onCreateView方法调用之后
      refreshState(getView());
    } else {
      EasyPermissions
          .requestPermissions(this, getString(R.string.title_assist_permissions), RC, perms);
    }
  }

  @Override
  public void onPermissionsGranted(int requestCode, List<String> perms) {

  }

  @Override
  public void onPermissionsDenied(int requestCode, List<String> perms) {
    // 如果权限有没有申请成功的权限在，则弹出弹出框，用户点击后进入设置界面自己打开权限
    if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
      new AppSettingsDialog
          .Builder(this)
          .build()
          .show();
    }
  }

  /**
   * 权限申请的时候回调的方法，在这个方法中把对应的权限申请状态交给EasyPermission框架
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // 传递对应的参数，并告知接收权限的处理者是自己
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }
}
