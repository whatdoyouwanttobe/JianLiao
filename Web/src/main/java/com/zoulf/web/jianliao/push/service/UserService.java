package com.zoulf.web.jianliao.push.service;

import com.zoulf.web.jianliao.push.bean.api.base.ResponseModel;
import com.zoulf.web.jianliao.push.bean.api.user.UpdateInfoModel;
import com.zoulf.web.jianliao.push.bean.card.UserCard;
import com.zoulf.web.jianliao.push.bean.db.User;
import com.zoulf.web.jianliao.push.factory.UserFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Zoulf
 */

// 127.0.0.1/api/user/...
@Path("/user")
public class UserService extends BaseService {

  // 用户信息修改接口
  // 返回自己的个人信息
  @PUT
  //@Path("") //127.0.0.1/api/user 不需要写，就是当前目录
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseModel<UserCard> update(UpdateInfoModel model) {
    if (!UpdateInfoModel.check(model)) {
      return ResponseModel.buildParameterError();
    }

    User self = getSelf();
    // 更新用户信息
    self = model.updateToUser(self);
    self = UserFactory.update(self);
    // 构架自己的用户信息
    UserCard card = new UserCard(self, true);
    // 返回
    return ResponseModel.buildOk(card);
  }

  // 拉取联系人
  @GET
  @Path("/contact")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseModel<List<UserCard>> contact() {
    User self = getSelf();
    List<User> users = UserFactory.contacts(self);

    // 转换为UserCard
    List<UserCard> userCards = users.stream()
        .map(user -> {
          return new UserCard(user, true);
        })// map 操作，相当于转置操作，将User -> UserCard
        .collect(Collectors.toList());

    // 返回
    return ResponseModel.buildOk(userCards);
  }

  // 关注人
  @PUT // 修改状态使用Put
  @Path("/follow/{followId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
    User self = getSelf();

    // 不能关注自己
    if (self.getId().equalsIgnoreCase(followId)) {
      return ResponseModel.buildParameterError();
    }

    // 找到我要关注的人
    User followUser = UserFactory.findById(followId);

    if (followUser == null) {
      // 未找到人
      return ResponseModel.buildNotFoundUserError(null);
    }

    // 备注默认为空
    followUser = UserFactory.follow(self, followUser, null);

    if (followUser == null) {
      // 关注失败，返回服务器异常
      return ResponseModel.buildServiceError();
    }

    // TODO 通知我关注的人我关注了他

    // 返回关注的人的信息
    return ResponseModel.buildOk(new UserCard(followUser, true));
  }

}

