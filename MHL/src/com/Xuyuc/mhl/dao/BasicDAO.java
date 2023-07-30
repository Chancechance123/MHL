package com.Xuyuc.mhl.dao;

import com.Xuyuc.mhl.utils.JDBCUtilsByDruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author XuYuCheng
 * @version 1.0
 * 开发BasicDAO
 */
@SuppressWarnings({"all"})
public class BasicDAO<T> {
    private QueryRunner qr = new QueryRunner();

    public int upDate(String sql, Object... paraments) throws SQLException {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruidUtils.getConnection();
            int update = qr.update(connection, sql, paraments);
            return update;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruidUtils.close(null, null, connection);
        }

    }
    //查询多行
    public List<T> queryMulti(String sal, Class<T> clazz) throws SQLException {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruidUtils.getConnection();
            return  qr.query(connection, sal, new BeanListHandler<T>(clazz));
            
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruidUtils.close(null, null, connection);
            
        }
        
    }
    //查询单行
    public T querySingle(String sal,Class<T> clazz,Object... paraments) throws SQLException {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruidUtils.getConnection();
            return  qr.query(connection, sal, new BeanHandler<T>(clazz), paraments);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruidUtils.close(null, null, connection);

        }

    }
    //查询单行单列
    public Object queryScalar(String sal,Object... paraments) throws SQLException {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruidUtils.getConnection();
            return  qr.query(connection, sal, new ScalarHandler<>(), paraments);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruidUtils.close(null, null, connection);

        }
    }
}
