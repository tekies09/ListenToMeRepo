package com.ssafy.a605.api.controller.counselor;


import com.ssafy.a605.model.dto.CertificateDto;
import com.ssafy.a605.model.dto.CounselorDto;
import com.ssafy.a605.model.entity.Certificate;
import com.ssafy.a605.model.entity.CounselorCategory;
import com.ssafy.a605.model.response.counselor.CounselorInfoRes;
import com.ssafy.a605.service.CategoryService;
import com.ssafy.a605.service.CertificateService;
import com.ssafy.a605.service.CounselorService;
import com.ssafy.a605.service.JwtServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/counselor-api")
@Api("상담자회원관리")
public class CounselorController {

    public static final Logger logger = LoggerFactory.getLogger(CounselorController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private CounselorService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CertificateService certificateService;

    @ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) Map<String,String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            Map<String,String> loginMap = userService.login(map);
            if (loginMap != null) {
                String token = jwtService.create("userid", loginMap.get("email"), "access-token");// key, data, subject
                logger.debug("로그인 토큰정보 : {}", token);
                resultMap.put("access-token", token);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("message", FAIL);
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("로그인 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<Map<String, Object>> getInfo(
            @PathVariable("userEmail") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userEmail) throws Exception {
//		logger.debug("userid : {} ", userid);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        CounselorInfoRes counselorInfoRes = userService.counselorInfo(userEmail);
        List<CounselorCategory> category = categoryService.getCounselorCategory(userEmail);
        List<CertificateDto> certificate = certificateService.getCounselorCertificate(userEmail);
        resultMap.put("userInfo", counselorInfoRes);
        resultMap.put("category", category);
        resultMap.put("certificate", certificate);
        resultMap.put("message", SUCCESS);
        status = HttpStatus.ACCEPTED;
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @PostMapping("/user")
    public ResponseEntity<String> joinUser(
            @RequestBody @ApiParam(value = "회원 가입 정보", required = true) CounselorDto counselorDto) throws Exception {
        logger.info("joinUser - 호출");
        if (userService.joinCounselor(counselorDto)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/shortgreeting")
    public ResponseEntity<String> setShortGreeting(@RequestBody Map<String,String> param) throws Exception {
        logger.info("shortGreeting 수정");
        String shortGreeting = param.get("shortgreeting");
        if (userService.updateShortGreeting(shortGreeting, jwtService.getUserId())) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/greeting")
    public ResponseEntity<String> setGreeting(@RequestBody Map<String,String> param) throws Exception {
        logger.info("greeting 수정");
        String greeting = param.get("greeting");
        if (userService.updateGreeting(greeting, jwtService.getUserId())) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<String> checkId(
            @PathVariable("userEmail") @ApiParam(value = "중복 아이디 체크", required = true) String userEmail) throws Exception {
        logger.info("checkId - 호출");
        if (userService.checkId(userEmail)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user")
    public ResponseEntity<String> modifyUser(
            @RequestBody @ApiParam(value = "수정할 회원정보.", required = true) CounselorDto counselorDto) throws Exception {
        logger.info("modify- 호출");

        if (userService.updateCounselor(counselorDto)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<String>(FAIL, HttpStatus.OK);
    }
    //
    @DeleteMapping("/user/{userEmail}")
    public ResponseEntity<String> deleteUser(
            @PathVariable("userEmail") @ApiParam(value = "회원탈퇴", required = true) String userEmail) throws Exception {
        logger.info("delete - 호출");
        if (userService.deleteCounselor(userEmail)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/certificate/{userEmail}")
    public ResponseEntity<List<CertificateDto>> getCertificateInfo(
            @PathVariable("userEmail") @ApiParam(value = "자격증 조회", required = true) String userEmail) throws Exception
    {
      logger.info("certificate - 호출");
      List<CertificateDto>results =  userService.getCertificates(userEmail);
      if(results == null){

          //return new ResponseEntity<List<CertificateDto>>(FAIL, HttpStatus.OK);
      }

        return new ResponseEntity<List<CertificateDto>>(results, HttpStatus.OK);
    }
}