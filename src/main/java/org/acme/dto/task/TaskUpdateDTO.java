package org.acme.dto.task;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.acme.utils.StringUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {

    @Size(min = 1, max = 40,message = "Title must be between 1 and 40 characters")
    private String title;

    @Size(min=1)
    private String description;

    private Boolean isComplete;

    public void setTitle(String title) {
        this.title = StringUtils.capitalizeOnlyFirstLetter(title);
    }
}
