package com.zoulf.factory.data.user;

/**
 * @author Zoulf.
 */

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction.QueryResultListCallback;
import com.zoulf.factory.data.DataSource.SucceedCallback;
import com.zoulf.factory.data.helper.DbHelper;
import com.zoulf.factory.model.db.User;
import com.zoulf.factory.model.db.User_Table;
import com.zoulf.factory.persistence.Account;
import java.util.LinkedList;
import java.util.List;

/**
 * 联系人仓库
 *
 * @author Zoulf
 * @version 1.0.0
 */
public class ContactRepository
    implements ContactDataSource,
    QueryResultListCallback<User>,
    DbHelper.ChangedListener<User> {

  // Set虽然是不重复的，但是是无序的，Map遍历消耗内存大，又因为有频繁的替换操作，所以LinkedList
  List<User> users = new LinkedList<>();
  private SucceedCallback<List<User>> callback;

  @Override
  public void load(SucceedCallback<List<User>> callback) {
    this.callback = callback;
    // 对数据辅助工具类添加一个数据更新的监听
    DbHelper.addChangedListener(User.class, this);
    // 加载本地数据库
    SQLite.select()
        .from(User.class)
        .where(User_Table.isFollow.eq(true))
        .and(User_Table.id.notEq(Account.getUserId()))
        .orderBy(User_Table.name, true)
        .limit(100)
        .async()
        .queryListResultCallback(this)
        .execute();
  }

  @Override
  public void dispose() {
    this.callback = null;
    // 取消对数据集合的监听
    DbHelper.removeChangedListener(User.class, this);
  }

  @Override
  public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
    // 数据库加载成功
    // 因为有@NotNull标识，所以不需要判断是否为空
    if (tResult.size() == 0) {
      users.clear();
      notifyDataChange();
      return;
    }

    // 转变为数组
    User[] users = tResult.toArray(new User[0]);
    // 回到数据集更新的操作中
    onDataSave(users);
  }

  @Override
  public void onDataSave(User... list) {
    boolean isChanged = false;
    // 当数据库数据变更的操作

    for (User user : list) {
      // 是关注的人，同时不是自己，即联系人
      if (isRequired(user)) {
        insertOrUpdate(user);
        isChanged = true;
      }
    }

    // 有数据变更，则进行界面刷新
    if (isChanged) {
      notifyDataChange();
    }
  }

  @Override
  public void onDataDelete(User... list) {
    boolean isChanged = false;
    // 当数据库数据删除的操作

    for (User user : list) {
      if (users.remove(user)) {
        isChanged = true;
      }
    }

    // 有数据变更，则进行界面刷新
    if (isChanged) {
      notifyDataChange();
    }
  }

  private void insertOrUpdate(User user) {
    int index = indexOf(user);
    if (index >= 0) {
      replace(index, user);
    } else {
      insert(user);
    }
  }

  // 替换，先移除指定index下的User，然后再在该位置下添加User
  private void replace(int index, User user) {
    users.remove(index);
    users.add(index, user);
  }

  // 添加
  private void insert(User user) {
    users.add(user);
  }

  /**
   * 判断数据是否已经存在于List中，通过User id 判断
   *
   * @param user User
   * @return -1 不存在，存在就返回index
   */
  private int indexOf(User user) {
    int index = -1;

    for (User user1 : users) {
      index++;
      if (user1.isSame(user)) {
        return index;
      }
    }
    return -1;
  }

  private void notifyDataChange() {
    if (callback != null) {
      callback.onDataLoaded(users);
    }
  }

  /**
   * 检查一个User是否是我需要关注的数据，即过滤操作
   *
   * @param user User
   * @return True 是我需要关注的数据
   */
  private boolean isRequired(User user) {
    return user.isFollow() && !user.getId().equals(Account.getUserId());
  }
}

