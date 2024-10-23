package com.iGoose.Kok.iGoose.auth.mapper;


import com.iGoose.Kok.iGoose.auth.request.UserInfoRequest;
import com.iGoose.Kok.iGoose.auth.response.VerificationUserInfoResponse;
import com.iGoose.Kok.iGoose.auth.vo.UserVO;
import com.iGoose.Kok.iGoose.auth.vo.VerificationVO;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface AuthMapper {

    /**
     * 회원가입
     * @param userVO
     * @throws Exception
     */
    @Insert("INSERT INTO \"USER\" (uuid, user_name, password, pronoun, intro, gender, status, profile, feeling, is_professional, revenue_status, created, updated,email, phone_number, email_verified, phone_verified)" +
            " VALUES (#{uuid}, #{user_name}, #{password}, #{pronoun}, #{intro}, #{gender}, #{status}, #{profile}, #{feeling}, #{is_professional}, #{revenue_status}, now(), now(), #{email}, #{phone_number}, #{email_verified}, #{phone_verified})")
    void insertUser(UserVO userVO) throws Exception;

    /**
     * 이메일 인증 요청 api
     * @param verificationVO
     * @throws Exception
     */
    @Insert("INSERT INTO \"VERIFICATION\" (uuid, method, expire, code)" +
            " VALUES (#{uuid}, #{method}, now(), #{code})")
    void emailVerification(VerificationVO verificationVO) throws Exception;

    /**
     * user_name로 유저 정보 검색
     * @param user_name
     * @return
     * @throws Exception
     */
    @Select("SELECT * FROM \"USER\" WHERE \"user_name\" = #{user_name}")
    UserVO findById(String user_name) throws Exception;

    /**
     * id 중복 검사
     * @param user_name
     * @return
     */
    @Select("SELECT COUNT(*) FROM \"USER\" WHERE \"user_name\" = #{user_name}")
    int countById(String user_name);

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
     * method를 이용해 verification 테이블 검색
     * @param method
     * @return
     */
    @Select("SELECT * FROM \"VERIFICATION\" WHERE method = #{method}")
    VerificationVO findByMethod(String method);

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
    UserVO findByEmail(String method);


    /**
     * 이메일 또는 전화번호로 user_name, profile 조회
     * @param method
     * @return
     */
    @Select("SELECT user_name, profile FROM \"USER\" WHERE email = #{method} OR phone_number = #{method}")
    VerificationUserInfoResponse findByEmailOrPhoneNumber(String method);

    /**
     * email인증 여부 수정
     * @param verificationVO
     */
    @Update("UPDATE \"VERIFICATION\" SET code = #{code} WHERE uuid = #{uuid}")
    void updateEmailVerified(VerificationVO verificationVO) throws Exception;

    @Update("UPDATE \"VERIFICATION\" SET code = #{code} , expire = now() WHERE uuid = #{uuid}")
    void updateVerificationCode(VerificationVO verificationVO) throws Exception;

    /**
     * 첫 인증 하지 않고 이메일 인증 코드 만료시 임시 저장한 사용자 정보 삭제
     * @param expirationTime
     */
    @Delete("DELETE FROM \"VERIFICATION\" WHERE expire < #{expirationTime}")
    void deleteExpiredUsers(Date expirationTime);

    @Delete("DELETE FROM \"VERIFICATION\" WHERE uuid = #{uuid}")
    void deleteVerification(String uuid) throws Exception;

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
