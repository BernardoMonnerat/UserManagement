package com.user_api.ebac1.Service;

import com.user_api.ebac1.Model.User;
import com.user_api.ebac1.Utils.LoggedUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final LoggedUser loggedUser;


    public UserService(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void addUser(User user){
        loggedUser.addUser(user);
    }
    public boolean validateUser(User user){
       return loggedUser.validateUser(user);
    }

    public boolean temporarySession(String email){
        return loggedUser.temporarySession(email);
    }
}
