package org.acme.task.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.task.dto.TaskCreateDTO;
import org.acme.task.dto.TaskResponseDTO;
import org.acme.task.dto.TaskUpdateDTO;
import org.acme.task.service.TaskService;
import org.acme.utils.response.SuccessResponse;
import org.acme.validation.ValidId;
import org.acme.validation.ValidTitle;

import java.util.List;

@Path("/task") //endpoint del controller!
@Produces(MediaType.APPLICATION_JSON) //indica che output(response) questo endpoint produrrà, in questo caso un json!
@Consumes(MediaType.APPLICATION_JSON) //indica che Input(request) questo controller accettà in questo caso json.

public class TaskResource {

    @Inject
    TaskService taskService;

    @GET
    public SuccessResponse<List<TaskResponseDTO>> getAllTask(){
        return new SuccessResponse<> (this.taskService.getAllTask());
    }

    @GET
    @Path("/{id}")
    public SuccessResponse<TaskResponseDTO> findById(
            @PathParam("id") @ValidId long id
    ){
        return new SuccessResponse<>(this.taskService.findById(id));
    }

    @GET
    @Path("/by-title")
    public SuccessResponse<TaskResponseDTO> findByTitle(
            @QueryParam("title")@ValidTitle(maxLength = 40) String title
    ){
        return new SuccessResponse<>(this.taskService.findByTitle(title));
    }

    @GET
    @Path("/by-completion")
    public SuccessResponse<List<TaskResponseDTO>> findByCompletion(
            @QueryParam("complete") String complete
    ){
        return new SuccessResponse<> (this.taskService.findByCompletion(complete));
    }

    @POST
    @Path("/add-task") // per aggiugnere  path ai method del controller bisogna , dopo il type di request (GET,POST etc) inserire nuovametne annotatio npath.
    public SuccessResponse<TaskResponseDTO> createTask(
            @Valid TaskCreateDTO createDTO //annotation valid fa un validation check sul DTO usando i criteri del Type definito dopo valid.  Comprende automaticamente che deve prendere i dati dal Body della request, grazie al REST JAX-RS.
            //Nota di rischio: bisognerebbe implementare un filter per non far arrivare qui campie extra , evitando cosi possibil irischi per la sicurezza!
            ){
        return new SuccessResponse<> (this.taskService.createTask(createDTO));
    }

        @PUT
        @Path("/update/{id}")
        public  SuccessResponse<TaskResponseDTO> updateTask(
                @PathParam("id") @ValidId long id,
                @Valid TaskUpdateDTO updateDTO
                ){
            return new SuccessResponse<> (this.taskService.updateTask(id,updateDTO));
        }

    @DELETE
    @Path("/remove/{id}")
    public SuccessResponse<TaskResponseDTO> deleteById(
            @PathParam("id") @ValidId long id
    ){
        return new SuccessResponse<> (this.taskService.deleteById(id));
    }




}
