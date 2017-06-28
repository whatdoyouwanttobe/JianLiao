package com.zoulf.jianliao.frags.user;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Log;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;
import com.zoulf.common.app.MyApplication;
import com.zoulf.common.app.MyFragment;
import com.zoulf.common.widget.PortraitView;
import com.zoulf.factory.Factory;
import com.zoulf.factory.net.UploaderHelper;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.frags.media.GalleryFragment;
import com.zoulf.jianliao.frags.media.GalleryFragment.OnSelectedListener;
import java.io.File;

/**
 * 用户更新信息的界面
 */
public class UpdateInfoFragment extends MyFragment {

  @BindView(R.id.im_portrait)
  PortraitView mPortrait;

  public UpdateInfoFragment() {
    // Required empty public constructor
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.fragment_update_info;
  }

  @OnClick(R.id.im_portrait)
  void onPortraitClick() {
    new GalleryFragment()
        .setOnSelectedListener(new OnSelectedListener() {
          @Override
          public void onSelectedImage(String path) {
            UCrop.Options options = new UCrop.Options();
            // 设置图片处理的格式-JPEG
            options.setCompressionFormat(CompressFormat.JPEG);
            // 设置压缩后的图片精度
            options.setCompressionQuality(96);

            File dPath = MyApplication.getPortraitTmpFile();
            // 发起剪切
            UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                .withAspectRatio(1, 1) // 1比1比例
                .withMaxResultSize(520, 520) // 返回的最大尺寸
                .withOptions(options) // 相关参数
                .start(getActivity());
          }
        })
        // show 的时候建议使用getChildFragmentManager，
        // 直接使用FragmentManager可能会与Activity的Manager引起麻烦
        // tag GalleryFragment的class名
        .show(getChildFragmentManager(), GalleryFragment.class.getName());
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // 收到从Activity传递过来的回调，然后取出其中的值进行图片加载
    // 如果是能够处理的类型
    if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
      // 通过UCrop得到对应的Uri
      final Uri resultUri = UCrop.getOutput(data);
      if (resultUri != null) {
        loadPortrait(resultUri);
      }
    } else if (resultCode == UCrop.RESULT_ERROR) {
      final Throwable cropError = UCrop.getError(data);
    }
  }

  /**
   * 加载Uri到当前的头像中
   *
   * @param uri Uri
   */
  private void loadPortrait(Uri uri) {
    Glide.with(this)
        .load(uri)
        .asBitmap()
        .centerCrop()
        .into(mPortrait);

    // 拿到本地地址
    final String localpath = uri.getPath();
    Log.e("TAG", "localPath:" + localpath);

    Factory.runOnAsync(new Runnable() {
      @Override
      public void run() {
        String url = UploaderHelper.uploadPortrait(localpath);
        Log.e("TAG", "url:" + url);
      }
    });
  }
}
