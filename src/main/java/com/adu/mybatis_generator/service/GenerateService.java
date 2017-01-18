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

import com.adu.mybatis_generator.model.FieldInfo;
import com.adu.mybatis_generator.model.MetaInfo;
import com.adu.mybatis_generator.model.TableInfo;
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
    private MetaInfo metaInfo;

    private Connection connection;
    private static Map<String, String> dbTypeAndJavaTypeMap = Maps.newHashMap();// DB字段类型与Java类型的映射
    private static Map<String, String> templateNameMap = Maps.newHashMap();// 模板文件映射(key为模板文件名报缀，value为模板文件路径)
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public GenerateService(String driver, String url, String userName, String pwd, MetaInfo metaInfo) {
        try {
            // 加载数据库连接
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, pwd);
            this.metaInfo = metaInfo;
        } catch (Exception e) {
            logger.error("[ERROR-getConnection]driver={},url={},userName={},pwd={}", driver, url, userName, pwd);
            throw new RuntimeException("ERROR-getConnection", e);
        }
    }

    static {
        dbTypeAndJavaTypeMap.put("boolean", "Boolean");
        dbTypeAndJavaTypeMap.put("char", "String");
        dbTypeAndJavaTypeMap.put("tinyint", "Integer");
        dbTypeAndJavaTypeMap.put("smallint", "Integer");
        dbTypeAndJavaTypeMap.put("mediumint", "Integer");
        dbTypeAndJavaTypeMap.put("int", "Integer");
        dbTypeAndJavaTypeMap.put("bigint", "Long");

        dbTypeAndJavaTypeMap.put("float", "Float");
        dbTypeAndJavaTypeMap.put("double", "Double");
        dbTypeAndJavaTypeMap.put("decimal", "BigDecimal");

        dbTypeAndJavaTypeMap.put("varchar", "String");
        dbTypeAndJavaTypeMap.put("tinytext", "String");
        dbTypeAndJavaTypeMap.put("text", "String");
        dbTypeAndJavaTypeMap.put("mediumtext", "String");
        dbTypeAndJavaTypeMap.put("longtext", "String");

        dbTypeAndJavaTypeMap.put("datetime", "Date");
        dbTypeAndJavaTypeMap.put("timestamp", "Date");
        dbTypeAndJavaTypeMap.put("date", "Date");

        templateNameMap.put(".java", "ftl/Model.ftl");// model.java
        templateNameMap.put("Mapper.xml", "ftl/Mapper.ftl");// mapper.java
        templateNameMap.put("Mapper.java", "ftl/Dao.ftl");// mapper.xml
    }

    /**
     * 自动生成代码（多表）
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

    /**
     * 自动生成代码（单表）
     * 
     * @param dirPath
     * @param dbName
     * @param tableName
     * @throws Exception
     */
    private void generateCode(String dirPath, String dbName, String tableName) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(dirPath));
        Preconditions.checkArgument(StringUtils.isNotBlank(dbName));
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName));

        TableInfo tableInfo = getTableInfo(dbName, tableName);// 获取表信息
        List<FieldInfo> fieldInfoList = getFieldInfos(dbName, tableName);// 获取表内各字段信息

        Map<String, Object> templateObject = buildTemplateData(tableInfo, fieldInfoList);// 构建模板数据

        // 对每个模板，生成相应的文件
        for (Map.Entry<String, String> entry : templateNameMap.entrySet()) {
            String fileName = dirPath + tableInfo.getUname() + entry.getKey();// 文件名
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
    private TableInfo getTableInfo(final String dbName, final String tableName) throws Exception {
        String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES  WHERE table_schema = ? AND table_name = ?";
        return new QueryRunner().query(connection, sql, new ResultSetHandler<TableInfo>() {
            public TableInfo handle(ResultSet rs) throws SQLException {
                if (!rs.next()) {
                    return null;
                }
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setSchema(dbName);
                tableInfo.setLname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName));
                tableInfo.setUname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName));
                tableInfo.setDesc(rs.getString(1));
                return tableInfo;
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
    private List<FieldInfo> getFieldInfos(String dbName, String tableName) throws Exception {
        String sql = "SELECT COLUMN_NAME,DATA_TYPE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = ? AND table_name = ?";
        return new QueryRunner().query(connection, sql, new ResultSetHandler<List<FieldInfo>>() {
            public List<FieldInfo> handle(ResultSet rs) throws SQLException {
                List<FieldInfo> propertyModels = Lists.newArrayList();
                while (rs.next()) {
                    FieldInfo fieldInfo = new FieldInfo();
                    String columnName = rs.getString(1);
                    fieldInfo.setColumnName(columnName);
                    fieldInfo.setLname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
                    fieldInfo.setUname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, columnName));

                    String dbType = rs.getString(2);
                    if (!dbTypeAndJavaTypeMap.containsKey(dbType)) {
                        throw new RuntimeException(String.format("DB类型[%s]没有对应的Java类型！！", dbType));
                    }
                    fieldInfo.setType(dbTypeAndJavaTypeMap.get(dbType));
                    fieldInfo.setDesc(rs.getString(3));
                    propertyModels.add(fieldInfo);
                }
                return propertyModels;
            }
        }, dbName, tableName);
    }

    /**
     * 构建模板数据
     * 
     * @param tableInfo 表信息
     * @param fieldInfoList 表内各字段信息
     * @return
     */
    private Map<String, Object> buildTemplateData(TableInfo tableInfo, List<FieldInfo> fieldInfoList) {
        Map<String, Object> res = Maps.newHashMap();// 模板数据
        res.put("metaInfo", metaInfo);
        res.put("tableInfo", tableInfo);
        res.put("fieldInfoList", fieldInfoList);

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
