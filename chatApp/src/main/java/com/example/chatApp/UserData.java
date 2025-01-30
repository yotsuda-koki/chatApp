package com.example.chatApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserData implements CommandLineRunner {

	private final UserService userService;

	public UserData(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run(String... args) throws Exception {
		registerIfNotExists("user1", "password");
		registerIfNotExists("user2", "password");
		registerIfNotExists("user3", "password");
	}

	// ユーザーが存在しない場合のみ登録を行うヘルパーメソッド
	private void registerIfNotExists(String username, String password) {
		if (userService.userExists(username)) {
			System.out.println("User " + username + " already exists. Skipping registration.");
		} else {
			try {
				userService.registerUser(username, password);
			} catch (Exception e) {
				System.err.println("Error during user registration for " + username + ": " + e.getMessage());
			}
		}
	}
}
