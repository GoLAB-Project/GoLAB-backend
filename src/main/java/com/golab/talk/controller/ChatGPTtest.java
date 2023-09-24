package com.golab.talk.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.service.ChatService;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/chatGPT")
public class ChatGPTtest {
	private final ChatgptService chatgptService;
	private final ChatService chatService;

	@PostMapping("/test")
	public String test(@RequestBody String question) {
		return chatService.getChatResponse(question);
		//\n\nAs an AI language model, I don't have feelings, but I'm functioning well. Thank you for asking. How can I assist you today?
	}

}
