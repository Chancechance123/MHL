package com.Xuyuc.mhl.service;

import com.Xuyuc.mhl.dao.EmployeeDAO;
import com.Xuyuc.mhl.dao.MenuDAO;
import com.Xuyuc.mhl.domain.Menu;

import java.sql.SQLException;
import java.util.List;

/**
 * @author XuYuCheng
 * @version 1.0
 * 完成对Menu表的各种SQL语句操作
 */
@SuppressWarnings({"ALL"})
public class MenuService {
    private MenuDAO menuDAO = new MenuDAO();

    public List<Menu> list() throws SQLException {
        return menuDAO.queryMulti("select * from menu", Menu.class);
    }

    public Menu getMenuByID(int id) throws SQLException {
        return menuDAO.querySingle("select * from menu where id = ?", Menu.class,id);
    }
}
