package com.zoulf.factory.presenter.contract;

import com.zoulf.factory.model.card.UserCard;
import com.zoulf.factory.presenter.BaseContract;

/**
 * 关注的接口定义
 *
 * @author Zoulf.
 */
public class FollowContract {

  // 任务调度者
  public interface Presenter extends BaseContract.Presenter {

    // 关注一个人
    void follow(String id);
  }

  public interface View extends BaseContract.View<Presenter> {

    // 成功的情况下返回用户的信息
    void onFollowSucceed(UserCard userCard);
  }

}
