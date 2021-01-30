package com.gadgetszan.jobportal.dao.impl;

import com.gadgetszan.jobportal.dao.JobRepository;
import com.gadgetszan.jobportal.exceptions.JpBadRequestException;
import com.gadgetszan.jobportal.exceptions.JpResourceNotFoundException;
import com.gadgetszan.jobportal.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
@Repository
public class JobRepositoryImpl implements JobRepository {
    private static final String SQL_CREATE="INSERT INTO JP_JOBS(JOB_ID,USER_ID,JOB_TITLE,JOB_DESCRIPTION) VALUES(NEXTVAL('JP_JOBS_SEQ'),?,?,?)";
    private static final String SQL_FIND_BY_ID="SELECT JOB_ID,USER_ID,JOB_TITLE,JOB_DESCRIPTION FROM JP_JOBS WHERE USER_ID=? AND JOB_ID=?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Job> findAll(Integer userId) throws JpResourceNotFoundException {
        return null;
    }

    @Override
    public Job findById(Integer userId, Integer jobId) throws JpResourceNotFoundException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,new Object[]{userId, jobId},JobRowMapper);

        }catch (Exception e){
            throw new JpResourceNotFoundException("Category Not Found ");
        }
    }


    @Override
    public Integer create(Integer userId, String jobTitle, String jobDescription) throws JpBadRequestException {
        try{
            KeyHolder keyHolder=new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps=connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,userId);
                ps.setString(2,jobTitle);
                ps.setString(3,jobDescription);
                return ps;
            },keyHolder);
            return (Integer) keyHolder.getKeys().get("JOB_ID");

        }catch(Exception e){
            throw new JpBadRequestException("Invalid Request");

        }
    }

    @Override
    public void update(Integer userId, Integer jobId, Job job) throws JpBadRequestException {

    }
    private RowMapper<Job> JobRowMapper=((rs, rowNum)->{
        return new Job(rs.getInt("JOB_ID"),
                rs.getInt("USER_ID"),
                rs.getString("JOB_TITLE"),
                rs.getString("JOB_DESCRIPTION"));
    });
}
