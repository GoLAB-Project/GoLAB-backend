package com.golab.talk.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.service.ChatService;
import com.golab.talk.service.KeywordService;

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

	@Autowired
	private KeywordService keywordService;

	//question을 받아서 질문 리스트를 return
	@PostMapping("/test")
	public String test(@RequestBody String question) {
		return chatService.getChatResponse(question);
	}

	//@Scheduled(cron = "0 50 23 * * *") //매일 23시 50분에 실행
	@GetMapping("/keyword")
	public ResponseEntity<List<String>> getTodayTopic() {
		System.out.println("오늘의 키워드를 생성합니다." + LocalDateTime.now());
		log.info("오늘의 키워드를 생성합니다." + LocalDateTime.now());

		List<String> list = keywordService.findAllKeyword();

		for (String keyword : list) {
			String question = "우리가 대한민국 10대들을 대상으로 1~3분 정도 되는 토론 게임을 하려고 해. 그 때 \""
				+ keyword + "\"에 대한 짧은 주제를 10가지 제시해줘. 그리고 다른 말은 하지 말고 주제만 번호를 달아서 알려줘";
			String result = chatService.getChatResponse(question);
			
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

}
