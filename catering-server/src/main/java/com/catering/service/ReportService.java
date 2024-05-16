package com.catering.service;

import com.catering.vo.OrderReportVO;
import com.catering.vo.SalesTop10ReportVO;
import com.catering.vo.TurnoverReportVO;
import com.catering.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getReportTurnover(LocalDate begin, LocalDate end);

    UserReportVO userReport(LocalDate begin, LocalDate end);

    OrderReportVO orderReport(LocalDate begin, LocalDate end);

    SalesTop10ReportVO topReport(LocalDate begin, LocalDate end);

    void getBusinessDate(HttpServletResponse httpServletResponse);

}
