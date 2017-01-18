/**
 * ${tableInfo.desc}对应的model
 *
 * @author ${metaInfo.author}
 * @date ${metaInfo.date?string("yyyy-MM-dd HH:mm:ss")}
 */
public class ${tableInfo.uname} extends Stringfy {
    <#list fieldInfoList as fieldInfo>
    private ${fieldInfo.type} ${fieldInfo.lname};//${fieldInfo.desc}
    </#list>

    <#list fieldInfoList as fieldInfo>
    public ${fieldInfo.type} get${fieldInfo.uname}() {
        return this.${fieldInfo.lname};
    }

    public void set${fieldInfo.uname}(${fieldInfo.type} ${fieldInfo.lname}) {
        this.${fieldInfo.lname} = ${fieldInfo.lname};
    }

    </#list>
}
