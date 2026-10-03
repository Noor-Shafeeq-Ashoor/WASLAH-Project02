package com.ga.waslah.service;

import com.ga.waslah.dto.EmployerRequestRequest;
import com.ga.waslah.exception.ConflictException;
import com.ga.waslah.exception.ResourceNotFoundException;
import com.ga.waslah.model.EmployerRequest;
import com.ga.waslah.model.EmployerRequestStatus;
import com.ga.waslah.model.User;
import com.ga.waslah.repository.EmployerRequestRepository;
import com.ga.waslah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployerRequestService {

    private final EmployerRequestRepository employerRequestRepository;
    private final UserRepository userRepository;

    public EmployerRequest createRequest(
            String username,
            EmployerRequestRequest request
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (employerRequestRepository.findByUserUsername(username).isPresent()) {
            throw new ConflictException("Employer request already exists");
        }

        EmployerRequest employerRequest = new EmployerRequest();

        employerRequest.setCompanyName(request.getCompanyName());
        employerRequest.setCompanyDescription(request.getCompanyDescription());
        employerRequest.setJobTypes(request.getJobTypes());
        employerRequest.setCommercialRegistration(
                request.getCommercialRegistration()
        );
        employerRequest.setStatus(EmployerRequestStatus.PENDING);
        employerRequest.setUser(user);

        return employerRequestRepository.save(employerRequest);
    }

    public EmployerRequest getMyRequest(String username) {

        return employerRequestRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employer request not found"
                        ));
    }
}