package com.Xuyuc.mhl.view;


import com.Xuyuc.mhl.domain.*;
import com.Xuyuc.mhl.service.BillService;
import com.Xuyuc.mhl.service.DiningTableService;
import com.Xuyuc.mhl.service.EmployeeService;
import com.Xuyuc.mhl.service.MenuService;
import com.Xuyuc.mhl.utils.Utility;

import java.sql.SQLException;
import java.util.List;

/**
 * @author XuYuCheng
 * @version 1.0
 * 主界面
 */
@SuppressWarnings({"All"})
public class MHLView {
    public boolean loop = true;
    public String key = "";
    //创建对应的service对象调用方法
    //定义一个EmployeeService对象为属性
    private EmployeeService employeeService = new EmployeeService();
    private DiningTableService diningTableService = new DiningTableService();
    private MenuService menuService = new MenuService();
    private BillService billService = new BillService();

    //显示所有菜品
    public void listMenu() throws SQLException {
        System.out.println("菜品编号\t\t菜品名\t\t\t类别\t\t\t价格");
        List<Menu> list = menuService.list();
        for (Menu menu : list) {
            System.out.println(menu);
        }
        System.out.println("===================显示完毕===================");

    }

    //结账房屋
    //完成结账
    public void payBill() throws SQLException {
        System.out.println("==============结账服务============");
        System.out.print("请选择要结账的餐桌编号(-1退出): ");
        int diningTableId = Utility.readInt();
        if (diningTableId == -1) {
            System.out.println("=============取消结账============");
            return;
        }
        //验证餐桌是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(diningTableId);
        if (diningTable == null) {
            System.out.println("=============结账的餐桌不存在============");
            return;
        }
        //验证餐桌是否有需要结账的账单
        if (!billService.hasPayBillBYDiningTableId(diningTableId)) {
            System.out.println("=============该餐位没有未结账账单============");
            return;
        }
        System.out.print("结账方式(现金/支付宝/微信)回车表示退出: ");
        String payMode = Utility.readString(20, "");//说明如果回车，就是返回 ""
        if ("".equals(payMode)) {
            System.out.println("=============取消结账============");
            return;
        }
        char key = Utility.readConfirmSelection();
        if (key == 'Y') { //结账

            //调用我们写的方法
            if (billService.payBill(diningTableId, payMode)) {
                System.out.println("=============完成结账============");
            } else {
                System.out.println("=============结账失败============");
            }

        } else {
            System.out.println("=============取消结账============");
        }
    }

    //显示账单信息
//    public void listBill() throws SQLException {
//        List<Bill> Bills = billService.list();
//        System.out.println("编号\t\t菜品号\t\t\t菜品量\t\t\t金额\t\t\t桌号\t\t日期\t\t\t\t\t\t\t\t状态");
//        for (Bill bill : Bills) {
//            System.out.println(bill);
//        }
//
//        System.out.println("=====================显示完毕======================");
//    }
    //显示多条账单信息
    public void listBill() throws SQLException {
        List<MultiTable> multiTables = billService.list2();
        System.out.println("\n编号\t\t菜品号\t\t\t菜品量\t\t\t金额\t\t\t桌号\t\t日期\t\t\t\t\t\t\t\t状态\t\t\t菜品名");
        for (MultiTable bill : multiTables) {
            System.out.println(bill);
        }

        System.out.println("=====================显示完毕======================");
    }

    //点餐
    public void orderMenu() throws Exception {
        System.out.println("===================点餐服务===================");
        System.out.println("请输入点餐的桌号(-1退出)：");
        int orderDiningTableId = Utility.readInt();
        if (orderDiningTableId == -1) {
            System.out.println("================取消订餐===================");
            return;
        }
        System.out.println("请输入点菜的编号(-1退出)：");
        int orderMenuId = Utility.readInt();
        if (orderMenuId == -1) {
            System.out.println("=================取消点菜=================");
            return;
        }
        System.out.println("请输入点餐的数量(-1退出)：");
        int orderNums = Utility.readInt();
        if (orderNums == -1) {
            System.out.println("=================取消点菜=================");
            return;
        }
        //验证餐桌号是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(orderDiningTableId);
        if (diningTable == null) {
            System.out.println("=================餐桌不存在=================");

        }
        System.out.println("===================显示完毕===================");
        //验证菜品是否存在
        Menu menuByID = menuService.getMenuByID(orderMenuId);
        if (menuByID == null) {
            System.out.println("=================菜品不存在=================");

        }
        //点餐服务
        if (billService.orderBill(orderMenuId, orderNums, orderDiningTableId)) {
            System.out.println("点餐成功");
        } else {
            System.out.println("点餐失败");
        }

    }

    //预定餐桌功能
    public void orderDiningTable() throws SQLException {
        System.out.println("===================预定餐桌===================");
        System.out.println("请选择餐桌座位编号(-1表示退出)：");
        int orderId = Utility.readInt();
        if (orderId == -1) {
            System.out.println("===================取消预定===================");
            return;
        }
        //得到y/n
        char key = Utility.readConfirmSelection();
        if (key == 'Y') {
            //判断餐桌是否存在
            DiningTable diningTable = diningTableService.getDiningTableById(orderId);
            if (diningTable == null) {
                System.out.println("===================预定餐桌不存在==================");
                return;
            }
            //判断餐桌状态
            if (!(("空".equals(diningTable.getState())))) {
                System.out.println("===================餐桌已经被预定==================");
                return;
            }
            //输入预定信息
            System.out.println("预定人的名字：");
            String orderName = Utility.readString(50);

            System.out.println("预定人的电话：");
            String orderTel = Utility.readString(50);
            //更新信息
            if (diningTableService.orderDiningTable(orderId, orderName, orderTel)) {
                System.out.println("===================餐桌预定成功==================");

            } else {
                System.out.println("===================餐桌预定失败==================");

            }

        } else {
            System.out.println("===================取消预定===================");

        }

        System.out.println("===================显示完毕===================");

    }

    public static void main(String[] args) throws Exception {
        new MHLView().mainMenu();
    }

    //显示餐桌状态信息
    public void listDiningTable() throws SQLException {
        System.out.println("\n餐桌编号\t\t餐桌状态");
        List<DiningTable> list = diningTableService.list();
        for (DiningTable diningTable : list) {
            System.out.println(diningTable);
        }
        System.out.println("===================显示完毕===================");

    }

    //显示主菜单
    public void mainMenu() throws Exception {
        while (loop) {
            System.out.println("===================满汉楼====================");
            System.out.println("\t\t 1 登录满汉楼");
            System.out.println("\t\t 2 退出满汉楼");
            System.out.println("请输入你的选择：");
            key = Utility.readString(1);
            switch (key) {
                case "1":
                    System.out.println("请输入员工号：");
                    String empId = Utility.readString(50);
                    System.out.println("请输入密码：");
                    String paw = Utility.readString(50);
                    Employee emp = employeeService.getEmployByIdAndPwd(empId, paw);
                    //用数据库判断
                    //说明存在
                    if (emp != null) {
                        System.out.println("===================登录成功===================\n");
                        //显示二级菜单 while循环
                        while (loop) {
                            System.out.println("===================满汉楼(二级菜单)===================");
                            System.out.println("\t\t 1 显示餐桌状态");
                            System.out.println("\t\t 2 预定餐桌");
                            System.out.println("\t\t 3 显示所有菜单");
                            System.out.println("\t\t 4 点餐服务");
                            System.out.println("\t\t 5 查看账单");
                            System.out.println("\t\t 6 结账");
                            System.out.println("\t\t 9 退出满汉楼");
                            System.out.println("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    listDiningTable();
                                    break;
                                case "2":
                                    //System.out.println("预定餐桌");
                                    orderDiningTable();
                                    break;
                                case "3":
                                    //System.out.println("显示所有菜单");
                                    listMenu();
                                    break;
                                case "4":
                                    //System.out.println("点餐服务");
                                    orderMenu();
                                    break;
                                case "5":
                                    //System.out.println("查看账单");
                                    listBill();
                                    break;
                                case "6":
                                    //System.out.println("结账");
                                    payBill();
                                    break;
                                case "9":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("你的输入有误，请重新输入：");
                            }
                        }
                    } else {
                        System.out.println("===================登录失败===================");
                    }
                    break;
                case "2":
                    loop = false;
                    break;
                default:
                    System.out.println("你的输入有误，请重新输入：");
            }
        }
        System.out.println("你成功退出满汉楼~~~");

    }
}
