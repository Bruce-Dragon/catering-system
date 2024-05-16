package com.catering.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface ReportMapper {


    Double getReportTurnover(Map map);
}
