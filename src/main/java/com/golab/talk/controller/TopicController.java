package com.golab.talk.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.dto.KeywordDto;
import com.golab.talk.dto.TopicDto;
import com.golab.talk.service.GPTService;
import com.golab.talk.service.KeywordService;
import com.golab.talk.service.TopicService;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/topic")
public class TopicController {
	private final ChatgptService chatgptService;
	private final GPTService GPTService;

	@Autowired
	private KeywordService keywordService;

	@Autowired
	private TopicService topicService;

	//question을 받아서 질문 리스트를 return
	@PostMapping("/test")
	public String test(@RequestBody String question) {
		return GPTService.getChatResponse(question);
	}

	//@Scheduled(cron = "0 50 23 * * *") //매일 23시 50분에 실행
	@PostMapping("/today")
	public ResponseEntity<String> getTodayTopic() {
		System.out.println("오늘의 주제를 생성합니다." + LocalDateTime.now());
		log.info("오늘의 주제를 생성합니다." + LocalDateTime.now());

		List<KeywordDto> list = keywordService.findAll();
		List<TopicDto> resultTopicList = new ArrayList<>();

		int topicListNum = 3;
		int topicNum = 1;

		for (KeywordDto keywordDto : list) {
			String keyword = keywordDto.getKeyName();
			int keyId = keywordDto.getKeyId();
			String question = "대한민국 10대들을 대상으로 1~3분 정도 되는 토론 게임을 하려고 해. 그 때 \""
				+ keyword + "\"에 대한 짧은 주제를 " + topicListNum + "가지 제시해줘. 그리고 다른 말은 하지 말고 주제의 뒤에는 꼭\"$\"을 넣고 말해줄래?";
			String result = GPTService.getChatResponse(question);

			for (int j = 1; j <= topicListNum; j++) {
				String target = j + ". ";
				int target_num = result.indexOf(target);

				String numTopic = result.substring(target_num + 3,
					(result.substring(target_num + 3).indexOf("$") + target_num + 3));
				resultTopicList.add(new TopicDto(topicNum, keyId, numTopic));
				topicNum++;
			}
		}
		int totalTopicNum = topicListNum * list.size();

		if (resultTopicList.size() == totalTopicNum) {
			topicService.saveAll(resultTopicList);
			return new ResponseEntity<>("오늘의 주제 " + totalTopicNum + "개 저장했습니다.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("오늘의 주제를 저장에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}

	}

}
