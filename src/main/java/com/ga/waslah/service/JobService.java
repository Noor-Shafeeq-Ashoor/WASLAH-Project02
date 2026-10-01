package com.ga.waslah.service;

import com.ga.waslah.dto.JobRequest;
import com.ga.waslah.dto.JobResponse;
import com.ga.waslah.exception.ResourceNotFoundException;
import com.ga.waslah.model.Job;
import com.ga.waslah.model.JobStatus;
import com.ga.waslah.model.User;
import com.ga.waslah.repository.JobRepository;
import com.ga.waslah.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(
            JobRepository jobRepository,
            UserRepository userRepository
    ) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public JobResponse createJob(String username, JobRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompanyName(request.getCompanyName());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSalary(request.getSalary());
        job.setDeadline(request.getDeadline());

        job.setStatus(JobStatus.OPEN);
        job.setCreatedBy(user);

        return toResponse(jobRepository.save(job));
    }

    public List<JobResponse> getAllJobs() {

        return jobRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public JobResponse getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found"));

        return toResponse(job);
    }

    public JobResponse updateJob(
            Long id,
            String username,
            JobRequest request
    ) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found"));

        if (!job.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to update this job");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompanyName(request.getCompanyName());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSalary(request.getSalary());
        job.setDeadline(request.getDeadline());

        return toResponse(jobRepository.save(job));
    }

    public void deleteJob(Long id, String username) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found"));

        if (!job.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to delete this job");
        }

        job.setStatus(JobStatus.CLOSED);

        jobRepository.save(job);
    }

    private JobResponse toResponse(Job job) {

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCompanyName(),
                job.getLocation(),
                job.getJobType(),
                job.getSalary(),
                job.getDeadline(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getCreatedBy().getUsername()
        );
    }
}