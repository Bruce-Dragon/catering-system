package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO getReportTurnover(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }
        List<Double> doubleList = new ArrayList<>();
        for (LocalDate localDate : localDateList) {
            LocalDateTime dateTimeMin = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime dateTimeMax = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", dateTimeMin);
            map.put("end", dateTimeMax);
            map.put("status", Orders.COMPLETED);
            Double sum = reportMapper.getReportTurnover(map);
            if (sum == null) {
                sum = 0.0;
                //sum = sum == null ? 0.0 : sum;
            }
            doubleList.add(sum);
        }
        String dateTime = StringUtils.join(localDateList, ",");
        String money = StringUtils.join(doubleList, ",");
        return new TurnoverReportVO(dateTime, money);
    }

    @Override
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }
        //总用户数
        List<Integer> countUser = new ArrayList<>();
        List<Integer> newUser = new ArrayList<>();
        for (LocalDate localDate : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin",beginTime);
            Integer count = userMapper.getCount(map);
            map.put("end",endTime);
            Integer newCount = userMapper.getCount(map);
            countUser.add(count);
            newUser.add(newCount);
        }
            UserReportVO userReportVO = new UserReportVO();
            userReportVO.setDateList(StringUtils.join(localDateList,","));
            userReportVO.setTotalUserList(StringUtils.join(countUser,","));
            userReportVO.setNewUserList(StringUtils.join(newUser,","));

            return userReportVO;
    }
}
