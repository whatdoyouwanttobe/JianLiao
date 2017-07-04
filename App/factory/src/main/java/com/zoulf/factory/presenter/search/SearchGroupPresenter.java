package com.zoulf.factory.presenter.search;

import com.zoulf.factory.presenter.BasePresenter;
import com.zoulf.factory.presenter.search.SearchContract.GroupView;

/**
 * 搜索群的逻辑实现
 *
 * @author Zoulf.
 */
public class SearchGroupPresenter extends BasePresenter<GroupView>
    implements SearchContract.Presenter {

  public SearchGroupPresenter(GroupView view) {
    super(view);
  }

  @Override
  public void search(String content) {

  }
}
