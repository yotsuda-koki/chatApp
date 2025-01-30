package com.example.chatApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

	@GetMapping("/chatroom")
	public String chatroom() {
		return "chatroom";
	}

}
