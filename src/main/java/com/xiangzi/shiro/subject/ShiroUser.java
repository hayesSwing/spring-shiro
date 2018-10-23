package com.xiangzi.shiro.subject;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {

	private static final long serialVersionUID = 7685225341921490468L;

	private static Logger logger = LoggerFactory.getLogger(ShiroUser.class);

	/**
	 * 用户ID
	 */
	private String id;

	/**
	 * 租户ID
	 */
	private String tenantId;

	/**
	 * 登录名称
	 */
	private String userName;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 用户姓名
	 */
	private String realName;

	public ShiroUser(String id, String tenantId, String userName, String nickName, String realName) {
		super();
		this.id = id;
		this.tenantId = tenantId;
		this.userName = userName;
		this.nickName = nickName;
		this.realName = realName;
	}

	public String getId() {
		return id;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getUserName() {
		return userName;
	}

	public String getNickName() {
		return nickName;
	}

	public String getRealName() {
		return realName;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return this.tenantId + "@" + this.userName;
	}

	/**
	 * 重载hashCode,只计算loginName;
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * 重载equals,只计算username;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShiroUser other = (ShiroUser) obj;
		return (userName == null ? other.userName == null : userName.equals(other.userName));
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数. 标注了 @PostConstruct 注释的方法将在类实例化后调用 标注 @PostConstruct
	 * 注释的方法都会在初始化被执行。
	 */
	@PostConstruct
	public void postConstruct() {
		logger.debug("=========== postConstruct ===========");
	}

	/**
	 * 标注了 @PreDestroy 的方法将在类销毁之前调用。 标注 @PreDestroy 注释的方法都会在销毁时被执行。
	 */
	@PreDestroy
	public void preDestroy() {
		logger.debug("=========== preDestroy ===========");
	}

}
