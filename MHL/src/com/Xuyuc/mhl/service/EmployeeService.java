package com.Xuyuc.mhl.service;

import com.Xuyuc.mhl.dao.EmployeeDAO;
import com.Xuyuc.mhl.domain.Employee;

import java.sql.SQLException;

/**
 * @author XuYuCheng
 * @version 1.0
 * 完成对employee表的各种SQL语句操作
 */
@SuppressWarnings({"all"})
public class EmployeeService {
    //定义EmployeeDAO对象 属性
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    public Employee getEmployByIdAndPwd(String empId,String pwd) throws SQLException {
        return   employeeDAO.querySingle("select * from employee where empId = ? AND pwd =md5(?)",
                Employee.class, empId, pwd);
    }
}
