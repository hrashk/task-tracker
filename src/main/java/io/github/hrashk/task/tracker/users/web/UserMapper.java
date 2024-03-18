package io.github.hrashk.task.tracker.users.web;

import io.github.hrashk.task.tracker.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    public abstract UserModel map(User user);

    public abstract User map(UserModel model);
}
