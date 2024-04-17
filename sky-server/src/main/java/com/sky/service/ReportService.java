package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getReportTurnover(LocalDate begin, LocalDate end);

    UserReportVO userReport(LocalDate begin, LocalDate end);
}
