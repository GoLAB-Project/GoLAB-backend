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
import lombok.ToString;

@Entity
@Table(name = "BAN")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Ban {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private int id;

	@Column(name = "MY_ID", nullable = false)
	private int myId;
	@Column(name = "BAN_ID", nullable = false)
	private int banId;
	@Column(name = "BAN_NAME", nullable = false)
	private String banName;
	// @Column(name = "CREATED_AT", nullable = false)
	// private LocalDateTime createdAt;
	// @Column(name = "UPDATED_AT", nullable = false)
	// private LocalDateTime updatedAt;

	public Ban(int myId, int banId, String banName) {
		this.myId = myId;
		this.banId = banId;
		this.banName = banName;
	}

}
