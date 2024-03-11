package io.github.hrashk.task.tracker.tasks.web;

import io.github.hrashk.task.tracker.tasks.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TaskMapper {
    public abstract TaskModel map(Task task);
}
