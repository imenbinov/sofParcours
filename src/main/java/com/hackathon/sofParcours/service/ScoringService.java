package com.hackathon.sofParcours.service;

import com.hackathon.sofParcours.model.Answer;
import com.hackathon.sofParcours.model.QuizHistory;
import com.hackathon.sofParcours.repository.AnswerRepository;
import com.hackathon.sofParcours.repository.QuizHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoringService {

    private final AnswerRepository answerRepository;
    private final QuizHistoryRepository quizHistoryRepository;
    private final BadgeService badgeService;

    public ScoringService(
            AnswerRepository answerRepository,
            QuizHistoryRepository quizHistoryRepository,
            BadgeService badgeService
    ) {
        this.answerRepository = answerRepository;
        this.quizHistoryRepository = quizHistoryRepository;
        this.badgeService = badgeService;
    }

    public Map<String, Object> calculateUserScore(String quizId, String userId) {
        List<Answer> answers = answerRepository.findByQuizIdAndUserId(quizId, userId);

        int totalScore = answers.stream()
                .mapToInt(Answer::getPointsEarned)
                .sum();

        long correctAnswers = answers.stream()
                .filter(Answer::isCorrect)
                .count();

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("totalScore", totalScore);
        result.put("correctAnswers", correctAnswers);
        result.put("totalQuestions", answers.size());

        return result;
    }

    public List<Map<String, Object>> getLeaderboard(String quizId) {
        List<Answer> allAnswers = answerRepository.findByQuizId(quizId);

        Map<String, Integer> userScores = new HashMap<>();
        Map<String, Long> userCorrectAnswers = new HashMap<>();

        allAnswers.forEach(answer -> {
            userScores.merge(answer.getUserId(), answer.getPointsEarned(), Integer::sum);
            if (answer.isCorrect()) {
                userCorrectAnswers.merge(answer.getUserId(), 1L, Long::sum);
            }
        });

        return userScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> userResult = new HashMap<>();
                    userResult.put("userId", entry.getKey());
                    userResult.put("totalScore", entry.getValue());
                    userResult.put("correctAnswers", userCorrectAnswers.getOrDefault(entry.getKey(), 0L));
                    return userResult;
                })
                .collect(Collectors.toList());
    }

    public QuizHistory saveQuizHistory(String userId, String quizId, String roomCode, int totalScore, int correctAnswers, int totalQuestions, int rank) {
        List<String> badgesEarned = badgeService.checkAndAwardBadges(userId, totalScore, correctAnswers);

        QuizHistory history = new QuizHistory();
        history.setUserId(userId);
        history.setQuizId(quizId);
        history.setRoomCode(roomCode);
        history.setTotalScore(totalScore);
        history.setCorrectAnswers(correctAnswers);
        history.setTotalQuestions(totalQuestions);
        history.setBadgesEarned(badgesEarned);
        history.setRank(rank);
        history.setCompletedAt(LocalDateTime.now());

        return quizHistoryRepository.save(history);
    }
}
