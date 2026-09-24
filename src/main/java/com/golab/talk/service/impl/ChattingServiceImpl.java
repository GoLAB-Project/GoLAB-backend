package com.golab.talk.service.impl;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.Chatting;
import com.golab.talk.dto.ChattingPageResponse;
import com.golab.talk.dto.ChattingResponseDto;
import com.golab.talk.repository.ChattingRepository;
import com.golab.talk.service.ChattingService;

@Service
public class ChattingServiceImpl implements ChattingService {

	@Autowired
	private ChattingRepository chattingRepository;

	private ChattingResponseDto convertToDto(Chatting chatting) {
		return ChattingResponseDto.builder()
			.id(chatting.getId())
			.roomId(chatting.getRoomId())
			.sendUserId(chatting.getSendUserId())
			.message(chatting.getMessage())
			.createdAt(chatting.getCreatedAt())
			.build();
	}

	@Override
	public ChattingPageResponse getChattingPage(int roomId, int cursor, int size) {
		Pageable pageable = PageRequest.of(0, size + 1, Sort.by(Sort.Direction.DESC, "id"));
		List<Chatting> list = chattingRepository.findByIdLessThanAndRoomIdOrderByIdDesc(cursor, roomId, pageable);

		boolean hasNext = list.size() > size;
		if (hasNext) {
			list = list.subList(0, size);
		}

		List<ChattingResponseDto> dtoList = new LinkedList<>();
		for (Chatting chatting : list) {
			dtoList.add(convertToDto(chatting));
		}

		Integer nextCursor = hasNext ? list.get(list.size() - 1).getId() : null;

		return ChattingPageResponse.builder()
			.chattingList(dtoList)
			.nextCursor(nextCursor)
			.hasNext(hasNext)
			.build();
	}

	@Override
	public Chatting saveChat(int roomId, int sendUserId, String message) {
		chattingRepository.insertByRoomId(roomId, sendUserId, message);
		// 저장 후 최신 1건 조회하여 반환
		Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
		List<Chatting> saved = chattingRepository.findByIdLessThanAndRoomIdOrderByIdDesc(Integer.MAX_VALUE, roomId, pageable);
		return saved.isEmpty() ? null : saved.get(0);
	}

}
