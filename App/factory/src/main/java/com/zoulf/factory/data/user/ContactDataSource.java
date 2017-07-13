package com.zoulf.factory.data.user;

import com.zoulf.factory.data.DataSource;
import com.zoulf.factory.model.db.User;
import java.util.List;

/**
 * 联系人数据源
 *
 * @author Zoulf
 * @version 1.0.0
 */
public interface ContactDataSource {

  /**
   * 对数据加载的一个职责
   * @param callback 返回一个加载成功的回调
   */
  void load(DataSource.SucceedCallback<List<User>> callback);

  /**
   * 销毁操作
   */
  void dispose();
}
