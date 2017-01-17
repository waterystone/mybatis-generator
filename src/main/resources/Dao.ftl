import java.util.List;
import java.util.Map;

/**
 * ${classModel.desc!''} dao层操作接口
 */
public interface ${classModel.uname!''}Dao {
    /**
     * 保存${classModel.desc!''}数据
     *
     * @param model
     */
    int save${classModel.uname!''}Model(${classModel.uname!''}Model model);

    /**
     * 批量保存${classModel.desc!''}数据
     *
     * @param list
     */
    void batchSave${classModel.uname!''}Models(List${r'<'}${classModel.uname!''}Model${r'>'} list);

    /**
     * 删除${classModel.desc!''}数据
     *
     * @param id 操作条数
     */
    int delete${classModel.uname!''}ModelById(int id);

    /**
     * 批量删除${classModel.desc!''}数据
     *
     * @param list
     * @return int 操作条数
     */
    int delete${classModel.uname!''}ModelByIds(List${r'<Integer>'} list);

    /**
     * 更新${classModel.desc!''}数据
     *
     * @param model
     */
    int update${classModel.uname!''}ModelById(${classModel.uname!''}Model model);

    /**
     * 根据id获取${classModel.desc!''}数据
     *
     * @param id
     */
    ${classModel.uname!''}Model select${classModel.uname!''}ModelById(int id);
}