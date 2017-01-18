${r'<?xml version="1.0" encoding="UTF-8"?>'}
${r'<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">'}
${r'<mapper namespace="需要替换成dao的完全限定名">'}

    ${r'<insert id="save'}${tableInfo.uname!''}${r'" useGeneratedKeys="true" keyProperty="id">'}
        INSERT INTO ${tableInfo.schema!''}.${tableInfo.tableName!''}
        (
        <#list fieldInfoList as fieldInfo>
            <#if fieldInfo.columnName == "id">
            <#else>
            ${fieldInfo.columnName!''}<#if fieldInfo_has_next>,</#if>
            </#if>
        </#list>
        )VALUES(
        <#list fieldInfoList as fieldInfo>
            <#if fieldInfo.lname == "id">
            <#else>
            ${r'#{'}${fieldInfo.lname!''}${r'}'}<#if fieldInfo_has_next>,</#if>
            </#if>
        </#list>
        )
    ${r'</insert>'}

    ${r'<insert id="batchSave'}${tableInfo.uname!''}${r'Models" parameterType="java.util.List">'}
        insert into ${tableInfo.schema!''}.${tableInfo.tableName!''}
        (
        <#list fieldInfoList as model>
            <#if model.columnName == "id">
            <#else>
            ${model.columnName!''}<#if model_has_next>,</#if>
            </#if>
        </#list>
        )values
        ${r'<foreach collection="list" item="item"  separator="," >'}
        (<#list fieldInfoList as model>
            <#if model.lname == "id"><#else>
            ${r'#{item.'}${model.lname!''}${r'}'}<#if model_has_next>,</#if>
            </#if>
        </#list>)
        ${r'</foreach>'}
    ${r'</insert>'}

    ${r'<delete id="delete'}${tableInfo.uname!''}${r'ModelById" parameterType="Integer">'}
        delete from ${tableInfo.schema!''}.${tableInfo.tableName!''} ${r'where id = #{id}'}
    ${r'</delete>'}

    ${r'<delete id="delete'}${tableInfo.uname!''}${r'ModelByIds"'}${r' parameterType="java.util.List">'}
        delete from ${tableInfo.schema!''}.${tableInfo.tableName!''} ${r'where id in('}
        ${r'<foreach collection="list" item="item"  separator="," >'}
            ${r'#{item}'}
        ${r'</foreach>)'}
    ${r'</delete>'}

    ${r'<update id="update'}${tableInfo.uname!''}${r'ModelById" parameterType="'}${tableInfo.uname!''}${r'Model">'}
        update
            ${tableInfo.schema!''}.${tableInfo.tableName!''}
        set
        <#list fieldInfoList as model>
            <#if model.columnName == "id">
            <#else>
            ${model.columnName!''} = ${r'#{'}${model.lname!''}${r'}'}<#if model_has_next>,</#if>
            </#if>
        </#list>
        ${r'where id = #{id}'}
    ${r'</update>'}

    ${r'<sql id="selectFields" >'}
        <#list fieldInfoList as model>
        ${model.columnName!''} as ${model.lname!''} <#if model_has_next>,</#if>
    </#list>
    ${r'</sql>'}
    ${r'<select id="select'}${tableInfo.uname!''}${r'ModelById" parameterType="Integer" resultType="'}${tableInfo.uname!''}${r'Model">'}
        SELECT
        ${r'<include refid="selectFields"/>'}
        FROM
            ${tableInfo.schema!''}.${tableInfo.tableName!''}
        ${r'where id = #{id}'}
    ${r'</select>'}
${r'</mapper>'}