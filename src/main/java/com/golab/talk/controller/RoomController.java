package com.golab.talk.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.domain.Message;
import com.golab.talk.dto.CreateRoomRequestDto;
import com.golab.talk.dto.CreateRoomResponseDto;
import com.golab.talk.dto.RoomListResponseDto;
import com.golab.talk.service.ChatService;

@RestController
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private ChatService chatService;

	@PostMapping("/create")
	public ResponseEntity<CreateRoomResponseDto> createRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
		CreateRoomResponseDto createRoomResponseDto = null;

		// findRoom = findByIdentifier()

		/* true -> findRoomInfo = findRoomInfo() userId/roomId
			CreateRoomResponse
				room_id: findRoom.id,
				identifier: findRoom.identifier,
				type: findRoom.type,
				room_name: findRoomInfo!.room_name,
				not_read_chat: findRoomInfo!.not_read_chat,
				last_read_chat_id: findRoomInfo!.last_read_chat_id,
				last_chat: findRoom.last_chat,
				updatedAt: findRoom.updatedAt
		 */

		/* false

			1. createRoom -> 생성과 동시에 객체를 반환
			2. createParticipant
			3. 2(~N)명의 참여자 정보를 통해, 생성자가 아닌 참가자들의 createParticipant
			4. CreateRoomResponse

		 */

		if (createRoomResponseDto != null) {
			return new ResponseEntity<>(createRoomResponseDto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<List<Message>> loadChat(@PathVariable int roomId) {
		List<Message> list = chatService.loadChat(roomId);
		/*
		attributes: [
			'id',
			'room_id',
			'send_user_id',
			'message',
			'not_read',
			'createdAt'
		  ], where: {
			id: { [Sequelize.Op.lt]: cursor },
			room_id
		  },
		  order: [['id', 'DESC']],
		  limit: 50
		*/
		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/list/{userId}")
	public ResponseEntity<ArrayList<RoomListResponseDto>> loadRoomList(@PathVariable int userId) {
		ArrayList<RoomListResponseDto> list = chatService.loadRoomList(userId);
		/*
			attributes: [
			'room_id',
			'room_name',
			'not_read_chat',
			'last_read_chat_id'
		  ],
		  include: [
			{
			  model: Room,
			  attributes: ['identifier', 'type', 'last_chat', 'updatedAt'],
			  required: true,
			  on: Sequelize.where(
				Sequelize.col('Participant.room_id'),
				'=',
				Sequelize.col('Room.id')
			  ),
			  where: {
				last_chat: {
				  [Sequelize.Op.ne]: ''
				}
			  }
			}
		  ]
		*/

		/*
		 * 1. ArrayList<RoomListResponse> 생성
		 * 2. roomRow를 만들어 합침
		 * 3. 최종 반환
		 * */

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
