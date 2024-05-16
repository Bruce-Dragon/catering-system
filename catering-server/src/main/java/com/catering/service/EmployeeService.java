package com.catering.service;

import com.catering.dto.EmployeeDTO;
import com.catering.dto.EmployeeLoginDTO;
import com.catering.dto.EmployeePageQueryDTO;
import com.catering.entity.Employee;
import com.catering.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 员工账号状态
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询回显
     * @param id
     * @return
     */
    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);
}
