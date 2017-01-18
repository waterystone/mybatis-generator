import java.util.List;
import java.util.Map;

/**
 * ${tableInfo.desc!''} dao层操作接口
 *
 * @author ${metaInfo.author!''}
 * @date ${metaInfo.date!''}
 */
public interface ${tableInfo.uname!''}Mapper {
    /**
     * 保存${tableInfo.desc!''}数据
     *
     * @param ${tableInfo.lname!''}
     */
    int save${tableInfo.uname!''}(${tableInfo.uname!''} ${tableInfo.lname!''});

    /**
     * 批量保存${tableInfo.desc!''}数据
     *
     * @param list
     */
    void batchSave${tableInfo.uname!''}Models(List${r'<'}${tableInfo.uname!''}Model${r'>'} list);

    /**
     * 删除${tableInfo.desc!''}数据
     *
     * @param id 操作条数
     */
    int delete${tableInfo.uname!''}ModelById(int id);

    /**
     * 批量删除${tableInfo.desc!''}数据
     *
     * @param list
     * @return int 操作条数
     */
    int delete${tableInfo.uname!''}ModelByIds(List${r'<Integer>'} list);

    /**
     * 更新${tableInfo.desc!''}数据
     *
     * @param model
     */
    int update${tableInfo.uname!''}ModelById(${tableInfo.uname!''}Model model);

    /**
     * 根据id获取${tableInfo.desc!''}数据
     *
     * @param id
     */
    ${tableInfo.uname!''}Model select${tableInfo.uname!''}ModelById(int id);
}