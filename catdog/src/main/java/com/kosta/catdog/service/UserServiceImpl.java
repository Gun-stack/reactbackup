package com.kosta.catdog.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kosta.catdog.entity.Designer;
import com.kosta.catdog.entity.User;
import com.kosta.catdog.repository.DesignerRepository;
import com.kosta.catdog.repository.UserDslRepository;
import com.kosta.catdog.repository.UserRepository;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDslRepository userDslRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DesignerRepository designerRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void join(User user) throws Exception {
        userRepository.save(user);
        System.out.println("Get Roles : " + user.getRoles());
        if (user.getRoles().equals("ROLE_DES")) {
            System.out.println("IF ROLE_DES !!!");
            String id = user.getId();
            Designer des = new Designer();
            des.setId(id);
            System.out.println(des.getId());
            designerRepository.save(des);
        }


    }

    @Override

    public boolean login(String id, String password) {
        try {
            User user = userDslRepository.findById_AndPassword(id, password);
            return user != null;
        } catch (Exception e) {
            // 예외 처리: 예외가 발생하면 로깅 또는 특정 작업 수행
            e.printStackTrace();
            return false; // 로그인 실패로 처리
        }
    }

    @Override

    public String isUserIdDuplicate(String id) throws Exception {
        User user = userDslRepository.findById(id);
        if (user != null) {
            return "fail";
        } else return "success";
    }

    @Override
    public String isNicknameDuplicate(String nickname) throws Exception {
        User user = userDslRepository.findByNickname(nickname);
        if (user != null) {
            return "fail";
        } else return "success";
    }

    @Override
    public String isEmailDuplicate(String email) throws Exception {
        String existingEmail = userDslRepository.findByEmail(email);
        if (existingEmail != null) {
            return "fail";
        } else
            return "success";
    }

    @Override
    public User getUserInfoById(String id) throws Exception {
        return userDslRepository.findById(id);
    }

    @Override
    public String findId(String email) throws Exception {
        return userDslRepository.findId(email);
    }

    @Override
    public User findPassword() throws Exception {
        // email
        return null;
    }

    @Override
    public void withdrawalUser(User user) throws Exception {
        // 유저 state false로 변경하는 레퍼지토리 작용
        userDslRepository.withdrawalUser(user);
    }

    @Override
    public User modifyNickname(Integer num, String nickname) throws Exception {
        userDslRepository.modifyNickname(num, nickname);
        return userRepository.findById(num).get();
    }

    @Override
    public User modifyTel(Integer num, String tel) throws Exception {
        userDslRepository.modifyTel(num, tel);
        return userRepository.findById(num).get();
    }

    @Override
    public String modifyPassword(Integer num, String password) throws Exception {
        userDslRepository.modifyPassword(num, password);

        return "success";
    }

    @Override
    public void modifyRole(String id) throws Exception {
        userDslRepository.modifyRole(id);
    }

    @Override
    public User findByNum(Integer num) throws Exception {
        return userDslRepository.findByNum(num);

    }

    @Override
    public Boolean idcomparison(String tid, String iid) throws Exception {
        System.out.println("아이디 비교 : " + tid.equals(iid));
        Boolean idcheck = tid.equals(iid);
        if (idcheck) {
            return true;
        } else {
            throw new Exception("아이디 불일치!");
        }

    }

    @Override
    public ResponseEntity<String> pwcomparison(User tuser, User iuser) throws Exception {
        System.out.println("비밀번호 비교 : " + bCryptPasswordEncoder.matches(tuser.getPassword(), iuser.getPassword()));
        Boolean pwcheck = bCryptPasswordEncoder.matches(tuser.getPassword(), iuser.getPassword());
        if (pwcheck) {
            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("PASSWORD 불일치", HttpStatus.FORBIDDEN);
        }

    }


}