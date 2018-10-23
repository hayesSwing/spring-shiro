package com.xiangzi.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xiangzi.Global;

/**
 * 登录入口
 */
@Controller(Global.APP_SIGN + "AccountController")
@RequestMapping(Global.ADMIN_PATH + "/account")
public class AccountController extends BaseAdminController {

	/**
	 * 用户登录页面
	 */
	@RequestMapping(value = "login")
	public String login(String userName, Model model, HttpServletRequest request) {
		logger.debug("用户登录页面");

		return "admin/login";
	}

}
