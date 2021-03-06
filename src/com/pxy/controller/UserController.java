package com.pxy.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pxy.chat.H5ServletServerSocket;
import com.pxy.entity.User;
import com.pxy.service.UserService;

/**
 * @author pxy
 *
 */
@Controller
public class UserController {
	@Resource
	UserService userService;
	
	/**
	 * 用户前往注册页面时的action
	 * @param model
	 * @return Register.jsp
	 */
	@RequestMapping("/register")
	public String register(Model model){
		model.addAttribute("user", new User());
		return "Register";
	}
	
	/**
	 * 用户前往登录页面时的action
	 * @param model
	 * @return Login.jsp
	 */
	@RequestMapping("/login")
	public String login(Model model){
		model.addAttribute("user", new User());
		return "Login";
	}
	
	/**
	 * 用户提交注册信息时的action
	 * @param model 
	 * @param user
	 * @return Login.jsp或Register.jsp
	 */
	@RequestMapping("/save_user")// 向前台页面传的值放入model中
	public String saveUser(Model model,@ModelAttribute("user") User user){
		String username = user.getUsername();
		String password = user.getPassword();
		user.setUsername(username);
		user.setPassword(password);
		if (userService.addUser(user)!=0) {
			model.addAttribute("user", user);
			return "redirect:/login";
		}else {
			String error = "用户名已被注册！";
			model.addAttribute("error", error);
			return "Register";
		}
		
	}
	
	/**
	 * 用户提交登录信息时的action
	 * @param model
	 * @param user
	 * @return Login.jsp或Chat.jsp
	 */
	
	@RequestMapping("/check_user")
	public String checkUser(Model model,@ModelAttribute("user") User user){
		boolean check = userService.checkUser(user.getUsername(), user.getPassword());
		
		if(check){
			model.addAttribute("user", user);
			int onlineCount = H5ServletServerSocket.getOnlineCount();
			
			model.addAttribute("onlineCount", onlineCount);
			
			return "Chat";
		}else {
			String error = "用户名或密码错误！";
			model.addAttribute("error",error);
			return "Login";
		}
		
	}
	
	/*@RequestMapping("/chat")
	public String chat(Model model,@ModelAttribute("user") User user){
		//H5ServletServerSocket h5ServletServerSocket = new H5ServletServerSocket();
		int onlineCount = H5ServletServerSocket.getOnlineCount();
		String messages = H5ServletServerSocket.getMessage(user.getUsername());
		model.addAttribute("onlineCount", onlineCount);
		model.addAttribute("messages", messages);
		return "Chat";
	}
	*/
}
