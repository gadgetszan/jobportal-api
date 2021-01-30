package com.gadgetszan.jobportal.services;
import com.gadgetszan.jobportal.exceptions.JpBadRequestException;
import com.gadgetszan.jobportal.exceptions.JpResourceNotFoundException;
import com.gadgetszan.jobportal.model.Job;

import java.util.List;

public interface JobService {

List<Job> fetchAllJobCategory(Integer userId);
Job fetchAllJobById(Integer userId,Integer jobId) throws JpResourceNotFoundException;
Job addJobCategory(Integer userId,String jobTitle,String jobDescription)throws JpBadRequestException;
void updateJobCategory(Integer usId,Integer jobId, Job job)throws  JpBadRequestException;
}
