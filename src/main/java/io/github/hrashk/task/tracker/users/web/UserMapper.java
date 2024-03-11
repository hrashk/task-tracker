package io.github.hrashk.task.tracker.users.web;

import io.github.hrashk.task.tracker.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    abstract UserModel map(User user);
}
