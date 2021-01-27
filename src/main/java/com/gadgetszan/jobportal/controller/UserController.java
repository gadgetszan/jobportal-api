package com.gadgetszan.jobportal.controller;

import com.gadgetszan.jobportal.Constants;
import com.gadgetszan.jobportal.model.User;
import com.gadgetszan.jobportal.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
