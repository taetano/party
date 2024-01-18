package com.example.party.service;

import com.example.party.common.JwtToken;
import com.example.party.common.ApiResponse;
import com.example.party.exception.LoginException;
import com.example.party.redis.RedisService;
import com.example.party.dto.request.LoginCommand;
import com.example.party.dto.request.SignupRequest;
import com.example.party.entity.User;
import com.example.party.exception.EmailOverlapException;
import com.example.party.exception.UserNotFoundException;
import com.example.party.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RedisService redisService;

    public ApiResponse signUp(SignupRequest signupRequest) {
        Boolean existsUser = userRepository.existsUserByEmail(signupRequest.getEmail());
        if (existsUser) {
            throw new EmailOverlapException();
        }
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User newUser = User.createUser(signupRequest, encodedPassword);

        userRepository.save(newUser);
        return ApiResponse.create("회원가입 완료");
    }

    public JwtToken login(LoginCommand loginCommand) {
        User user = userRepository.findByEmail(loginCommand.getEmail())
                .orElseThrow(LoginException::new);
        if (!passwordEncoder.matches(loginCommand.getPassword(), user.getPassword())) {
            throw new LoginException();
        }

        if (redisService.existsRefreshToken(user.getId())) {
            System.out.println("중복 로그인");
            redisService.deleteRefreshToken(user.getId());
            throw new LoginException();
        }

        JwtToken token = JwtToken.of(user.getId(), user.getEmail());
        redisService.putRefreshToken(user.getId(), token.getRefreshToken());

        return token;
    }

    public ApiResponse logout(Long userId) {
        redisService.deleteRefreshToken(userId);
        return ApiResponse.ok("로그아웃 완료");
    }

    public ApiResponse withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.setDormant();
        return ApiResponse.ok("회원탈퇴 완료");
    }

}
