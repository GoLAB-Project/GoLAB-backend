package com.golab.talk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "GAME_ROOM")
@Getter
@Setter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GAME_ROOM_ID", nullable = false)
	private int gameRoomId;

	@Column(name = "Game_Room_Type", nullable = false)
	private int gameRoomType;

	@Column(name = "Max_Participants", nullable = false, columnDefinition = "int default 3")
	private int maxParticipants;

	@Column(name = "Game_Room_Name")
	private String gameRoomName;

	@Column(name = "Game_Room_PW")
	private String gameRoomPW;

	@Column(name = "ROOM_STATUS", nullable = false, columnDefinition = "int default 0")
	private int roomStatus;


}
