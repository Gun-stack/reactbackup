package com.kosta.catdog.service;


import java.util.Optional;

import com.kosta.catdog.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
	// 회원가입
	void join(User user) throws Exception;

	// 로그인
	boolean login(String id, String password) throws Exception;



	// 아이디 중복체크
	String isUserIdDuplicate(String id) throws Exception;
	// 닉네임 중복체크
	String isNicknameDuplicate(String nickname) throws Exception;
	// 이메일 중복체크
	String isEmailDuplicate(String email) throws Exception;
	// 아이디로 회원정보 가져오기
	User getUserInfoById(String id) throws Exception;
	// 계정 찾기
	String findId(String email) throws Exception;
	// 비밀번호 찾기
	User findPassword() throws Exception;
	// 회원탈퇴
	void withdrawalUser(User user) throws Exception;
	// 닉네임 변경
	User modifyNickname(Integer num, String nickname) throws Exception;
	// 전화번호 변경
	User modifyTel(Integer num, String tel) throws Exception;
	// 비밀번호 변경	

	String modifyPassword(Integer num, String password) throws Exception;

	void modifyRole(String id) throws Exception;

	// 사용자 번호(PK)로 사용자 찾기
	User findByNum(Integer num) throws Exception;

	// ID 비교
	Boolean idcomparison(String tid, String iid) throws Exception;

	// Password 비교
	ResponseEntity<String> pwcomparison(User tuser, User iuser) throws Exception;
}