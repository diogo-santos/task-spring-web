package com.todo.web;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
class TaskForm implements Serializable {
    @NotEmpty
    @Size(max = 50)
    private String description;
}
