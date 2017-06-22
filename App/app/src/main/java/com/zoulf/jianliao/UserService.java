package com.zoulf.jianliao;

/**
 * Created by Administrator on 2017/6/22.
 */

public class UserService implements IUserService {

  @Override
  public String search(int hashCode) {
    return "User" + hashCode;
  }
}
