package com.example.goose.iGoose.auth.controller;


import com.example.goose.iGoose.auth.response.VerificationResponse;
import com.example.goose.iGoose.auth.request.LoginRequest;
import com.example.goose.iGoose.auth.request.VerificationRequest;
import com.example.goose.iGoose.auth.service.AuthService;
import com.example.goose.iGoose.auth.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserVO userVO) throws Exception {
        return authService.signup(userVO);
    }


    // ID 중복확인 API
    @GetMapping("/check-id")
    public ResponseEntity<?> checkDuplicateId(@RequestBody UserVO userVO) throws Exception {
        if (authService.isIdDuplicate(userVO)) {
            return ResponseEntity.badRequest().body("이미 사용중인 ID 입니다.");
        }
        return ResponseEntity.ok("사용 가능한 ID 입니다.");
    }


    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.login(loginRequest);
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Refresh-Token") String refreshToken) throws Exception {
        try {
            authService.logout(refreshToken);  // Service layer handles the logout logic
            return ResponseEntity.ok("로그아웃 성공");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("로그아웃 실패: " + e.getMessage());
        }
    }

    /**
     * Email 중복 확인 API
     * @param verificationResponse
     * @return
     * @throws Exception
     */
    @GetMapping("/check-email")
    public ResponseEntity<?> checkDuplicateEmail(@RequestBody VerificationResponse verificationResponse) throws Exception {
        if (authService.isEmailDuplicate(verificationResponse)) {
            return ResponseEntity.badRequest().body("이미 사용중인 Email 입니다");
        }
        return ResponseEntity.ok("사용 가능한 Email 입니다.");
    }

    /**
     * 이메일 발송 요청 api
     * @param verificationRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/send-mail")
    public ResponseEntity<?> sendMail(@RequestBody VerificationRequest verificationRequest) throws Exception {
        return authService.sendEmail(verificationRequest);
    }

    // 이메일 인증 API
    @GetMapping("/verify-code")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest verificationRequest) throws Exception {

        try {
            authService.verifyEmail(verificationRequest);
            return ResponseEntity.ok("이메일 인증 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
