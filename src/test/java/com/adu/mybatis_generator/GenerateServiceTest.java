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
        String dbName = "gaoyang";// DB名称
        String[] tables = {"parent_task", "parent_task_seq", "task", "task_op", "task_project", "task_project_owner", "task_submit", "task_submit_result"};// 要生成代码的表
        String filePath = "/Users/yunjie.du/mybatis_generator";// 目标目录
        generateService.generateCode(dbName, tables, filePath);
    }

    @Before
    public void init() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://10.86.42.140:3306";
        String userName = "adu_w";
        String password = "aduADU123aduADU7";
        MetaInfo metaInfo = new MetaInfo("yunjie.du", new Date());// 作者等信息
        generateService = new GenerateService(driver, url, userName, password, metaInfo);
    }
}
