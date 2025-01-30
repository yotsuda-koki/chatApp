package com.example.chatApp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	/**
	 * ChatApp用のSecurityFilterChain
	 * 
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable() // WebSocket接続を利用のためCSRFを無効
		// ).headers(headers -> headers.frameOptions(frame -> frame.disable()) //
		// 試用段階でiframeを許可
		).formLogin(login -> login.loginPage("/login") // ログインページのURL
				.defaultSuccessUrl("/chatroom", true) // ログイン成功時の遷移先
				.permitAll() // ログインページへのアクセスを許可
		).logout(logout -> logout.logoutUrl("/logout") // ログアウトURL
				.logoutSuccessUrl("/login") // ログアウト後の遷移先
				.invalidateHttpSession(true) // セッションを無効化
				.deleteCookies("JSESSIONID") // クッキー削除
				.permitAll() // ログアウトを全員許可
		).authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/register").permitAll() // ログイン・登録ページは全員許可
				.requestMatchers("/chatroom/**").authenticated() // "/chatroom"エンドポイントで認証を要求
		// ).authorizeHttpRequests(auth ->
		// auth.requestMatchers("/h2-console/**").permitAll() // 試用段階でh2コンソールを許可
		).authorizeHttpRequests(auth -> auth.anyRequest().authenticated() // その他のリクエストは認証が必要
		).sessionManagement(session -> session.maximumSessions(1) // 1ユーザーあたり1つのセッションのみ
				.maxSessionsPreventsLogin(true) // 既存セッションがあると新しいログインを拒否
		);

		return http.build();
	}

	/**
	 * パスワードのハッシュ化
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
