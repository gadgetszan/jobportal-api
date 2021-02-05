package com.gadgetszan.jobportal.services.impl;

import com.gadgetszan.jobportal.dao.UserRepository;
import com.gadgetszan.jobportal.exceptions.JpAuthException;
import com.gadgetszan.jobportal.model.User;
import com.gadgetszan.jobportal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws JpAuthException {
        if(email !=null) email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email,password);
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password, String userType) throws JpAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(email !=null) email = email.toLowerCase();
        if(!pattern.matcher(email).matches()) throw new JpAuthException("Invalid email format");
        Integer count = userRepository.getCountByEmail(email);
        if(count > 0)
            throw new JpAuthException("Email already in use");
        Integer userId = userRepository.create(firstName,lastName,email,password,userType);
        return userRepository.findById(userId);
    }

    @Override
    public void resetPassword(String email, User user) throws JpAuthException
    {
        userRepository.assignResetKey(email,user);
    }

    @Override
    public void changePassword(String email, String password) throws JpAuthException
    {
        userRepository.assigningNewPass(email,password);
    }
}
