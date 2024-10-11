package org.acme.dto.task;
//quando creiamo un DTO dobbimamo inserire delle annotazioni per far si che faccia dei "controlli" di validazione durante la fase di passaggi odei dati.

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.acme.model.Task;
import org.acme.utils.StringUtils;


//DTO per la creazione di nuovi Task, da usare nel method POST del Task.
@Getter @Setter
public class TaskCreateDTO {

    @NotBlank(message = "title cannot be empty")
    @Size(max = 40,message = "title must be between 1 and 40 characters")
    private String title;

    @NotBlank(message = "description cannot be empty")
    private String description;

    public void setTitle(String title) {
        this.title = StringUtils.capitalizeOnlyFirstLetter(title);
    }

    public Task toEntity(){
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        return task;
    }
}