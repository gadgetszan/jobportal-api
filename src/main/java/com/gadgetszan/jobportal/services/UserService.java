package com.gadgetszan.jobportal.services;

import com.gadgetszan.jobportal.exceptions.JpAuthException;
import com.gadgetszan.jobportal.model.User;

public interface UserService {
    User validateUser(String email,String password) throws JpAuthException;

    User registerUser(String firstName,String lastName,String email,String password, String userType) throws JpAuthException;

    void resetPassword(String email, User user) throws JpAuthException;

    void changePassword(String email, String password) throws JpAuthException;

}
