package com.catering.service.impl;

import com.catering.dto.GoodsSalesDTO;
import com.catering.entity.Orders;
import com.catering.mapper.OrderMapper;
import com.catering.mapper.ReportMapper;
import com.catering.mapper.UserMapper;
import com.catering.service.ReportService;
import com.catering.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

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
            map.put("end",endTime);
            Integer count = userMapper.getCount(map);
            map.put("begin",beginTime);
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

    @Override
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {
        //时间列表
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }
        List<Integer> counts = new ArrayList<>();
        List<Integer> incounts = new ArrayList<>();
        for (LocalDate localDate : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate,LocalTime.MAX);
            Map map = new HashMap();
            map.put("end",endTime);
            map.put("begin", beginTime);
            //每天订单总数
            Integer count = orderMapper.orderCout(map);
            map.put("status",Orders.COMPLETED);
            //每天有效订单数
            Integer incount = orderMapper.orderCout(map);
            counts.add(count);
            incounts.add(incount);
        }
        //统计订单总数
        Integer sum = 0;
        for (Integer count : counts) {
                sum = sum += count;
        }
        //统计有效订单数
        Integer insum = 0;
        for (Integer incount : incounts) {
                insum = insum += incount;
        }
        //订单有效率
        Double aDouble = 0.0;
        if (sum != 0){

            aDouble = (sum.doubleValue() / insum);

        }

            OrderReportVO orderReportVO = new OrderReportVO();
            //时间列表分隔
            orderReportVO.setDateList(StringUtils.join(localDateList, ","));
            //订单总数分隔
            orderReportVO.setOrderCountList(StringUtils.join(counts,","));
            //有效订单分隔
            orderReportVO.setValidOrderCountList(StringUtils.join(incounts,","));
            orderReportVO.setValidOrderCount(insum);
            orderReportVO.setTotalOrderCount(sum);
            orderReportVO.setOrderCompletionRate(aDouble);
        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO topReport(LocalDate begin, LocalDate end) {
        /*List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }*/
        LocalDateTime begins = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime ends = LocalDateTime.of(end, LocalTime.MAX);
        Map map = new HashMap();
        map.put("begin",begins);
        map.put("end",ends);
        map.put("status",Orders.COMPLETED);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.topsReport(map);
        List<String> name = goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String names = StringUtils.join(name, ",");
        List<Integer> number = goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numbers = StringUtils.join(number, ",");
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        salesTop10ReportVO.setNameList(names);
        salesTop10ReportVO.setNumberList(numbers);
        return salesTop10ReportVO;
    }

        public BusinessDataVO getBusinessData(LocalDateTime begin,LocalDateTime end){
            //营业额
            Map map = new HashMap();
            map.put("end",end);
            //总订单数
            Integer count = orderMapper.orderCout(map);
            map.put("begin",begin);
            //30天内总用户数
            Integer userCount = userMapper.getCount(map);
            map.put("status",Orders.COMPLETED);
            //总营业额
            Double reportTurnover = reportMapper.getReportTurnover(map);
            if (reportTurnover == null) {
                reportTurnover = 0.0;
            }
            //平均客单价
             Double avg = reportTurnover / userCount;
             //Double avg = (reportTurnover != null && userCount != null && userCount != 0) ? reportTurnover / userCount : 0.0;


            BusinessDataVO businessDataVO = new BusinessDataVO();
            businessDataVO.setUnitPrice(avg);
            businessDataVO.setTurnover(reportTurnover);
            //有效订单数
            Integer valCount = orderMapper.orderCout(map);
            businessDataVO.setValidOrderCount(valCount);
            //订单完成率
            Double resource = 0.0;
            if (count != 0 && valCount != 0){
                resource =  (valCount.doubleValue() / count);
            }
            businessDataVO.setOrderCompletionRate(resource);
            businessDataVO.setNewUsers(userCount);

            return businessDataVO;
        }

    @Override
    public void getBusinessDate(HttpServletResponse httpServletResponse) {
        //获取文件输入流（读）
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("temple/运营数据报表模板.xlsx");
        //算出时间区间
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        //基于模板文件创建一个新的表格对象
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(resourceAsStream);
            XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
            XSSFRow row = sheetAt.getRow(1);
            XSSFCell cell = row.getCell(1);
            cell.setCellValue("时间:"+ begin + "至" + end);
            sheetAt.getRow(3).getCell(2).setCellValue(businessData.getTurnover());
            sheetAt.getRow(3).getCell(4).setCellValue(businessData.getOrderCompletionRate());
            sheetAt.getRow(3).getCell(6).setCellValue(businessData.getNewUsers());
            sheetAt.getRow(4).getCell(2).setCellValue(businessData.getValidOrderCount());
            sheetAt.getRow(4).getCell(4).setCellValue(businessData.getUnitPrice());
            for (int i = 0; i < 30; i++){
                LocalDate localDate = begin.plusDays(i);
                LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
                LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
                BusinessDataVO businessData1 = getBusinessData(beginTime, endTime);
                XSSFRow row1 = sheetAt.getRow(7 + i);
                row1.getCell(1).setCellValue(localDate.toString());
                row1.getCell(3).setCellValue(businessData1.getTurnover());
                row1.getCell(3).setCellValue(businessData1.getValidOrderCount());
                row1.getCell(4).setCellValue(businessData1.getOrderCompletionRate());
                row1.getCell(5).setCellValue(businessData1.getUnitPrice());
                row1.getCell(6).setCellValue(businessData1.getNewUsers());
            }
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            xssfWorkbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                resourceAsStream.close();
                xssfWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
