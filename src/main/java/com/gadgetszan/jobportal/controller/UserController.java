package com.gadgetszan.jobportal.controller;

import com.gadgetszan.jobportal.Constants;
import com.gadgetszan.jobportal.dao.UserRepository;
import com.gadgetszan.jobportal.dao.impl.UserRepositoryImpl;
import com.gadgetszan.jobportal.exceptions.JpAuthException;
import com.gadgetszan.jobportal.model.User;
import com.gadgetszan.jobportal.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@RequestBody Map<String,Object> userMap){
      System.out.println("Welcome Register API");
      String firstName = (String) userMap.get("firstName");
      String lastName = (String) userMap.get("lastName");
      String email = (String) userMap.get("email");
      String password = (String) userMap.get("password");
      String userType = (String) userMap.get("userType");
      User user = userService.registerUser(firstName,lastName,email,password,userType);

      return new ResponseEntity<>(generateJWToken(user), HttpStatus.OK);
    };

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody Map<String,Object> userMap){
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(email,password);
        return new ResponseEntity<>(generateJWToken(user),HttpStatus.OK);
    };

    //Method to reset password or assigning a password replacement key
    @PutMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPass(HttpServletRequest request,
                                                          @RequestBody Map<String,Object> userMap,
                                                          User user)
    {
        try
        {
            String email = (String) userMap.get("email");
            userService.resetPassword(email,user);
            Map<String, String> map = new HashMap<>();
            map.put("Your reset key: ", UserRepositoryImpl.key);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch(Exception e)
        {
            throw new JpAuthException("An error has acquired");
        }

    }


    @PutMapping("/changePassword")
    public ResponseEntity<Map<String, Boolean>> changePass(HttpServletRequest request,
                                                         @RequestBody Map<String,Object> userMap)
    {

            //To validate user credentials/info
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");
            System.out.println(password);
            userService.validateUser(email,password);

            //Assigning a new password
            password = (String) userMap.get("newPassword");
            userService.changePassword(email,password);
            Map<String, Boolean> map = new HashMap<>();
            map.put("Changed password successfully ",true);
            return new ResponseEntity<>(map, HttpStatus.OK);

    }

    private Map<String,String> generateJWToken(User user){
      long timestamp = System.currentTimeMillis();
      String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
              .setIssuedAt(new Date(timestamp))
              .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
              .claim("userId", user.getUserId())
              .claim("email",user.getEmail())
              .claim("firstName",user.getFirstName())
              .claim("lastName",user.getLastName())
              .claim("userType",user.getUserType())
              .compact();
      Map<String,String> map = new HashMap<>();
      map.put("token",token);
      return map;
    };
}
