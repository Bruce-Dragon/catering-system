package com.catering.controller.user;

import com.catering.constant.JwtClaimsConstant;
import com.catering.dto.UserLoginDTO;
import com.catering.entity.User;
import com.catering.properties.JwtProperties;
import com.catering.result.Result;
import com.catering.service.UserService;
import com.catering.utils.JwtUtil;
import com.catering.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录:{}",userLoginDTO.getCode());
            //微信登录
            User user = userService.UserLogin(userLoginDTO);
            //为微信用户生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID,user.getId());
            String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
            //构建响应数据
            UserLoginVO userLoginVO = UserLoginVO.builder()
                   .id(user.getId())
                   .openid(user.getOpenid())
                   .token(token)
                   .build();
            return Result.success(userLoginVO);

    }
}
