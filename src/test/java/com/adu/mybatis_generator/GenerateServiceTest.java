package com.adu.mybatis_generator;

import org.junit.Before;
import org.junit.Test;

import com.adu.mybatis_generator.service.GenerateService;

/**
 * @author yunjie.du
 * @date 2017/1/17 18:14
 */
public class GenerateServiceTest {
    private GenerateService generateService;

    @Test
    public void generateCode() {
        String dbName = "adu";
        String[] tables = { "user_info" };
        String filePath = "D:/mybatis_generator";
        generateService.generateCode(dbName, tables, filePath);
    }

    @Before
    public void init() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://l-ordermovie2.des.dev.cn0.qunar.com:3306";
        String userName = "adu_w";
        String password = "aduADU123aduADU1";
        generateService = new GenerateService(driver, url, userName, password);
    }
}
