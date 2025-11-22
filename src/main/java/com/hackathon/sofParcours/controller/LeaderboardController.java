package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.model.QuizHistory;
import com.hackathon.sofParcours.repository.QuizHistoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leaderboard")
@Tag(name = "Classement", description = "Classements globaux et par room")
public class LeaderboardController {

    private final QuizHistoryRepository quizHistoryRepository;

    public LeaderboardController(QuizHistoryRepository quizHistoryRepository) {
        this.quizHistoryRepository = quizHistoryRepository;
    }

    @Operation(summary = "Obtenir le classement global (paginé)")
    @GetMapping("/global")
    public ResponseEntity<Map<String, Object>> getGlobalLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "totalScore"));
        List<QuizHistory> histories = quizHistoryRepository.findAll();

        // Agrégation des scores par utilisateur
        Map<String, Integer> userScores = histories.stream()
                .collect(Collectors.groupingBy(
                        QuizHistory::getUserId,
                        Collectors.summingInt(QuizHistory::getTotalScore)
                ));

        List<Map<String, Object>> leaderboard = userScores.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .skip((long) page * size)
                .limit(size)
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("userId", entry.getKey());
                    item.put("totalScore", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("leaderboard", leaderboard);
        response.put("page", page);
        response.put("size", size);
        response.put("totalUsers", userScores.size());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtenir le classement d'une room")
    @GetMapping("/room/{roomCode}")
    public ResponseEntity<List<Map<String, Object>>> getRoomLeaderboard(
            @PathVariable String roomCode
    ) {
        List<QuizHistory> histories = quizHistoryRepository.findByRoomCode(roomCode);

        Map<String, Integer> userScores = histories.stream()
                .collect(Collectors.groupingBy(
                        QuizHistory::getUserId,
                        Collectors.summingInt(QuizHistory::getTotalScore)
                ));

        List<Map<String, Object>> leaderboard = userScores.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("userId", entry.getKey());
                    item.put("totalScore", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(leaderboard);
    }
}
