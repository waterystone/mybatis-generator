package com.adu.mybatis_generator;

import com.adu.mybatis_generator.model.MetaInfo;
import com.adu.mybatis_generator.service.GenerateService;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * @author yunjie.du
 * @date 2017/1/17 18:14
 */
public class GenerateServiceTest {
    private GenerateService generateService;

    @Test
    public void generateCode() {
        String dbName = "srvmgr";// DB名称
        String[] tables = {"t_calleruseageext"};// 要生成代码的表
        String filePath = "/Users/yunjie.du/mybatis_generator";// 目标目录
        generateService.generateCode(dbName, tables, filePath);
    }

    @Before
    public void init() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://10.9.19.205:3306";
        String userName = "srvmgr_rw";
        String password = "123456";
        MetaInfo metaInfo = new MetaInfo("duyunjie", new Date());// 作者等信息
        generateService = new GenerateService(driver, url, userName, password, metaInfo);
    }
}
