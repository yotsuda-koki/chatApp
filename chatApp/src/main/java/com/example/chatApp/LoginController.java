package com.example.chatApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	// SLF4Jを使用
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	private final UserService userService;

	/**
	 * UserServiceをインジェクション
	 * 
	 * @param userService
	 */
	public LoginController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * login.htmlにマッピング
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	/**
	 * register.htmlにマッピング
	 * 
	 * @return
	 */
	@GetMapping("/register")
	public String showRegisterPage() {
		return "register";
	}

	/**
	 * 成功したらログインページへリダイレクト、失敗したら再度登録ページへ
	 * 
	 * @param username
	 * @param password
	 * @param model
	 * @param userService
	 * @return
	 */
	@PostMapping("/register")
	public String registerUser(@RequestParam("username") String username, @RequestParam("password") String password,
			Model model) {
		try {
			userService.registerUser(username, password); // ユーザー登録
			return "redirect:/login";
		} catch (Exception e) {
			logger.error("ユーザー登録中にエラーが発生しました: {}", e.getMessage(), e); // エラーメッセージとスタックトレースを出力
			model.addAttribute("error", "登録に失敗しました。別のユーザー名をお試しください。"); // フロントエンドにエラーメッセージを渡す
			return "register";
		}
	}
}
