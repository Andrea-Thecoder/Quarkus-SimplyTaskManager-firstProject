package org.acme.dto.task;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.acme.utils.StringUtils;

@Getter
@Setter
public class TaskUpdateDTO {

    @Size(max = 40,message = "Title must be between 1 and 40 characters")
    private String title;

    @Size(min=1)
    private String description;

    private Boolean isComplete;

    public TaskUpdateDTO(){}

    public void setTitle(String title) {
        this.title = StringUtils.capitalizeOnlyFirstLetter(title);
    }
}
