package com.gadgetszan.jobportal.services.impl;


import com.gadgetszan.jobportal.dao.JobRepository;
import com.gadgetszan.jobportal.exceptions.JpBadRequestException;
import com.gadgetszan.jobportal.exceptions.JpResourceNotFoundException;
import com.gadgetszan.jobportal.model.Job;
import com.gadgetszan.jobportal.services.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class JobServiceImpl implements JobService {
@Autowired
    JobRepository jobRepository;

    @Override
    public List<Job> fetchAllJobCategory(Integer userId) {
        return null;
    }

    @Override
    public Job fetchAllJobById(Integer userId, Integer jobId) throws JpResourceNotFoundException {
        return jobRepository.findById(userId,jobId );
    }

    @Override
    public Job addJobCategory(Integer userId, String jobTitle, String jobDescription) throws JpBadRequestException {
        int jobId=jobRepository.create(userId, jobTitle, jobDescription);
        return jobRepository.findById(userId,jobId);
    }

    @Override
    public void updateJobCategory(Integer usId, Integer jobId, Job job) throws JpBadRequestException {

    }
}

