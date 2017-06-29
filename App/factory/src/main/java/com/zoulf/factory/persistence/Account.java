package com.zoulf.factory.persistence;

/**
 * @author Zoulf.
 */

public class Account {

  // 设备的推送Id
  private static String pushId = "test";

  public static void setPushId(String pushId) {
    Account.pushId = pushId;
  }

  public static String getPushId() {
    return pushId;
  }
}
