package com.example.chatApp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatHandler extends TextWebSocketHandler {

	// 接続中のクライアントのセッションを管理するリスト
	private static List<WebSocketSession> sessions = new ArrayList<>();

	private final MessageService messageService;

	public ChatHandler(MessageService messageService) {
		this.messageService = messageService;
	}

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	/**
	 * WebSocket接続が確立したときに呼ばれるメソッド
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		if (session.getPrincipal() == null) {
			System.out.println("Unauthorized connection attempt");
			// 接続を拒否する
			session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Unauthorized"));
			return;
		}
		// Principalオブジェクトからユーザー名を取得
		String username = (String) session.getPrincipal().getName();
		// 動作確認用ログ
		System.out.println("Connection established for user: " + username);
		// セッションにユーザー名を追加
		session.getAttributes().put("username", username);

		sessions.add(session); // セッションをリストに追加

//		SpringSecurityで認証するためコメントアウトする
//		
//		// クリエパラメータや属性からトークンを取得
//		Map<String, Object> attributes = session.getAttributes();
//		String authToken = (String) attributes.get("authToken");

//		// トークンを検証
//		if (isAuthenticated(authToken)) {
//			// 認証に成功したらセッションを追加
//			sessions.add(session);
//			System.out.println("Authenticated connection established: " + session.getId());
//		} else {
//			// 認証に失敗したら接続を切断
//			System.out.println("Authentication failed for session: " + session.getId());
//			session.close();
//		}

	}

//		/**
//	 	* トークンを検証するメソッド
//	 	* 
//		 * @param authToken
//		 * @return
//		 */
//		private boolean isAuthenticated(String authToken) {
//			// トークンが"valid-token"の場合のみ認証成功
//			return "valid-token".equals(authToken);
//		}

	/**
	 * クライアントからのメッセージを受信したときに呼ばれるメソッド
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		String username = (String) session.getAttributes().get("username");
		String content = message.getPayload();
		// チェックでコンソールに受信したメッセージを表示する
		System.out.println(username + "send message: " + content);

		String timestamp = LocalDateTime.now().format(formatter);

		String link;

		if (content.startsWith("link:")) {
			link = content.substring(5);
			content = null;
		} else {
			link = null;
		}

		messageService.saveTextMessage(username, content, link);

		// JSONを生成
		JSONObject json = new JSONObject();
		json.put("sender", username);
		json.put("content", content);
		json.put("link", link);
		json.put("timestamp", timestamp);

		// JSON文字列を生成
		String jsonResponse = json.toString();
		// 動作確認用ログ
		System.out.println("Sending JSON: " + jsonResponse);

		// 全クライアントにJSONメッセージを送信
		for (WebSocketSession webSocketSession : sessions) {
			if (webSocketSession.isOpen()) {
				webSocketSession.sendMessage(new TextMessage(jsonResponse));
			}
		}
	}

	/**
	 * WebSocket接続が切断されたときに呼ばれるメソッド
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		System.out.println("Connection closed: " + session.getId());
	}

}
