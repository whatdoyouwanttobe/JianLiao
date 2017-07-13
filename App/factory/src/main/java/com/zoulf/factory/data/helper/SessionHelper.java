package com.zoulf.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.zoulf.factory.model.db.Session;
import com.zoulf.factory.model.db.Session_Table;

/**
 * 会话辅助工具类
 *
 * @author Zoulf
 * @version 1.0.0
 */
public class SessionHelper {

  // 从本地查询Session
  public static Session findFromLocal(String id) {
    return SQLite.select()
        .from(Session.class)
        .where(Session_Table.id.eq(id))
        .querySingle();
  }
}

