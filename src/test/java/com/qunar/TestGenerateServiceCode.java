package com.qunar;

import com.qunar.mybatis.service.GenerateService;

/**
 * Created by renqun.yuan on 2016/4/4.
 */
public class TestGenerateServiceCode {
    public static void main(String[] args) {
        String url = "jdbc:mysql://l-ordermovie2.des.dev.cn0.qunar.com:3306";
        String userName = "adu_w";
        String password = "aduADU123aduADU1";
        GenerateService generateService = new GenerateService(url, userName, password);
        String dbName = "adu";
        String[] tables = {"user_info"};
        String filePath = "D:/mybatis";
        generateService.generateCode(dbName, tables, filePath);
    }
}
