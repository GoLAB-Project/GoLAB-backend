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
@Table(name = "BAN_LIST")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BanList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BAN_IDX", nullable = false)
	private int banIdx;

	@Column(name = "USER_ID", nullable = false)
	private String userId;
	@Column(name = "BAN_ID", nullable = false)
	private String banId;

	public BanList(String userId, String banId) {
		this.userId = userId;
		this.banId = banId;
	}

}
