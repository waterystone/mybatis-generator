package com.adu.mybatis_generator.model;

import com.adu.mybatis_generator.util.Stringfy;

/**
 * 无数据信息
 * 
 * @author yunjie.du
 * @date 2017/1/18 10:37
 */
public class MetaInfo extends Stringfy {
    private String author;// 作者
    private String date;// 生成日期

    public MetaInfo(String author, String date) {
        this.author = author;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
