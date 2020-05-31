package com.todo.web;

import com.todo.web.domain.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TaskService {
    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final WebClient webClient;

    public TaskService(Environment environment, WebClient.Builder webClientBuilder) {
        String tasksServiceServer = environment
                .getProperty("tasks.url", "http://localhost:8080");

        this.webClient = webClientBuilder
                .baseUrl(tasksServiceServer)
                .build();
    }

    public void save(Long userId, TaskDto task) {
        logger.debug("In create with {} {}", userId, task);
        this.webClient.post()
                .uri("/user/{userId}/tasks", userId)
                .body(BodyInserters.fromObject(task))
                .exchange().then()
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable))
                .subscribe();
        logger.debug("Out create with {}", userId);
    }

    public void updateStatus(Long userId, Long taskId) {
        logger.debug("In updateStatus with {} {}", userId, taskId);
        this.webClient.put()
                .uri("/user/{userId}/tasks/{taskId}/status", userId, taskId)
                .exchange().then()
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable))
                .subscribe();
        logger.debug("Out updateStatus with {}", userId);
    }

    public void remove(Long userId, Long taskId) {
        logger.debug("In remove with {} {}", userId, taskId);
        this.webClient.delete()
                .uri("/user/{userId}/tasks/{taskId}", userId, taskId)
                .exchange().then()
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable))
                .subscribe();
        logger.debug("Out remove with {} {}", userId, taskId);
    }

    public TaskDto[] find(Long userId) {
        logger.debug("In find with {}", userId);
        return this.webClient.get().uri("/user/{userId}/tasks", userId)
                .retrieve()
                .bodyToMono(TaskDto[].class)
                .doOnError(throwable -> logger.error(throwable.getMessage(), throwable))
                .block();
    }
}
