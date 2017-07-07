package com.zoulf.factory.presenter.contract;

import com.zoulf.factory.model.db.User;
import com.zoulf.factory.presenter.BaseContract;

/**
 * @author Zoulf.
 */

public interface ContactContract {

  // 什么都不需要额外定义，开始就是调用start即可
  interface Presenter extends BaseContract.Presenter {

  }

  // 都在基类完成了
  interface View extends BaseContract.RecyclerView<Presenter, User> {

  }

}
