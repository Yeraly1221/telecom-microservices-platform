package com.example.user.domain;

import com.example.user.dto.CreateProfileRequest;
import com.example.user.dto.UpdateProfileRequest;
import com.example.user.dto.UserProfileResponse;
import com.example.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserProfileResponse createUser(CreateProfileRequest  request) {

        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }

        User user = User.builder()
                .credentialId(request.credentials())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        User saved = userRepository.save(user);

        return new UserProfileResponse(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getCredentialId()
        );
    }

    @Transactional
    public UserProfileResponse findUserById(Long user_id) {
        if(user_id == null){
            throw new IllegalArgumentException("id cannot be null");
        }

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .credentialsId(user.getCredentialId())
                .build();
    }

    @Transactional
    public UserProfileResponse updateProfile(Long user_id, UpdateProfileRequest request) {
        if(user_id == null){
            throw new IllegalArgumentException("id cannot be null");
        }

        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }



        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        userRepository.save(user);

        return new UserProfileResponse(
                user_id,
                user.getFirstName(),
                user.getLastName(),
                user.getCredentialId());
    }

}
