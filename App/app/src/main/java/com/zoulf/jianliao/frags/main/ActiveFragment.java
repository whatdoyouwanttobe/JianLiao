package com.zoulf.jianliao.frags.main;


import butterknife.BindView;
import com.zoulf.common.app.MyFragment;
import com.zoulf.common.widget.GalleyView;
import com.zoulf.common.widget.GalleyView.SelectedChangeListener;
import com.zoulf.jianliao.R;

/**
 * @author Zoulf.
 */
public class ActiveFragment extends MyFragment {

  @BindView(R.id.galleryView)
  GalleyView mGallery;

  public ActiveFragment() {
    // Required empty public constructor
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.fragment_active;
  }

  @Override
  protected void initData() {
    super.initData();
    mGallery.setup(getLoaderManager(), new SelectedChangeListener() {
      @Override
      public void onSelectedCountChanged(int count) {

      }
    });
  }

}
