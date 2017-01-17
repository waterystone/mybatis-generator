
/**
 * ${classModel.desc!''}对应的model
 */
public class ${classModel.uname!''}Model {
    <#list propertyModelList as model>
    /**
     * ${model.desc!''}
     */
    private ${model.type!''} ${model.lname!''};
    </#list>

    <#list propertyModelList as model>
    public ${model.type!''} get${model.uname!''}() {
        return ${model.lname!''};
    }

    public void set${model.uname!''}(${model.type!''} ${model.lname!''}) {
        this.${model.lname!''} = ${model.lname!''};
    }
    </#list>
}
