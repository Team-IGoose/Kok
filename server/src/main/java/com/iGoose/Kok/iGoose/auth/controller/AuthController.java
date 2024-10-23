package com.iGoose.Kok.iGoose.auth.controller;


import com.iGoose.Kok.iGoose.auth.response.VerificationResponse;
import com.iGoose.Kok.iGoose.auth.request.LoginRequest;
import com.iGoose.Kok.iGoose.auth.request.VerificationRequest;
import com.iGoose.Kok.iGoose.auth.response.VerificationUserInfoResponse;
import com.iGoose.Kok.iGoose.auth.service.AuthService;
import com.iGoose.Kok.iGoose.auth.service.ImageService;
import com.iGoose.Kok.iGoose.auth.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestPart("user") UserVO userVO,
                                    @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        try {
            // Pass both userVO and file to the service layer
            authService.signup(userVO, file);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: " + e.getMessage());
        }
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
    @PostMapping("/send-login-mail")
    public ResponseEntity<?> sendLoginMail(@RequestBody VerificationRequest verificationRequest) throws Exception {
        return authService.sendLoginEmail(verificationRequest);
    }

    @PostMapping("/send-signUp-mail")
    public ResponseEntity<?> sendSingUpMail(@RequestBody VerificationRequest verificationRequest) throws Exception {
        return authService.sendSingUpEmail(verificationRequest);
    }

    // 이메일 인증 API
    @GetMapping("/verify-code")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest verificationRequest) throws Exception {

        try {
            VerificationUserInfoResponse result = authService.verifyEmail(verificationRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
