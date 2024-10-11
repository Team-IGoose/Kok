package com.example.goose.iGoose.auth.mapper;


import com.example.goose.iGoose.auth.response.VerificationResponse;
import com.example.goose.iGoose.auth.request.UserInfoRequest;
import com.example.goose.iGoose.auth.vo.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface AuthMapper {

    /**
     * 회원가입
     * @param userVO
     * @throws Exception
     */
    void insertUser(UserVO userVO) throws Exception;

    /**
     * 이메일 인증 요청 api
     * @param verificationResponse
     * @throws Exception
     */
    @Insert("INSERT INTO \"VERIFICATION\" (uuid, method, expire, code)" +
            " VALUES (#{uuid}, #{method}, now(), #{code})")
    void emailVerification(VerificationResponse verificationResponse) throws Exception;

    /**
     * id로 유저 정보 검색
     * @param id
     * @return
     * @throws Exception
     */
    @Select("SELECT * FROM \"USER\" WHERE \"id\" = #{id}")
    UserVO findById(String id) throws Exception;

    /**
     * id 중복 검사
     * @param id
     * @return
     */
    @Select("SELECT COUNT(*) FROM \"USER\" WHERE \"id\" = #{id}")
    int countById(String id);

    /**
     * email 중복 검사
     * @param email
     * @return
     */
    @Select("SELECT COUNT(*) FROM \"USER\" WHERE email = #{email}")
    int countByEmail(String email);


    /**
     * method 중복 검사
     * @param method
     * @return
     */
    @Select("SELECT COUNT(*) FROM VERIFICATION WHERE method = #{method}")
    boolean existsByMethod(String method);

    /**
     * uuid로 유저 정보 검색
     * @param uuid
     * @return
     * @throws Exception
     */
    @Select("SELECT * FROM \"USER\" WHERE \"uuid\" = #{uuid}")
    UserVO findByUuid(String uuid) throws Exception;

    /**
     * email로 유저 정보 검색
     * @param method
     * @return
     */
    @Select("SELECT * FROM \"USER\" WHERE email = #{method} OR phone_number = #{method}")
    VerificationResponse findByEmail(String method);

    /**
     * email인증 여부 수정
     * @param verificationResponse
     */
    @Update("UPDATE \"USER_VERIFICATION\" SET is_verified = true, email_verification = #{email_verification} WHERE uuid = #{uuid}")
    void updateEmailVerified(VerificationResponse verificationResponse) throws Exception;

    /**
     * 이메일 인증 코드 만료시 임시 저장한 사용자 정보 삭제
     * @param expirationTime
     */
    @Delete("DELETE FROM \"USER_VERIFICATION\" WHERE is_verified = false AND expire < #{expirationTime}")
    void deleteExpiredUsers(Date expirationTime);

    /**
     * uuid로 유저 정보 업데이트
     * @param userInfoRequest
     */
    @Update("UPDATE \"USER\" SET user_name=#{userInfoRequest.user_name}, pronoun=#{userInfoRequest.pronoun}, intro=#{userInfoRequest.intro}, gender=#{userInfoRequest.gender}, " +
            "status=#{userInfoRequest.status}, profile=#{userInfoRequest.profile}, feeling=#{userInfoRequest.feeling}, is_professional=#{userInfoRequest.is_professional}, " +
            "revenue_status=#{userInfoRequest.revenue_status}, updated= now() WHERE uuid=#{uuid}")
    void updateUserInfo(@Param("userInfoRequest") UserInfoRequest userInfoRequest, @Param("uuid") String uuid);


    /**
     * uuid로 유저 정보 삭제
     * @param uuid
     */
    @Delete("DELETE FROM \"USER\" WHERE uuid = #{uuid}")
    void deleteUserByUuid(@Param("uuid") String uuid);
}
