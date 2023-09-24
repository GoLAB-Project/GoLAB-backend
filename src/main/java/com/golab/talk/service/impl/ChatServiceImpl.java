package com.golab.talk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.service.ChatService;

import io.github.flashvayne.chatgpt.service.ChatgptService;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatgptService chatgptService;

	@Override
	public String getChatResponse(String message) {
		return chatgptService.sendMessage(message);
	}
}
