package oBeta.PiggyWebBank.interfaces;

import oBeta.PiggyWebBank.entities.User;

import java.util.UUID;

public interface UsersEntity {

    User getUser();

    default UUID getUserId(){
        if (this.getUser() == null)
            return null;

        return this.getUser().getId();
    };
}
