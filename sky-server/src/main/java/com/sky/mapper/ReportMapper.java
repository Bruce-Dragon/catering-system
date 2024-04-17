package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;

@Mapper
@Repository
public interface ReportMapper {


    Double getReportTurnover(Map map);
}
