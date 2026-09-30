package com.ga.waslah.service;

import com.ga.waslah.model.UserProfile;
import com.ga.waslah.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
}
