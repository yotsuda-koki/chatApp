package com.example.chatApp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class MessageService {

	private final MessageRepository messageRepository;

	/**
	 * MessageRepositoryをインジェクション
	 * 
	 * @param messageRepository
	 */
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Transactional
	public Message saveTextMessage(String sender, String content, String link) {
		Message message = new Message(sender, content, link, LocalDateTime.now());
		return messageRepository.save(message);
	}

	public List<Message> getAllMessages() {
		return messageRepository.findAll();
	}
}
