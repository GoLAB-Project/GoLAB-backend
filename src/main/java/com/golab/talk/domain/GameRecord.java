package com.golab.talk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "GAME_RECORD")
@Getter
@Setter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GameRecord {

	@Id
	@Column(name = "USER_ID", nullable = false)
	private String userId;

	@Column(name = "GAMES", nullable = false)
	private int games;
	@Column(name = "WINS", nullable = false)
	private int wins;
	@Column(name = "MMR", nullable = false)
	private int mmr;

}
