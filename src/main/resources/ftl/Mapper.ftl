${r'<?xml version="1.0" encoding="UTF-8"?>'}
${r'<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">'}
${r'<mapper namespace="TODO:需要替换成dao的完全限定名">'}
    <resultMap id="${tableInfo.lname}ResultMap" type="${tableInfo.uname}">
    <#list fieldInfoList as fieldInfo>
        <result column="${fieldInfo.columnName}" property="${fieldInfo.lname}" />
    </#list>
    </resultMap>

    <sql id="selectFields" >
        <#list fieldInfoList as fieldInfo>${fieldInfo.columnName}<#sep>, </#sep></#list>
    </sql>

    <select id="query${tableInfo.uname}ById" resultMap="${tableInfo.lname}ResultMap">
        SELECT
            <include refid="selectFields"/>
        FROM ${tableInfo.schema}.${tableInfo.tableName}
        WHERE id = ${r'#{id}'}
    </select>

    <select id="query${tableInfo.uname}sByIds" resultMap="${tableInfo.lname}ResultMap">
        SELECT
            <include refid="selectFields"/>
        FROM ${tableInfo.schema}.${tableInfo.tableName}
        WHERE id IN
            <foreach collection="list" open="(" close=")" item="item"  separator=",">
                ${r'#{item}'}
            </foreach>'
    </select>

    <update id="updateById">
        UPDATE ${tableInfo.schema}.${tableInfo.tableName}
        SET
        <#list fieldInfoList as fieldInfo>
            <#if fieldInfo.columnName != "id">
            ${fieldInfo.columnName} = ${r'#{'}${fieldInfo.lname}${r'}'}<#if fieldInfo_has_next>,</#if>
            </#if>
        </#list>
        WHERE id = ${r'#{id}'}
    </update>

    <insert id="save" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO ${tableInfo.schema}.${tableInfo.tableName}
        (
        <#list fieldInfoList as fieldInfo>
            <#if fieldInfo.columnName != "id">
            ${fieldInfo.columnName}<#if fieldInfo_has_next>,</#if>
            </#if>
        </#list>
        )VALUES(
        <#list fieldInfoList as fieldInfo>
            <#if fieldInfo.lname != "id">
            ${r'#{'}${fieldInfo.lname}${r'}'}<#if fieldInfo_has_next>,</#if>
            </#if>
        </#list>
        )
    </insert>

    <insert id="batchSave" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO ${tableInfo.schema}.${tableInfo.tableName}
        (
        <#list fieldInfoList as fieldInfo>
            <#if fieldInfo.columnName != "id">
            ${fieldInfo.columnName}<#if fieldInfo_has_next>,</#if>
            </#if>
        </#list>
        )VALUES
        <foreach collection="list" item="item"  separator=",">
        (
        <#list fieldInfoList as fieldInfo>
            <#if fieldInfo.lname != "id">
            ${r'#{item.'}${fieldInfo.lname}${r'}'}<#if fieldInfo_has_next>,</#if>
            </#if>
        </#list>
        )
        </foreach>
    </insert>

    <delete id="deleteById">
        DELETE FROM ${tableInfo.schema}.${tableInfo.tableName} WHERE id = ${r'#{id}'}
    </delete>

    <delete id="deleteByIds">
        DELETE FROM ${tableInfo.schema}.${tableInfo.tableName}
        WHERE id IN
            <foreach collection="list" open="(" close=")" item="item"  separator=",">
                ${r'#{item}'}
            </foreach>
    </delete>

${r'</mapper>'}