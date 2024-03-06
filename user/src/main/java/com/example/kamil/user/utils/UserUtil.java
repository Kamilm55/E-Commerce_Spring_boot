package com.example.kamil.user.utils;

import com.example.kamil.user.exception.customExceptions.UserIsNotActiveException;
import com.example.kamil.user.model.entity.User;

public class UserUtil {
    public static void checkIsActive(User user){
        if (user.getIsActive().equals(false)) {
            throw new UserIsNotActiveException();
        }
    }
}
