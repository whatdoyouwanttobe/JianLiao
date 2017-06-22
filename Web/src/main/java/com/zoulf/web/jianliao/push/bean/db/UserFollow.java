package com.zoulf.web.jianliao.push.bean.db;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 用户关系的Model
 * 用于用户直接进行好友关系的实现
 *
 * @author Zoulf
 */
@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {

  @Id
  @PrimaryKeyJoinColumn
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(updatable = false, nullable = false)
  private String id;

  // 定义一个发起人，你关注某人，这里就是你
  // 多对一 -> 你可以关注很多人，你的每一次关注都是一条记录
  // 你可以创建很多个关注的信息，所以是多对一
  // 这里的多对一是 User ： 多个UserFollow
  // optional 不可选，必须存储，一条关注记录一定要有一个"你"
  @ManyToOne(optional = false)
  // 定义关联的表字段名为 originId ,对应的是 User.id
  // 定义的是数据库中的存储字段
  @JoinColumn(name = "originId")
  private User origin;
  // 把这个列提取到我们的Model中，不允许为null，更新和插入。
  @Column(nullable = false, updatable = false, insertable = false)
  private String originId;

  // 定义关注的目标，你关注的人
  // 也是多对一，你可以被很多人关注，每一次关注都是一条记录
  // 即多个 Follow : 一个 User 的情况
  @ManyToOne(optional = false)
  // 定义关联的表字段名为 targetId ,对应的是 User.id
  // 定义的是数据库中的存储字段
  @JoinColumn(name = "targetId")
  private User target;
  // 把这个列提取到我们的Model中，不允许为null，更新和插入。
  @Column(nullable = false, updatable = false, insertable = false)
  private String targetId;

  // 别名，也就是对 target 的备注，可以为 Null
  @Column
  private String alias;

  // 定义为创建时间戳，在创建时就已经写入
  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createAt = LocalDateTime.now();

  // 定义为更新时间戳，在创建时就已经写入
  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updateAt = LocalDateTime.now();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public User getOrigin() {
    return origin;
  }

  public void setOrigin(User origin) {
    this.origin = origin;
  }

  public String getOriginId() {
    return originId;
  }

  public void setOriginId(String originId) {
    this.originId = originId;
  }

  public User getTarget() {
    return target;
  }

  public void setTarget(User target) {
    this.target = target;
  }

  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public LocalDateTime getCreateAt() {
    return createAt;
  }

  public void setCreateAt(LocalDateTime createAt) {
    this.createAt = createAt;
  }

  public LocalDateTime getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(LocalDateTime updateAt) {
    this.updateAt = updateAt;
  }
}
