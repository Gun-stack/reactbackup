package com.kosta.catdog.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kosta.catdog.dto.LoginRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.catdog.config.auth.PrincipalDetails;
import com.kosta.catdog.dto.LoginRequestDto;
import com.kosta.catdog.entity.Designer;
import com.kosta.catdog.entity.User;
import com.kosta.catdog.repository.UserDslRepository;
import com.kosta.catdog.repository.UserRepository;
import com.kosta.catdog.service.DesignerService;
import com.kosta.catdog.service.UserService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserDslRepository userDslRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final UserService userService;

    private final DesignerService designerService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    // 유저 정보 조회
    @GetMapping("/user")
    public ResponseEntity<User> user(Authentication authentication) {
        System.out.println("User Controller ===========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails : " + principalDetails);
//        System.out.println("User Id  : " + principalDetails.getUser().getId());
//        System.out.println("User Nickname : " + principalDetails.getUser().getNickname());
//        System.out.println("User Role : " + principalDetails.getUser().getRoles());
        return new ResponseEntity<User>(principalDetails.getUser(), HttpStatus.OK);
    }


    // 회원가입
    @PostMapping("/userjoin")
    public String join(@RequestBody User user) {
        try {
            System.out.println(user.getPassword().trim());
            String encodePassword = bCryptPasswordEncoder.encode(user.getPassword().trim());
            System.out.println(encodePassword);
            user.setPassword(encodePassword);
            userService.join(user);
            return "joinsuccess";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    // 아이디 중복 체크
    @GetMapping("/checkuserid")
    public ResponseEntity<String> checkuserid(@RequestParam String id) {
        System.out.println("Id : " + id);
        try {
            String res = userService.isUserIdDuplicate(id);
            return new ResponseEntity<String>(res, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    // 닉네임 중복 체크
    @GetMapping("/checkusernickname")
    public ResponseEntity<String> checkusernickname(@RequestParam String nickname) {
        try {
            String res = userService.isNicknameDuplicate(nickname);

            return new ResponseEntity<String>(res, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    // 이메일 중복 체크
    @GetMapping("/checkemail")
    public ResponseEntity<String> checkemail(@RequestParam String email) {
        System.out.println("email : " + email);
        try {
            String res = userService.isEmailDuplicate(email);
            return new ResponseEntity<String>(res, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    // 비밀번호 찾기
    public String findpassword(String id, String password) {
        System.out.println("findPassword!!");
        return "";
    }

    // 회원정보 수정
    public User checkuser(String id, String password) {
        System.out.println("checkUser !!");
        return null;
    }

    // 닉네임 변경

    @PostMapping("/modinickname")
    public ResponseEntity<User> modinickname(@RequestBody Map<String, Object> requestBody) {
        Integer num = (Integer) requestBody.get("num");
        String nickname = (String) requestBody.get("nickname");

        try {
            User user = userService.modifyNickname(num, nickname);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    // 전화번호 변경
    @PostMapping("/moditel")
    public ResponseEntity<User> moditel(@RequestBody Map<String, Object> requestBody) {
        Integer num = (Integer) requestBody.get("num");
        String userTel = (String) requestBody.get("userTel");

        try {
            User user = userService.modifyTel(num, userTel);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }

    }

    // 비밀번호 변경
    @PostMapping("/modipassword")
    public ResponseEntity<String> modipassword(@RequestBody Map<String, Object> requestBody) {
        System.out.println("modiPassword !!");
        Integer num = (Integer) requestBody.get("num");
//        String password = (String)requestBody.get("password");
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String password = bCryptPasswordEncoder.encode(requestBody.get("password").toString());

        System.out.println("Num : " + num);
        System.out.println("password : " + password);

        try {
            User user = userService.findByNum(num);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            System.out.println("UserNum : " + user.getNum());
            String res = userService.modifyPassword(user.getNum(), password);
            return new ResponseEntity<String>(res, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    // 회원 탈퇴
    @PostMapping("/exit")
    public ResponseEntity<Boolean> exit(@RequestBody LoginRequestDto dto, Authentication authentication) {
        System.out.println("EXIT !!");
        try {
            // 토큰에 저장된 유저 정보 가져오기
            User tuser = ((PrincipalDetails) authentication.getPrincipal()).getUser();
            // 입력한 아이디로 유저 정보 찾아오기
            User iuser = userService.getUserInfoById(dto.getId());
            // 토큰에 저장된 유저 아이디와 입력한 아이디로 유저 정보를 찾아와서 비교
            // 두개의 값이 같을 경우 패스워드 비교 실시
            Boolean idcheck = userService.idcomparison(tuser.getId(), iuser.getId());


            System.out.println("idcheck : " + idcheck);
            System.out.println("pwcheck dto : " + bCryptPasswordEncoder.matches(dto.getPassword(), tuser.getPassword()));

            if (idcheck && bCryptPasswordEncoder.matches(dto.getPassword(), tuser.getPassword())) { // 아이디 패스워드 다 일치할 경우 회원 state 를 false 로 변경하는 서비스 레퍼지토리 작성
                userService.withdrawalUser(iuser);
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }else{
                return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.FORBIDDEN);

        }
    }

    //비밀번호 체크
    @PostMapping("/ispassword")
    public ResponseEntity<Boolean> isPass(@RequestBody LoginRequestDto dto, Authentication authentication) {
        try {
            //입력한 패스워드
            String password = bCryptPasswordEncoder.encode(dto.getPassword());

            //dto와 토큰으로 현재 유저정보를 찾아옴
            User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

            System.out.println(bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword()));
            Boolean isPass = bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword());

            return new ResponseEntity<Boolean>(isPass, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }

    }

    //디자이너 등록
    @PostMapping("/desreg")
    public ResponseEntity<Object> desreg(@RequestPart(value = "file", required = false) List<MultipartFile> file
            , @RequestParam("id") String id
            , @RequestParam("desNickname") String desNickname
            , @RequestParam("position") String position) {
        try {
            User user = userService.getUserInfoById(id);
            Designer des = new Designer();
            BigDecimal zero = new BigDecimal(0);
            Map<String, Object> response = new HashMap<>();

            if (user.getRoles().equals("ROLE_USER")) { // 일반 회원이 미용사로 신청할 경우
                user.setRoles("ROLE_DES"); // user 권한 변경
                userService.modifyRole(id);
                des.setId(user.getId());
                des.setDesNickname(desNickname);
                des.setSId("0");
                des.setPosition(position);
                des.setEmail(user.getEmail());
                des.setTel(user.getTel());
                des.setRole(user.getRoles());
                des.setName(user.getName());
                des.setStar(zero);
                des.setReviewCnt(0);
                des.setBookmarkCnt(0);
                des.setName(user.getName());
            } else { // ROLE_SHOP 권한을 가진 사람이 신청할경우
                des.setId(user.getId());
                des.setSId("0");
                des.setName(user.getName());
                des.setDesNickname(desNickname);
                des.setPosition(position);
                des.setEmail(user.getEmail());
                des.setTel(user.getTel());
                des.setRole(user.getRoles());
                des.setStar(zero);
                des.setReviewCnt(0);
                des.setBookmarkCnt(0);
                des.setName(user.getName());

            }
            Designer des1 = designerService.desreg(des, file);

            response.put("des", des1);
            response.put("user", user);

            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

    }
}
