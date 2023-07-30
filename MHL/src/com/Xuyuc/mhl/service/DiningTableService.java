package com.Xuyuc.mhl.service;

import com.Xuyuc.mhl.dao.DiningTableDAO;
import com.Xuyuc.mhl.domain.DiningTable;

import java.sql.SQLException;
import java.util.List;

/**
 * @author XuYuCheng
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class DiningTableService {
    private DiningTableDAO diningTableDAO = new DiningTableDAO();

    //返回餐桌状态信息
    public List<DiningTable> list() throws SQLException {
        return diningTableDAO.queryMulti("select id,state from diningTable", DiningTable.class);

    }

    //根据id来判断 餐桌是否为null
    public DiningTable getDiningTableById(int id) throws SQLException {
        return diningTableDAO.querySingle("select * from diningTable where id = ?", DiningTable.class, id);
    }

    //预定餐桌
    public boolean orderDiningTable(int id, String orderName, String orderTel) throws SQLException {
        int upDate = diningTableDAO.upDate("update diningTable set state = '已经预定',orderName = ?, orderTel = ? where id = ?" ,orderName, orderTel,id);
        return upDate > 0;
    }
    //更新餐桌信息
    public boolean updateDiningTable(int id, String state) throws SQLException {
        int upDate = diningTableDAO.upDate("update diningTable set state = ? where id = ?", state, id);
        return upDate > 0;
    }
    //餐桌更改为空闲状态
    public boolean updateDiningToFree(int id, String state) throws SQLException {
        int upDate = diningTableDAO.upDate("update diningTable set state = ? ,orderName='',orderTel = '' where id = ?", state, id);
        return upDate > 0;
    }
    
}
