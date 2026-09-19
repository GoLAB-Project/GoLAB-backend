package com.golab.talk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.golab.talk.service.GPTService;

import io.github.flashvayne.chatgpt.service.ChatgptService;

@Service
public class GPTServiceImpl implements GPTService {

	private final ChatgptService chatgptService;
	private final String apiKey;

	public GPTServiceImpl(
		@Autowired(required = false) ChatgptService chatgptService,
		@Value("${chatgpt.api-key:}") String apiKey
	) {
		this.chatgptService = chatgptService;
		this.apiKey = apiKey;
	}

	@Override
	public String getChatResponse(String message) {
		if (chatgptService == null || apiKey == null || apiKey.isBlank()) {
			return null;
		}
		try {
			return chatgptService.sendMessage(message);
		} catch (Exception e) {
			return null;
		}
	}
}
