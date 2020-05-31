package com.todo.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDto {
    private Long id;
    private String description;
    private Boolean checked;
    private LocalDateTime lastUpdate;

    public TaskDto(String description) {
        this.description = description;
    }
}
