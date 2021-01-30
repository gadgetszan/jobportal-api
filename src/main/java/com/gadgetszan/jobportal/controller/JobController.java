package com.gadgetszan.jobportal.controller;

import com.gadgetszan.jobportal.model.Job;
import com.gadgetszan.jobportal.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/jobs")
public class JobController {
    @Autowired
    JobService jobService;

    @PostMapping("")
    public ResponseEntity<Job> addCategory(HttpServletRequest request,
                                           @RequestBody Map<String, Object>jobMap) {
        int userId = (Integer) request.getAttribute("userId");
        String jobTitle = (String) jobMap.get("jobTitle");
        String jobDescription = (String) jobMap.get("jobDescription");

        Job job = jobService.addJobCategory(userId, jobTitle, jobDescription);


        return new ResponseEntity<>(job, HttpStatus.CREATED);

    }
}
