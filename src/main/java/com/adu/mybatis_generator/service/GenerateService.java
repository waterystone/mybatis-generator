package com.adu.mybatis_generator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adu.mybatis_generator.model.TableInfo;
import com.adu.mybatis_generator.model.FieldInfo;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 代码生成服务
 * 
 * @author yunjie.du
 * @date 2017/1/17 18:14
 */
public class GenerateService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Connection connection;
    private static Map<String, String> dbTypeAndJavaTypeMap = Maps.newHashMap();// DB字段类型与Java类型的映射
    private static Map<String, String> templateNameMap = Maps.newHashMap();// 模板文件映射(key为模板文件名报缀，value为模板文件路径)

    public GenerateService(String driver, String url, String userName, String pwd) {
        try {
            // 加载数据库连接
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, pwd);
        } catch (Exception e) {
            logger.error("[ERROR-getConnection]driver={},url={},userName={},pwd={}", driver, url, userName, pwd);
            throw new RuntimeException("ERROR-getConnection", e);
        }
    }

    static {
        dbTypeAndJavaTypeMap.put("bigint", "Long");
        dbTypeAndJavaTypeMap.put("boolean", "Boolean");
        dbTypeAndJavaTypeMap.put("char", "String");
        dbTypeAndJavaTypeMap.put("varchar", "String");
        dbTypeAndJavaTypeMap.put("datetime", "Date");
        dbTypeAndJavaTypeMap.put("timestamp", "Date");
        dbTypeAndJavaTypeMap.put("date", "Date");
        dbTypeAndJavaTypeMap.put("int", "Integer");
        dbTypeAndJavaTypeMap.put("smallint", "Integer");
        dbTypeAndJavaTypeMap.put("tinyint", "Integer");
        dbTypeAndJavaTypeMap.put("decimal", "BigDecimal");

        templateNameMap.put(".java", "ftl/Model.ftl");// model.java
        templateNameMap.put("Mapper.xml", "ftl/Mapper.ftl");// mapper.java
        templateNameMap.put("Mapper.java", "ftl/Dao.ftl");// mapper.xml
    }

    /**
     * 自动生成代码
     *
     * @param dbName DB名
     * @param tableNames 要生成的表数组
     * @param dirPath 目标目录
     */
    public void generateCode(String dbName, String[] tableNames, String dirPath) {
        logger.info("op=start_generateCode,dbName={},tableNames={},dirPath={}", dbName, tableNames, dirPath);
        try {
            dirPath = getFileDirectory(dirPath);
            File file = new File(dirPath);

            if (!file.exists()) {
                file.mkdir();
            }

            for (String tableName : tableNames) {
                generateCode(dirPath, dbName, tableName);
            }
        } catch (Exception e) {
            logger.error("[ERROR-generateCode]", e);
            return;
        } finally {
            DbUtils.closeQuietly(connection);
        }
        logger.info("op=end_generateCode,dbName={},tableNames={},filePath={}", dbName, tableNames, dirPath);
    }

    private void generateCode(String dirPath, String dbName, String tableName) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(dirPath));
        Preconditions.checkArgument(StringUtils.isNotBlank(dbName));
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName));

        TableInfo classModel = getClassModel(dbName, tableName);// 获取表信息
        List<FieldInfo> propertyModelList = getPropertyModel(dbName, tableName);// 获取表内各字段信息

        Map<String, Object> templateObject = buildTemplateData(classModel, propertyModelList);// 构建模板数据

        // 对每个模板，生成相应的文件
        for (Map.Entry<String, String> entry : templateNameMap.entrySet()) {
            String fileName = dirPath + classModel.getUname() + entry.getKey();// 文件名
            generateTemplateContent(entry.getValue(), templateObject, fileName);
        }
    }

    /**
     * 获取表的属性信息
     *
     * @param dbName
     * @param tableName
     * @return
     * @throws Exception
     */
    private TableInfo getClassModel(final String dbName, final String tableName) throws Exception {
        String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES  WHERE table_schema = ? AND table_name = ?";
        return new QueryRunner().query(connection, sql, new ResultSetHandler<TableInfo>() {
            public TableInfo handle(ResultSet rs) throws SQLException {
                if (!rs.next()) {
                    return null;
                }
                TableInfo model = new TableInfo();
                model.setTableName(tableName);
                model.setSchema(dbName);
                model.setLname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName));
                model.setUname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName));
                model.setDesc(rs.getString(1));
                return model;
            }
        }, dbName, tableName);
    }

    /**
     * 获取表各字段的属性信息
     * 
     * @param dbName
     * @param tableName
     * @return
     * @throws Exception
     */
    private List<FieldInfo> getPropertyModel(String dbName, String tableName) throws Exception {
        String sql = "SELECT COLUMN_NAME,DATA_TYPE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = ? AND table_name = ?";
        return new QueryRunner().query(connection, sql, new ResultSetHandler<List<FieldInfo>>() {
            public List<FieldInfo> handle(ResultSet rs) throws SQLException {
                List<FieldInfo> propertyModels = Lists.newArrayList();
                while (rs.next()) {
                    FieldInfo model = new FieldInfo();
                    String columnName = rs.getString(1);
                    model.setColumnName(columnName);
                    model.setLname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
                    model.setUname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, columnName));
                    model.setType(dbTypeAndJavaTypeMap.get(rs.getString(2)));
                    model.setDesc(rs.getString(3));
                    propertyModels.add(model);
                }
                return propertyModels;
            }
        }, dbName, tableName);
    }

    /**
     * 构建模板数据
     * 
     * @param classModel 表信息
     * @param propertyModelList 表内各字段信息
     * @return
     */
    private Map<String, Object> buildTemplateData(TableInfo classModel, List<FieldInfo> propertyModelList) {
        Map<String, Object> res = Maps.newHashMap();// 模板数据
        res.put("classModel", classModel);
        res.put("propertyModelList", propertyModelList);
        return res;
    }

    /**
     * 模板生成文件
     * 
     * @param templateName 模板名
     * @param templateObject 模板数据
     * @param fileName 目标文件名
     * @throws IOException
     * @throws TemplateException
     */
    private void generateTemplateContent(String templateName, Map<String, Object> templateObject, String fileName)
            throws IOException, TemplateException {
        if (MapUtils.isEmpty(templateObject)) {
            return;
        }

        // 读取模板并设置模板内容
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));
        configuration.setEncoding(Locale.CHINA, "UTF-8");
        Template template = configuration.getTemplate(templateName);

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
        template.process(templateObject, writer);

        IOUtils.closeQuietly(writer);
    }

    /**
     * 目录尾加上/
     * 
     * @param filePath
     * @return
     */
    private String getFileDirectory(String filePath) {
        if (StringUtils.endsWith(filePath, File.separator)) {
            return filePath;
        }
        return filePath + File.separator;
    }

}
