package com.gadgetszan.jobportal.dao;

import com.gadgetszan.jobportal.exceptions.JpBadRequestException;
import com.gadgetszan.jobportal.exceptions.JpResourceNotFoundException;
import com.gadgetszan.jobportal.model.Job;

import java.util.List;

public interface JobRepository {
    List<Job> findAll(Integer userId)throws JpResourceNotFoundException;
    Job findById(Integer userId,Integer jobId)throws JpResourceNotFoundException;
    Integer create(Integer userId,String jobTitle,String jobDescription)throws JpBadRequestException;
    void update(Integer userId,Integer jobId,Job job)throws JpBadRequestException;


}
