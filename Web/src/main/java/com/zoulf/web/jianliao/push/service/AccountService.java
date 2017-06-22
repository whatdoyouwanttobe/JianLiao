package com.zoulf.web.jianliao.push.service;

import com.zoulf.web.jianliao.push.bean.db.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author  Zoulf
 */
// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService {

  // GET 127.0.0.1/api/account/login
  @GET
  @Path("/login")
  public String get() {
    return "you get the login.";
  }

  // POST 127.0.0.1/api/account/login
  @POST
  @Path("/login")
  // 指定请求与返回的响应体为JSON
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public User post() {
    User user = new User();
    user.setName("Zoulf");
    user.setSex(1);
    return user;
  }
}
