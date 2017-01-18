package com.adu.mybatis_generator;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;

import com.adu.mybatis_generator.model.MetaInfo;
import com.adu.mybatis_generator.service.GenerateService;

/**
 * @author yunjie.du
 * @date 2017/1/17 18:14
 */
public class GenerateServiceTest {
    private GenerateService generateService;

    @Test
    public void generateCode() {
        String dbName = "adu";// DB名称
        String[] tables = { "user_info" };// 要生成代码的表
        String filePath = "D:/mybatis_generator";// 目标目录
        generateService.generateCode(dbName, tables, filePath);
    }

    @Before
    public void init() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://10.86.42.140:3306";
        String userName = "adu_w";
        String password = "aduADU123aduADU7";
        MetaInfo metaInfo = new MetaInfo("yunjie.du", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));// 作者等信息
        generateService = new GenerateService(driver, url, userName, password, metaInfo);
    }
}
