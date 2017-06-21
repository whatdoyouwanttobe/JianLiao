package com.zoulf.jianliao;

import android.widget.TextView;
import butterknife.BindView;
import com.zoulf.common.app.MyActivity;

public class MainActivity extends MyActivity {

  @BindView(R.id.tv_test)
  TextView mTestText;

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_main;
  }

  @Override
  protected void initWidget() {
    super.initWidget();
    mTestText.setText("Test Hello!");
  }
}
