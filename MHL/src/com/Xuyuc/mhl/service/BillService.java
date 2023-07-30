package com.Xuyuc.mhl.service;


import com.Xuyuc.mhl.dao.BillDAO;
import com.Xuyuc.mhl.dao.MultiTableDAO;
import com.Xuyuc.mhl.domain.Bill;
import com.Xuyuc.mhl.domain.MultiTable;


import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * @author XuYuCheng
 * @version 1.0
 * //处理账单相关操作
 */
@SuppressWarnings({"ALl"})
public class BillService {
    private BillDAO billDAO = new BillDAO();
    private MenuService menuService = new MenuService();
    private DiningTableService diningTableService = new DiningTableService();
    private MultiTableDAO multiTableDAO = new MultiTableDAO();

    //相关方法 点餐
    //更新状态
    //判断是否成功
    public boolean orderBill(int menuId, int nums, int diningTableId) throws Exception {
        //生成uuid订单号
        String billID = UUID.randomUUID().toString();

        //将账单信息放入表中
        int upDate = billDAO.upDate("insert into bill values(null,?,?,?,?,?,now(),'未结账')", billID, menuId, nums, menuService.getMenuByID(menuId).getPrice() * nums, diningTableId);
        if (upDate <= 0) {
            return false;
        }
        boolean b = diningTableService.updateDiningTable(diningTableId, "就餐中");
        return b;
    }

    //返回账单
    public List<Bill> list() throws SQLException {
        return billDAO.queryMulti("select * from bill ", Bill.class);
    }

    public List<MultiTable> list2() throws SQLException {
        return multiTableDAO.queryMulti("select  bill.*, name from bill, menu where bill.menuId = menu.id ", MultiTable.class);
    }

    //查看某个餐桌是否有未结账
    public boolean hasPayBillBYDiningTableId(int diningTableId) throws SQLException {
        Bill bill = billDAO.querySingle("SELECT * FROM bill WHERE diningTableId=? and state = '未结账' LIMIT 0, 1", Bill.class, diningTableId);
        return bill != null;
    }

    //完成账单 餐桌存在但未结账
    public boolean payBill(int diningTableId, String payMode) throws SQLException {
        //改变bill表
        int upDate = billDAO.upDate("update bill set state = ? where diningTableId=? and state = '未结账'", payMode, diningTableId);
        if (upDate < 0) {
            return false;
        }
        //更新DiningTable表
        //billDAO.upDate("update bill set state = ? where diningTableId=? and state = '未结账'");

        if (!diningTableService.updateDiningToFree(diningTableId, "空")) {
            return false;
        }
        return true;
    }

}
