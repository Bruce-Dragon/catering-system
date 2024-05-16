package com.catering.controller.admin;

import com.catering.result.Result;
import com.catering.service.ReportService;
import com.catering.vo.OrderReportVO;
import com.catering.vo.SalesTop10ReportVO;
import com.catering.vo.TurnoverReportVO;
import com.catering.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
            TurnoverReportVO turnoverReportVO = reportService.getReportTurnover(begin,end);
            return Result.success(turnoverReportVO);
    }
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
            UserReportVO userReportVO = reportService.userReport(begin,end);
            return Result.success(userReportVO);
    }
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
            OrderReportVO orderReportVO = reportService.orderReport(begin,end);
            return Result.success(orderReportVO);
    }
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> salesTop10Report(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
            SalesTop10ReportVO salesTop10ReportVO = reportService.topReport(begin,end);
            return Result.success(salesTop10ReportVO);
    }
    @GetMapping("/export")
    public void businessDate(HttpServletResponse httpServletResponse){
        reportService.getBusinessDate(httpServletResponse);

    }
}
