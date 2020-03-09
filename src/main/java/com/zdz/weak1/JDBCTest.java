package com.zdz.weak1;

import com.zdz.weak1.entry.Student;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCTest {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            // 加载类驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 通过驱动管理器获取数据库连接
            connection = DriverManager.getConnection("jdbc:mysql://47.105.41.157:3306/learn?characterEncoding=utf-8", "root", "123456");
            // 准备sql语句
            String sql = "insert into student(name, age) values(?, ?)";
            // 获取预处理statement
            statement = connection.prepareStatement(sql);
            // 对占位符赋值
            statement.setString(1, "李四");
            statement.setInt(2, 14);
            // 执行sql语句
            int i = statement.executeUpdate();
            System.out.println(i);
            // 准备sql语句
            String selectSql = "select * from student where id = ?";
            statement = connection.prepareStatement(selectSql);
            statement.setInt(1, 1);
            rs = statement.executeQuery();
            List<Student> students = resultToObject(rs, Student.class);

            for (Student s : students) {
                System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != rs) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(null != statement){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> List<T> resultToObject(ResultSet resultSet, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        try {
            while (resultSet.next()){
                // 通过反射获取对象实例
                T t = clazz.getConstructor().newInstance();
                // 获取每一行中所有列的信息
                ResultSetMetaData metaData = resultSet.getMetaData();
                // 遍历mataData获取每个列中的信息
                for (int i = 0;i < metaData.getColumnCount();i++){
                    // 通过索引获取某个列的信息，从1开始
                    String column1 = metaData.getColumnName(i + 1);
                    // 通过反射将结果集中的字段名与实体对象中的属性名相对应
                    // 需要使用getDeclaredField() 方法获取私有属性
                    Field f = clazz.getDeclaredField(column1);
                    // 当isAccessible()的结果是false时不允许通过反射访问该字段
                    // 当该字段时private修饰时isAccessible()得到的值是false，必须要改成true才可以访问
                    f.setAccessible(true);
                    // 将结果集中的值赋给相应的对象实体的属性
                    f.set(t, resultSet.getObject(column1));
                }
                resultList.add(t);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
}
