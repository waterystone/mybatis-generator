package com.qunar.mybatis.service;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qunar.mybatis.model.ClassModel;
import com.qunar.mybatis.model.PropertyModel;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ming.zhao on 2015/1/9.
 */
public class GenerateService {
    private static final Logger logger = Logger.getLogger("generateLogger");
    private Connection connection;
    private static Map<String, String> dbTypeAndJavaTypeMap = Maps.newHashMap();
    private static Map<String, String> templateNameMap = Maps.newHashMap();

    public GenerateService(String url, String userName, String pwd) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, pwd);
        } catch (Exception e) {
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

        templateNameMap.put("Model.java", "Model.ftl");
        templateNameMap.put("Mapper.xml", "Mapper.ftl");
        templateNameMap.put("Dao.java", "Dao.ftl");
    }

    private List<PropertyModel> getPropertyModel(String dbName, String tableName) throws Exception {
        String sql = "SELECT COLUMN_NAME,DATA_TYPE,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = ? AND table_name = ?";
        return new QueryRunner().query(connection, sql, new ResultSetHandler<List<PropertyModel>>() {
            public List<PropertyModel> handle(ResultSet rs) throws SQLException {
                List<PropertyModel> propertyModels = Lists.newArrayList();
                while (rs.next()) {
                    PropertyModel model = new PropertyModel();
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

    private ClassModel getClassModel(final String dbName, final String tableName) throws Exception {
        String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES  WHERE table_schema = ? AND table_name = ?";
        return new QueryRunner().query(connection, sql, new ResultSetHandler<ClassModel>() {
            public ClassModel handle(ResultSet rs) throws SQLException {
                if (!rs.next()) {
                    return null;
                }
                ClassModel model = new ClassModel();
                model.setTableName(tableName);
                model.setSchema(dbName);
                model.setLname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName));
                model.setUname(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName));
                model.setDesc(rs.getString(1));
                return model;
            }
        }, dbName, tableName);
    }

    private void generateTemplateContent(String templateName, Map<String, Object> templateObject, String fileName) throws IOException, TemplateException {
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

    private void generateCode(String filePath, String dbName, String tableName) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(filePath));
        Preconditions.checkArgument(StringUtils.isNotBlank(dbName));
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName));

        ClassModel classModel = getClassModel(dbName, tableName);
        List<PropertyModel> propertyModelList = getPropertyModel(dbName, tableName);

        Map<String, Object> templateObject = Maps.newHashMap();
        templateObject.put("classModel", classModel);
        templateObject.put("propertyModelList", propertyModelList);
        String fileDirectory = getFileDirectory(filePath);
        File file = new File(fileDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
        for (Map.Entry<String, String> entry : templateNameMap.entrySet()) {
            String fileName = fileDirectory + classModel.getUname() + entry.getKey();
            generateTemplateContent(entry.getValue(), templateObject, fileName);
        }
    }

    private String getFileDirectory(String filePath) {
        if (StringUtils.endsWith(filePath, File.separator)) {
            return filePath;
        }
        return filePath + File.separator;
    }

    public void generateCode(String dbName, String[] tableNames, String filePath) {
        logger.log(Level.INFO, "开始生成代码");
        try {
            for (String tableName : tableNames) {
                generateCode(filePath, dbName, tableName);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "生成代码出现异常,", e);
            return;
        } finally {
            DbUtils.closeQuietly(connection);
        }
        logger.log(Level.INFO, "生成代码结束");
    }
}
