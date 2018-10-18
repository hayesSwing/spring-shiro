package com.xiangzi.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xiangzi.Global;

@Controller(Global.APP_SIGN + "IndexController")
@RequestMapping(Global.ADMIN_PATH)
public class IndexController extends BaseAdminController {
	
	@RequestMapping(value = "index")
	public String index(Model model, HttpServletRequest request) {
		String info = "进入后台主页面";
		logger.debug(info);
		
		return "admin/index";
	}
	
}
