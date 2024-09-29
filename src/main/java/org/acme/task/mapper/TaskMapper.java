package org.acme.task.mapper;


// il mapper serve per convertire il model (entity) in DTO e viceversa. In questo modo non si espongono i dati dell'entity direttamente . Andrebbe fatto a mano ma per ora suamo una libreria esterna!

import org.acme.task.dto.TaskCreateDTO;
import org.acme.task.dto.TaskResponseDTO;
import org.acme.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi") //serve per  far si che si integri col CDI di quarkus!
public interface TaskMapper {

    //questa sriga di codice si usa quando non è presente un CDI
    //TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class); // Instanza del mapper

    //converte il DTO in Task, valido per il create
    Task toEntityCreate (TaskCreateDTO taskCreateDTO);

    //converte task in DTO response
    TaskResponseDTO toDtoResponse(Task task);
    //converte listTask in dto response List
    List<TaskResponseDTO> toDtoListResponse(List<Task> task);




}
