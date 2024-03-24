package io.github.hrashk.task.tracker.tasks.web;

import io.github.hrashk.task.tracker.tasks.Task;
import io.github.hrashk.task.tracker.users.web.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public abstract class TaskMapper {
    public abstract TaskModel map(Task task);

    public abstract Task map(TaskModel task);
}
