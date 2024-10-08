package org.acme.task.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class TaskUpdateDTO {



    @Size(min = 1,max = 40,message = "Title must be between 1 and 40 characters")
    private String title;

    @Size(min=1)
    private String description;


    private Boolean isComplete;

    public TaskUpdateDTO(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        this.isComplete = complete;
    }
}
