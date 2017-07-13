package com.zoulf.factory.data.group;

import com.zoulf.factory.model.card.GroupCard;
import com.zoulf.factory.model.card.GroupMemberCard;

/**
 * @author Zoulf.
 */

public interface GroupCenter {

  // 群卡片的处理
  void dispatch(GroupCard... cards);

  // 群成员的处理
  void dispatch(GroupMemberCard... cards);
}
