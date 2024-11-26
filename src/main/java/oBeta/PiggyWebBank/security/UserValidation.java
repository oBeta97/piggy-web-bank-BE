package oBeta.PiggyWebBank.security;

import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.NotFoundException;
import oBeta.PiggyWebBank.interfaces.UsersEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserValidation {

    public <T> void validateUser(User loggedUser, UsersEntity usersEntity, Class<T> entityType, long entityId ){
        if(!loggedUser.getId().equals(usersEntity.getUserId()))
            throw new NotFoundException(entityType.getSimpleName() + " with id " + entityId + " not found!");
    }

}