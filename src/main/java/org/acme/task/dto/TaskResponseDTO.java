package org.acme.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TaskResponseDTO {


    @NotNull(message = "id cannot be null")
    @Positive(message = "id must be positive!")
    private long id;


    @NotNull(message = "title cannot be null!")
    @NotBlank(message = "title cannot be empty")
    @Size(min = 1, max = 40,message = "title must be between 1 and 40 characters")
    private String title;


    @NotNull(message = "description cannot be null!")
    @NotBlank(message = "description cannot be empty")
    private String description;


    @NotNull(message = "createAt cannot be null!")
    private LocalDateTime createAt;


    @NotNull(message = "updateAt cannot be null!")
    private LocalDateTime updateAt;

    @NotNull(message = "isComplete cannot be null!")
    private boolean isComplete;

    public TaskResponseDTO(long id, String title, String description, LocalDateTime createAt, LocalDateTime updateAt, boolean isComplete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.isComplete = isComplete;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt( LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public void setComplete(boolean complete){
        this.isComplete=complete;
    }

    @JsonProperty("isComplete")
    public boolean isComplete() {
        return isComplete;
    }
}


