package org.acme.api;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.BaseSearchRequest;
import org.acme.dto.PageResult;
import org.acme.dto.response.SuccessResponse;
import org.acme.dto.task.TaskAdminResponseDTO;
import org.acme.dto.task.TaskCreateDTO;
import org.acme.dto.task.TaskResponseDTO;
import org.acme.dto.task.TaskUpdateDTO;
import org.acme.service.TaskService;
import org.acme.validation.ValidId;

@Path("/tasks") //endpoint del controller!
@Produces(MediaType.APPLICATION_JSON) //indica che output(response) questo endpoint produrrà, in questo caso un json!
@Authenticated
public class TaskResource {

    @Inject
    TaskService taskService;


    @GET
    //@RolesAllowed("Admin")
    public SuccessResponse<PageResult<TaskAdminResponseDTO>> findTasks(
            @BeanParam @Valid BaseSearchRequest request
            ){
        return new SuccessResponse<>(taskService.findTasks(request,null));
    }

    @GET
    @Path("/incomplete")
    public SuccessResponse<PageResult<TaskAdminResponseDTO>> findTaskInComplete(
            @BeanParam @Valid BaseSearchRequest request
    ){
        return new SuccessResponse<>(taskService.findTasks(request,false));
    }

    @GET
    @Path("/complete")
    public SuccessResponse<PageResult<TaskAdminResponseDTO>> findTaskComplete(
            @BeanParam @Valid BaseSearchRequest request
    ){
        return new SuccessResponse<>(taskService.findTasks(request,true));
    }

    @GET
    @Path("/{id}")
        public SuccessResponse<TaskResponseDTO> getById(
                @PathParam("id") @ValidId long id
        ){
        return new SuccessResponse<>(taskService.getTaskById(id));
        }

    @POST
    @Consumes(MediaType.APPLICATION_JSON) //indica che Input(request) questo controller accettà in questo caso json.
    public SuccessResponse<TaskResponseDTO> createTask(
            @Valid TaskCreateDTO createDTO
            ){
        return new SuccessResponse<>(taskService.createTask(createDTO));
        }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public SuccessResponse<TaskResponseDTO> updateTask(
                @PathParam("id")@ValidId long id,
                @Valid TaskUpdateDTO updateDTO
                ){
        return new SuccessResponse<>(taskService.updateTask(id,updateDTO));
        }

    @PUT
    @Path("/{id}/complete")
    public SuccessResponse<TaskResponseDTO> updateTaskComplete(
            @PathParam("id") @ValidId long id
    ){
        return new SuccessResponse<>(taskService.flagTaskComplete(id));
    }

    @PUT
    @Path("/{id}/incomplete")
    public  SuccessResponse<TaskResponseDTO> updateTaskInComplete(
            @PathParam("id") @ValidId long id
    ){
        return new SuccessResponse<>(taskService.flagTaskInComplete(id));
    }

    @DELETE
    @Path("/{id}")
    public SuccessResponse<TaskResponseDTO> deleteTask(
            @PathParam("id") long id
        ){
        return new SuccessResponse<>(taskService.deleteTask(id));
        }


}
