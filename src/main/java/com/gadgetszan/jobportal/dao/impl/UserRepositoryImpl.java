package com.gadgetszan.jobportal.dao.impl;

import com.gadgetszan.jobportal.dao.UserRepository;
import com.gadgetszan.jobportal.exceptions.JpAuthException;
import com.gadgetszan.jobportal.exceptions.JpBadRequestException;
import com.gadgetszan.jobportal.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.Random;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String SQL_CREATE = "INSERT INTO JP_USERS(USER_ID,FIRST_NAME,LAST_NAME," +
            "EMAIL,PASSWORD,USER_TYPE) VALUES(NEXTVAL('JP_USERS_SEQ'),?,?,?,?,?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT (*) FROM JP_USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID ="SELECT USER_ID, FIRST_NAME,LAST_NAME,EMAIL,PASSWORD,USER_TYPE " +
            "FROM JP_USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME,LAST_NAME,EMAIL,PASSWORD,USER_TYPE " +
            "FROM JP_USERS WHERE EMAIL = ?";

    private static final String SQL_UPDATE_PASSWORD = "UPDATE JP_USERS SET PASSWORD = ? WHERE EMAIL = ?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    public static String key = "";
    @Override
    public Integer create(String firstName, String lastName, String email, String password, String userType) throws JpAuthException {
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt(10));
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection ->{
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,firstName);
                ps.setString(2,lastName);
                ps.setString(3,email);
                ps.setString(4,hashedPassword);
                ps.setString(5,userType);
                return ps;
            },keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");
        } catch (Exception e){
            System.out.println(e);
            throw new JpAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws JpAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper);
            if (!BCrypt.checkpw(password, user.getPassword()))
                if (!password.equals(user.getPassword()))
                    throw new JpAuthException("Invalid email/password");
            return user;
        } catch(EmptyResultDataAccessException e){
            throw new JpAuthException("Invalid email/password");
        }
    };

    @Override
    public Integer getCountByEmail(String email) {
         return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email},Integer.class);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,new Object[]{userId},userRowMapper);
    }

    @Override
    public void assignResetKey(String email, User user)
    {
        key = generateRandomKey();
        String hashedPassword = BCrypt.hashpw(key, BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        String pass = user.getPassword();
        System.out.println(pass);
        try {
            jdbcTemplate.update(SQL_UPDATE_PASSWORD, new Object[]{hashedPassword,email});
        }catch (Exception e) {
            throw new JpBadRequestException("Invalid request");
        }
    }

    @Override
    public void assigningNewPass(String email, String password)
    {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {

            jdbcTemplate.update(SQL_UPDATE_PASSWORD, new Object[]{hashedPassword,email});
        }catch (Exception e) {
            throw new JpBadRequestException("Invalid request");
        }
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) ->{
        return new User(rs.getInt("USER_ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                rs.getString("USER_TYPE"));
    });


    public static String generateRandomKey()
    {
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 7;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphabet.length());

            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String randomString = sb.toString();
        return randomString;

    }
}
