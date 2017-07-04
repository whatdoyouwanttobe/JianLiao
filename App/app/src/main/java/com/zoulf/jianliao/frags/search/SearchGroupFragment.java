package com.zoulf.jianliao.frags.search;


import android.support.v4.app.Fragment;
import com.zoulf.common.app.MyFragment;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.activities.SearchActivity.SearchFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends MyFragment
    implements SearchFragment{


  public SearchGroupFragment() {
    // Required empty public constructor
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.fragment_search_group;
  }

  @Override
  public void search(String content) {

  }
}
