${r'<?xml version="1.0" encoding="UTF-8"?>'}
${r'<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">'}
${r'<mapper namespace="需要替换成dao的完全限定名">'}

    ${r'<insert id="save'}${classModel.uname!''}${r'Model" parameterType="'}${classModel.uname!''}${r'Model" useGeneratedKeys="true" keyProperty="id">'}
        insert into ${classModel.schema!''}.${classModel.tableName!''}
        (
        <#list propertyModelList as model>
            <#if model.columnName == "id">
            <#else>
            ${model.columnName!''}<#if model_has_next>,</#if>
            </#if>
        </#list>
        )values(
        <#list propertyModelList as model>
            <#if model.lname == "id">
            <#else>
            ${r'#{'}${model.lname!''}${r'}'}<#if model_has_next>,</#if>
            </#if>
        </#list>
        )
    ${r'</insert>'}

    ${r'<insert id="batchSave'}${classModel.uname!''}${r'Models" parameterType="java.util.List">'}
        insert into ${classModel.schema!''}.${classModel.tableName!''}
        (
        <#list propertyModelList as model>
            <#if model.columnName == "id">
            <#else>
            ${model.columnName!''}<#if model_has_next>,</#if>
            </#if>
        </#list>
        )values
        ${r'<foreach collection="list" item="item"  separator="," >'}
        (<#list propertyModelList as model>
            <#if model.lname == "id"><#else>
            ${r'#{item.'}${model.lname!''}${r'}'}<#if model_has_next>,</#if>
            </#if>
        </#list>)
        ${r'</foreach>'}
    ${r'</insert>'}

    ${r'<delete id="delete'}${classModel.uname!''}${r'ModelById" parameterType="Integer">'}
        delete from ${classModel.schema!''}.${classModel.tableName!''} ${r'where id = #{id}'}
    ${r'</delete>'}

    ${r'<delete id="delete'}${classModel.uname!''}${r'ModelByIds"'}${r' parameterType="java.util.List">'}
        delete from ${classModel.schema!''}.${classModel.tableName!''} ${r'where id in('}
        ${r'<foreach collection="list" item="item"  separator="," >'}
            ${r'#{item}'}
        ${r'</foreach>)'}
    ${r'</delete>'}

    ${r'<update id="update'}${classModel.uname!''}${r'ModelById" parameterType="'}${classModel.uname!''}${r'Model">'}
        update
            ${classModel.schema!''}.${classModel.tableName!''}
        set
        <#list propertyModelList as model>
            <#if model.columnName == "id">
            <#else>
            ${model.columnName!''} = ${r'#{'}${model.lname!''}${r'}'}<#if model_has_next>,</#if>
            </#if>
        </#list>
        ${r'where id = #{id}'}
    ${r'</update>'}

    ${r'<sql id="selectFields" >'}
        <#list propertyModelList as model>
        ${model.columnName!''} as ${model.lname!''} <#if model_has_next>,</#if>
    </#list>
    ${r'</sql>'}
    ${r'<select id="select'}${classModel.uname!''}${r'ModelById" parameterType="Integer" resultType="'}${classModel.uname!''}${r'Model">'}
        SELECT
        ${r'<include refid="selectFields"/>'}
        FROM
            ${classModel.schema!''}.${classModel.tableName!''}
        ${r'where id = #{id}'}
    ${r'</select>'}
${r'</mapper>'}