package org.acme.task.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.task.dto.TaskCreateDTO;
import org.acme.task.dto.TaskResponseDTO;
import org.acme.task.dto.TaskUpdateDTO;
import org.acme.task.service.TaskService;

import java.util.List;

@Path("/task") //endpoint del controller!
@Produces(MediaType.APPLICATION_JSON) //indica che output(response) questo endpoint produrrà, in questo caso un json!
@Consumes(MediaType.APPLICATION_JSON) //indica che Input(request) questo controller accettà in questo caso json.

public class TaskController {

    @Inject
    TaskService taskService;

    @GET
    public List<TaskResponseDTO> getAllTask(){
        return this.taskService.getAllTask();
    }

    @GET
    @Path("/{id}")
    public TaskResponseDTO findById(
            @PathParam("id") long id
    ){
        return this.taskService.findById(id);
    }

    @GET
    @Path("/by-title")
    public TaskResponseDTO findByTitle(
            @QueryParam("title") String title
    ){
        return this.taskService.findByTitle(title);
    }

    @GET
    @Path("/find-completion")
    public List<TaskResponseDTO> findByCompletion(
            @QueryParam("complete") String complete
    ){
        return this.taskService.findByCompletion(complete);
    }

    @POST
    @Path("/add-task") // per aggiugnere  path ai method del controller bisogna , dopo il type di request (GET,POST etc) inserire nuovametne annotatio npath.
    public TaskResponseDTO createTask(
            @Valid TaskCreateDTO createDTO //annotation valid fa un validation check sul DTO usando i criteri del Type definito dopo valid.  Comprende automaticamente che deve prendere i dati dal Body della request, grazie al REST JAX-RS.
            //Nota di rischio: bisognerebbe implementare un filter per non far arrivare qui campie extra , evitando cosi possibil irischi per la sicurezza!
            ){
        return this.taskService.createTask(createDTO);
    }

    @PUT
    @Path("/update/{id}")
    public  TaskResponseDTO updateTask(
            @PathParam("id") long id,
            @Valid TaskUpdateDTO updateDTO
            ){
        return this.taskService.updateTask(id,updateDTO);
    }

    @DELETE
    @Path("/remove/{id}")
    public TaskResponseDTO deleteById(
            @PathParam("id") long id
    ){
        return this.taskService.deleteById(id);
    }




}
