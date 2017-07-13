package com.zoulf.factory.data.message;

import com.zoulf.factory.model.card.MessageCard;

/**
 * @author Zoulf.
 */

public interface MessageCenter {
  void dispatch(MessageCard... cards);
}

