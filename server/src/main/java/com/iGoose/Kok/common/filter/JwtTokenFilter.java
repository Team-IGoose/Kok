package com.iGoose.Kok.common.filter;

import com.iGoose.Kok.common.jwt.JwtTokenProvider;
import com.iGoose.Kok.common.service.CustomUserDetailsService;
import com.iGoose.Kok.common.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰 추출
        String encryptedToken = request.getHeader("Authorization");

        // 토큰이 존재하고 유효하다면 사용자 인증 처리
        if (encryptedToken != null) {
            try {



                String[] tokens = jwtTokenProvider.parseCombinedToken(encryptedToken);

                String accessToken = tokens[0];
                String refreshToken = tokens[1];

                if (jwtTokenProvider.validateToken(accessToken)) {
                    String uuid = jwtTokenProvider.getUserIdFromToken(accessToken);  // 토큰에서 사용자 UUID 추출
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(uuid);  // 사용자 정보 로드

                    // 인증 정보 설정
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // SecurityContext에 인증 정보 저장
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);


                }
                /**
                 * AT가 만료됐다면 RT이용해서 새로 생성
                 */
                else if (jwtTokenProvider.validateToken(refreshToken) && refreshTokenService.isValidRefreshToken(refreshToken)) {
                    String newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken);
                    String uuid = jwtTokenProvider.getUserIdFromToken(newAccessToken);  // 토큰에서 사용자 UUID 추출
                    String newRefreshToken = jwtTokenProvider.generateRefreshToken(uuid);

                    String newEncryptedToken = jwtTokenProvider.generateCombinedToken(newAccessToken, newRefreshToken);


                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(uuid);  // 사용자 정보 로드

                    refreshTokenService.storeRefreshToken(newRefreshToken, jwtTokenProvider.getRefreshTokenValidity());
                    // 인증 정보 설정
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // SecurityContext에 인증 정보 저장
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    response.setHeader("Authorization", newEncryptedToken);
                }
                /**
                 * RT도 만료됐을때 로직
                 */
                else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: 유효하지 않거나 만료된 refreshToken 입니다.");
                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: " + e.getMessage());   // 에러 메시지 출력
                return;
            }
        }

        // 다음 필터로 이동
        filterChain.doFilter(request, response);
    }


}
