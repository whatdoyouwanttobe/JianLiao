package com.zoulf.web.jianliao.push.service;

import com.google.common.base.Strings;
import com.zoulf.web.jianliao.push.bean.api.base.ResponseModel;
import com.zoulf.web.jianliao.push.bean.api.user.UpdateInfoModel;
import com.zoulf.web.jianliao.push.bean.card.UserCard;
import com.zoulf.web.jianliao.push.bean.db.User;
import com.zoulf.web.jianliao.push.factory.UserFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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
  // 简化：关注人的操作其实是双方同时关注
  @PUT // 修改状态使用Put
  @Path("/follow/{followId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
    User self = getSelf();

    // 不能关注自己
    if (self.getId().equalsIgnoreCase(followId)
        || Strings.isNullOrEmpty(followId)) {
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

  // 获取某人的信息
  @GET
  @Path("{id}") // http://127.0.0.1/api/user/{id}
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
    if (Strings.isNullOrEmpty(id)) {
      // 返回参数异常
      return ResponseModel.buildParameterError();
    }

    User self = getSelf();
    if (self.getId().equalsIgnoreCase(id)) {
      // 返回自己，不必查询数据库
      return ResponseModel.buildOk(new UserCard(self, true));
    }

    User user = UserFactory.findById(id);
    if (user == null) {
      // 没找到用户
      return ResponseModel.buildNotFoundUserError(null);
    }

    // 如果我们直接有关注的记录，则我已关注需要查询信息的用户
    boolean isFollow = UserFactory.getUserFollow(self, user) != null;

    return ResponseModel.buildOk(new UserCard(user, isFollow));
  }

  // 搜索人的接口实现
  // 为了简化分页，只返回20条数据
  @GET
  // http://127.0.0.1/api/user/search/
  @Path("/search/{name:(.*)?}") // 名字为任意字符，可以为空
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) {
    User self = getSelf();

    // 先查询数据
    List<User> seachUsers = UserFactory.search(name);
    // 把查询的人封装为UserCard
    // 判断这些人是否有我已经关注的人
    // 如果有，则返回的关注状态中应该已经设置好状态

    // 拿出我的联系人
    final List<User> contacts = UserFactory.contacts(self);

    // 把User -> UserCard
    List<UserCard> userCards = seachUsers.stream()
        .map(user -> {
          // 判断这个人是否是否是我自己，或者是在我的联系人中
          boolean isFollow = user.getId().equalsIgnoreCase(user.getId())
              // 进行联系人的任意匹配，匹配其中的Id字段
              || contacts.stream()
              .anyMatch(contactUser -> contactUser.getId().equalsIgnoreCase(user.getId()));

          return new UserCard(user, isFollow);
        }).collect(Collectors.toList());
    // 返回
    return ResponseModel.buildOk(userCards);
  }

}

