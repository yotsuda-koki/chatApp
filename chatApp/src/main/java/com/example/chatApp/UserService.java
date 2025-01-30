package com.example.chatApp;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/**
	 * UserRepositoryをインジェクション
	 * 
	 * @param userRepository
	 */
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * ユーザー登録する
	 * 
	 * @param username
	 * @param rawPassword
	 */
	@Transactional
	public void registerUser(String username, String rawPassword) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(rawPassword));
		userRepository.save(user);
	}

//	public User authenticate(String username, String password) {
//		User user = userRepository.findByUsername(username);
//		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//			return user;
//		}
//		return null;
//	}

	/**
	 * ユーザー認証する
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// データベースからユーザーを検索
		User user = userRepository.findByUsername(username);

		// ユーザーが見つからなければ例外を投げる
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		// Spring Security用のUserDetailsオブジェクトを構築して返す
		return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()) // ユーザー名
				.password(user.getPassword()) // 暗号化されたパスワード
				.authorities("USER") // ユーザーの権限
				.build();
	}

	/**
	 * ユーザーが存在するかチェックする
	 * 
	 * @param username
	 * @return
	 */
	public boolean userExists(String username) {
		return userRepository.findAll().stream().anyMatch(user -> user.getUsername().equals(username));
	}
}
