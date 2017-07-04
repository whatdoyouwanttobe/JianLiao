package com.zoulf.factory.presenter.contract;

import android.support.annotation.StringRes;
import com.zoulf.factory.data.DataSource;
import com.zoulf.factory.data.helper.UserHelper;
import com.zoulf.factory.model.card.UserCard;
import com.zoulf.factory.presenter.BasePresenter;
import com.zoulf.factory.presenter.contract.FollowContract.View;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 关注的逻辑实现
 *
 * @author Zoulf.
 */
public class FollowPresenter extends BasePresenter<FollowContract.View>
    implements FollowContract.Presenter, DataSource.Callback<UserCard> {

  public FollowPresenter(View view) {
    super(view);
  }

  @Override
  public void follow(String id) {
    start();
    UserHelper.follow(id, this);
  }

  @Override
  public void onDataLoaded(final UserCard userCard) {
    final FollowContract.View view = getView();
    if (view != null) {
      Run.onUiAsync(new Action() {
        @Override
        public void call() {
          view.onFollowSucceed(userCard);
        }
      });
    }
  }

  @Override
  public void onDataNotAvailableLoaded(@StringRes final int strRes) {
    final FollowContract.View view = getView();
    if (view != null) {
      Run.onUiAsync(new Action() {
        @Override
        public void call() {
          view.showError(strRes);
        }
      });
    }
  }
}
