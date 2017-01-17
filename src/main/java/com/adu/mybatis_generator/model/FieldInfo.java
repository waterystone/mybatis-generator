package com.adu.mybatis_generator.model;

/**
 * 表内的字段信息
 * 
 * @author yunjie.du
 * @date 2017/1/17 18:14
 */
public class FieldInfo {
    /**
     * 表字段名称
     */
    private String columnName;
    /**
     * 首字母小写属性名称，
     */
    private String lname;
    /**
     * 属性类型
     */
    private String type;
    /**
     * 属性描述
     */
    private String desc;
    /**
     * 首字母大写属性名称，
     */
    private String uname;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
