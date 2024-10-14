package com.example.goose.iGoose.auth.service;

import com.example.goose.common.jwt.JwtTokenProvider;
import com.example.goose.common.service.RefreshTokenService;
import com.example.goose.iGoose.auth.request.VerificationRequest;
import com.example.goose.iGoose.auth.response.VerificationResponse;
import com.example.goose.iGoose.auth.request.LoginRequest;
import com.example.goose.iGoose.auth.mapper.AuthMapper;
import com.example.goose.iGoose.auth.vo.UserVO;
import com.example.goose.iGoose.auth.vo.VerificationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Date;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final JavaMailSender mailSender;


    // ID 중복 체크

    /**
     * ID가 존재하면 true값 반환
     */
    public boolean isIdDuplicate(UserVO userVO) {
        return authMapper.countById(userVO.getId()) > 5;
    }

    /**
     * 중복된 이메일이 존재하면 true 값 반환
     */
    public boolean isEmailDuplicate(VerificationResponse verificationResponse) {
        return authMapper.countByEmail(verificationResponse.getMethod()) > 5;
    }

    // 회원가입 로직
    public ResponseEntity<?> signup(UserVO userVO) throws Exception {
        String verificationCode = generateVerificationCode();

        userVO.setUuid(UUID.randomUUID().toString());
        userVO.setPassword(passwordEncoder.encode(userVO.getPassword())); // 비밀번호 암호화
        authMapper.insertUser(userVO);


        return ResponseEntity.ok("회원가입 완료!");
    }

    /**
     * 이메일 발송 로직
     * @param verificationRequest
     * @return
     * @throws Exception
     */
    public ResponseEntity<?> sendEmail(VerificationRequest verificationRequest) throws Exception {
        String verificationCode = generateVerificationCode();
        VerificationVO response = new VerificationVO();

        VerificationVO existingRecord = authMapper.findByMethod(verificationRequest.getMethod());


        if (existingRecord == null) {
            response.setUuid(UUID.randomUUID().toString());
            response.setMethod(verificationRequest.getMethod());
            response.setCode(verificationCode);
            response.setIs_verified(false);


            sendVerificationEmail(verificationRequest.getMethod(), verificationCode);

            authMapper.emailVerification(response);

            return ResponseEntity.ok("첫 인증 메일 발송 완료");

        }
        existingRecord.setCode(verificationCode);
        authMapper.updateVerificationCode(existingRecord);

        sendVerificationEmail(verificationRequest.getMethod(), verificationCode);

        return ResponseEntity.ok("인증 메일 재전송 성공");
    }

    /**
     * 랜덤 코드 생성
     *
     * @return
     */
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /**
     * 이메일 인증 검사
     *
     * @param verificationRequest
     * @throws Exception
     */
    public void verifyEmail(VerificationRequest verificationRequest) throws Exception {
        VerificationVO verificationVO = authMapper.findByMethod(verificationRequest.getMethod());
        if (verificationVO == null) {
            throw new Exception("이메일 다름");
        }

        if (!verificationRequest.getCode().equals(verificationVO.getCode())) {
            throw new Exception("코드가 일치하지 않습니다.");
        }
        Date now = new Date();
        long diff = now.getTime() - verificationVO.getExpire().getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

        if (minutes > 3) {
            throw new Exception("만료된 코드 입니다.");
        }

        if (verificationVO.getIs_verified()) {
            authMapper.deleteVerification(verificationVO.getUuid());
        }

        authMapper.updateEmailVerified(verificationVO.getUuid());


    }

    // 로그인 로직
    public ResponseEntity<?> login(LoginRequest loginRequest) throws Exception {
        UserVO user = authMapper.findById(loginRequest.getId()); // 로그인할때 입력한 Id 값으로 유저 정보 검색

        // 암호화 한 password 비교
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            String accessToken = jwtTokenProvider.generateAccessToken(user.getUuid());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUuid());

            String encryptedToken = jwtTokenProvider.generateCombinedToken(accessToken, refreshToken);

            // refresh Token Redis에 저장
            long refreshTokenValidity = jwtTokenProvider.getRefreshTokenValidity();
            refreshTokenService.storeRefreshToken(refreshToken, refreshTokenValidity);
            // uuid와 refreshToken, 만료 기간 저장

            return ResponseEntity.ok()
                    .header("Authorization", encryptedToken)
                    .body("로그인 성공");
        } else {
            return ResponseEntity.status(401).body("로그인 실패");
        }
    }

    // 로그아웃 로직
    public void logout(String refreshToken) throws Exception {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new Exception("유효하지 않은 refreshToken");
        }

        String uuid = jwtTokenProvider.getUserIdFromToken(refreshToken);

        String storedRefreshToken = refreshTokenService.getRefreshToken(uuid);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new Exception("저장된 Refresh Token과 일치하지 않습니다.");
        }

        refreshTokenService.deleteRefreshToken(uuid);

    }


    /**
     * 인증 메일 전송 로직
     *
     * @param email
     * @param verification
     */
    public void sendVerificationEmail(String email, String verification) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address: " + email);
        }
        String subject = "이메일 인증";
        String message = "이메일 인증 코드 : " + verification + ". 인증 코드 만료시간은 3분 입니다.";

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        mailSender.send(emailMessage);
    }


}
