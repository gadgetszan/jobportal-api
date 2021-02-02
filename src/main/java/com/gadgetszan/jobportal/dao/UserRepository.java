package com.gadgetszan.jobportal.dao;

import com.gadgetszan.jobportal.exceptions.JpAuthException;
import com.gadgetszan.jobportal.model.User;

public interface UserRepository {
    Integer create(String firstName,String lastName,String email,String password,String userType)
            throws JpAuthException;
    User findByEmailAndPassword(String email, String password) throws JpAuthException;
    Integer getCountByEmail(String email);
    User findById(Integer userId);
    void reset(Integer userId, User user) throws JpAuthException;
}
