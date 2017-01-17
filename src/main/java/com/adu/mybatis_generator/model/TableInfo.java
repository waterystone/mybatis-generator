package com.adu.mybatis_generator.model;

/**
 * 表信息
 * 
 * @author yunjie.du
 * @date 2017/1/17 18:14
 */
public class TableInfo {
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 库名称
     */
    private String schema;
    /**
     * 首字母小写类名称，
     */
    private String lname;
    /**
     * 首字母大写类名称，
     */
    private String uname;
    /**
     * 类描述
     */
    private String desc;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
