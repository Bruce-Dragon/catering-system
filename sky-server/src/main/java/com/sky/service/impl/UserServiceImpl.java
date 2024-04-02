package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User UserLogin(UserLoginDTO userLoginDTO) {
        //调用微信服务接口。获得当前的微信用户的openid
        Map<String, String> claim = new HashMap<>();
        claim.put("appid",weChatProperties.getAppid());
        claim.put("secret",weChatProperties.getSecret());
        claim.put("js_code",userLoginDTO.getCode());
        claim.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, claim);
        //JSON.parseObject()的作用是将指定的JSON字符串转换成自己的实体类的对象
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");


        //判断openid是否为null。如果为null表示登陆失败，抛出业务异常
        if (openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断当前用户是否为新用户
        User user = userMapper.UserLogin(openid);
        //如果是，自动完成注册
        if (user == null){
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);

        }
        //返回用户对象
        return user;
    }
}
