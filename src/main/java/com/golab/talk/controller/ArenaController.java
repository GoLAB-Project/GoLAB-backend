package com.golab.talk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.dto.ArenaMatchResponse;
import com.golab.talk.dto.ArenaSpeakRequest;
import com.golab.talk.service.ArenaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/arena")
@CrossOrigin
public class ArenaController {

	private final ArenaService arenaService;

	@PostMapping("/start")
	public ResponseEntity<ArenaMatchResponse> start() {
		return ResponseEntity.ok(arenaService.start());
	}

	@GetMapping("/{matchId}")
	public ResponseEntity<ArenaMatchResponse> get(@PathVariable String matchId) {
		try {
			return ResponseEntity.ok(arenaService.get(matchId));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{matchId}/speak")
	public ResponseEntity<ArenaMatchResponse> speak(
		@PathVariable String matchId,
		@RequestBody(required = false) ArenaSpeakRequest request
	) {
		try {
			String text = request == null ? "" : request.getText();
			return ResponseEntity.ok(arenaService.speak(matchId, text));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
