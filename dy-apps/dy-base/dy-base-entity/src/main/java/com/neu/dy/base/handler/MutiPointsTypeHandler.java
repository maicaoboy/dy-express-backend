package com.neu.dy.base.handler;

/**
 * @Classname MutiPointsTypeHandler
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/7/17 20:36
 * @Created by maicaoboy
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MutiPointsTypeHandler extends BaseTypeHandler<List<List<Map>>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<List<Map>> parameter, JdbcType jdbcType) throws SQLException {
        System.out.println("444444444");
        // 在这里将Java对象转换为数据库可以存储的类型
        // 例如，可以将List<List<Map>>转换为JSON字符串后存储到数据库中
        // 这里只是一个示例，请根据实际情况进行转换
        String json = convertToJson(parameter);
        ps.setString(i, json);
    }

    @Override
    public List<List<Map>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        System.out.println("333333333");
        // 在这里从数据库中获取数据，并将其转换为Java对象
        // 例如，可以从数据库中取出JSON字符串，并将其转换为List<List<Map>>
        // 这里只是一个示例，请根据实际情况进行转换
        String json = rs.getString(columnName);
        return convertFromJson(json);
    }

    @Override
    public List<List<Map>> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        System.out.println("11111111");
        return null;
    }

    @Override
    public List<List<Map>> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        System.out.println("222222222");
        return null;
    }

    // 其他方法省略，可以根据需要实现其他父类方法或自定义方法

    // 将List<List<Map>>转换为JSON字符串的方法
    private String convertToJson(List<List<Map>> list) {
        // 实现转换逻辑
        // 这里只是一个示例，请根据实际情况进行转换
        // 返回转换后的JSON字符串

//        使用jackson将list转化为json字符串
        System.out.println("sss"+list.toString());
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    // 将JSON字符串转换为List<List<Map>>的方法
    private List<List<Map>> convertFromJson(String json) {
        System.out.println("jjjjjj"+json);
//        使用jackson将json字符串转化为list<list<Map>>
        ObjectMapper mapper = new ObjectMapper();
        List<List<Map>> list = null;
        try {
            list = mapper.readValue(json, new TypeReference<List<List<Map>>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
