package com.iGoose.Kok.iGoose.auth.service;

import com.iGoose.Kok.common.jwt.JwtTokenProvider;
import com.iGoose.Kok.common.service.RefreshTokenService;
import com.iGoose.Kok.iGoose.auth.request.VerificationRequest;
import com.iGoose.Kok.iGoose.auth.response.VerificationResponse;
import com.iGoose.Kok.iGoose.auth.request.LoginRequest;
import com.iGoose.Kok.iGoose.auth.mapper.AuthMapper;
import com.iGoose.Kok.iGoose.auth.response.VerificationUserInfoResponse;
import com.iGoose.Kok.iGoose.auth.vo.UserVO;
import com.iGoose.Kok.iGoose.auth.vo.VerificationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageService imageService;


    // ID 중복 체크

    /**
     * ID가 존재하면 true값 반환
     */
    public boolean isIdDuplicate(UserVO userVO) {
        return authMapper.countById(userVO.getUser_name()) > 0;
    }

    /**
     * 중복된 이메일이 존재하면 true 값 반환
     */
    public boolean isEmailDuplicate(VerificationResponse verificationResponse) {
        return authMapper.countByEmail(verificationResponse.getMethod()) > 5;
    }

    // 회원가입 로직
    public void signup(UserVO userVO, MultipartFile file) throws Exception {

        userVO.setUuid(UUID.randomUUID().toString());
        userVO.setPassword(passwordEncoder.encode(userVO.getPassword())); // 비밀번호 암호화

        if (file != null && !file.isEmpty()) {
            String imageUrl = imageService.saveImage(file);
            userVO.setProfile(imageUrl);
        }
        authMapper.insertUser(userVO);
    }

    /**
     * 이메일 발송 로직
     *
     * @param verificationRequest
     * @return
     * @throws Exception
     */
    public ResponseEntity<?> sendLoginEmail(VerificationRequest verificationRequest) throws Exception {
        UserVO existingRecord = authMapper.findByEmail(verificationRequest.getMethod());
        VerificationVO verificationVO = authMapper.findByMethod(verificationRequest.getMethod());

        if (existingRecord != null && verificationVO == null) {
            return handleEmailSending(verificationRequest.getMethod(),true);
        } else if (verificationVO != null) {
            return handleEmailSending(verificationRequest.getMethod(),false);
        } else {
            return ResponseEntity.ok("회원가입으로 진행");
        }
    }

    public ResponseEntity<?> sendSingUpEmail(VerificationRequest verificationRequest) throws Exception {
        UserVO existingRecord = authMapper.findByEmail(verificationRequest.getMethod());
        VerificationVO verificationVO = authMapper.findByMethod(verificationRequest.getMethod());

        if (existingRecord == null && verificationVO == null) {
            return handleEmailSending(verificationRequest.getMethod(),true);
        } else if (verificationVO != null){
            return handleEmailSending(verificationRequest.getMethod(),false);
        } else {
            return ResponseEntity.ok("이미 가입 된 이메일 또는 전화번호");
        }
    }



    /**
     * 이메일 인증 검사
     *
     * @param verificationRequest
     * @throws Exception
     */
    public VerificationUserInfoResponse verifyEmail(VerificationRequest verificationRequest) throws Exception {
        VerificationVO verificationVO = authMapper.findByMethod(verificationRequest.getMethod());

        if (verificationVO == null) {
            throw new Exception("사용자 정보가 다름");
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

        VerificationUserInfoResponse result = new VerificationUserInfoResponse();
        UserVO userVO = authMapper.findByEmail(verificationRequest.getMethod());

        if(userVO != null) {
            result.setMessage("인증 완료");
            result.setUser_name(userVO.getUsername());
            result.setProfile(userVO.getProfile());
        } else {
            result.setMessage("인증 완료 됐고 유저 정보 없음");
        }

        authMapper.deleteVerification(verificationVO.getUuid());
        return result;
    }

    // 로그인 로직
    public ResponseEntity<?> login(LoginRequest loginRequest) throws Exception {
        UserVO user = authMapper.findByEmail(loginRequest.getMethod()); // 로그인할때 입력한 Id 값으로 유저 정보 검색

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

    /**
     * 이메일 전송 공통 로직 처리 함수
     * @param method
     * @param isNew
     * @return
     * @throws Exception
     */
    private ResponseEntity<?> handleEmailSending(String method, boolean isNew) throws Exception {
        String verificationCode = generateVerificationCode();
        VerificationVO response = new VerificationVO();

        if(isNew) {
            response.setUuid(UUID.randomUUID().toString());
            response.setMethod(method);
            response.setCode(verificationCode);
            sendVerificationEmail(method, verificationCode);
            authMapper.emailVerification(response);
            return ResponseEntity.ok("인증 메일 발송 완료");
        } else {
            VerificationVO existingVerification = authMapper.findByMethod(method);
            existingVerification.setCode(verificationCode);
            sendVerificationEmail(method, verificationCode);
            authMapper.updateVerificationCode(existingVerification);
            return ResponseEntity.ok("인증 메일 재발송 완료");
        }
    }


    /**
     * 랜덤 코드 생성
     *
     * @return
     */
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

}
