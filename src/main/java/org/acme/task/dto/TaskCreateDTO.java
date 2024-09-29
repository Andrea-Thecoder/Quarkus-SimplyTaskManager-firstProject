package org.acme.task.dto;
//quando creiamo un DTO dobbimam oisnerire delle annotazioni  per far si che faccia dei "controlli" di validazione durante la fase di passaggi odei dati.


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//DTO per la creazione di nuovi Task, da usare nel method POST del Task.
public class TaskCreateDTO {

    @NotNull(message = "title cannot be null!")
    @NotBlank(message = "title cannot be empty")
    @Size(min = 1, max = 40,message = "title must be between 1 and 40 characters")
    private String title;

    @NotNull(message = "description cannot be null!")
    @NotBlank(message = "description cannot be empty")
    private String description;


    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description=description;
    }

}
