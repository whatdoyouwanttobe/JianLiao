package com.zoulf.factory.data.user;

import com.zoulf.factory.model.card.UserCard;

/**
 * 用户中心的基本定义
 * @author Zoulf.
 */

public interface UserCenter {

  // 分发处理一堆用户卡片的信息，并更新到数据库
  void dispatch(UserCard... cards);
}
