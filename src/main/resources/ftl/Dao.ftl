import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * ${tableInfo.desc} 相关操作Dao
 *
 * @author ${metaInfo.author}
 * @date ${metaInfo.date}
 */
public interface ${tableInfo.uname}Mapper {
    /**
    * 根据id查询
    *
    * @param id
    * @return
    */
    ${tableInfo.uname} query${tableInfo.uname}ById(@Param("id") long id);

    /**
    * 根据id批量查询
    *
    * @param idList
    * @return
    */
    List<${tableInfo.uname}> query${tableInfo.uname}sByIds(List<Long> idList);

    /**
    * 更新数据
    *
    * @param ${tableInfo.lname}
    * @return 更新条数
    */
    int updateById(${tableInfo.uname} ${tableInfo.lname});

    /**
     * 保存单条数据
     *
     * @param ${tableInfo.lname}
     * @return 保存条数
     */
    int save(${tableInfo.uname} ${tableInfo.lname});

    /**
     * 批量保存
     *
     * @param ${tableInfo.lname}List
     * @return 保存条数
     */
    int batchSave(List<${tableInfo.uname}> ${tableInfo.lname}List);

    /**
     * 根据ID单条删除
     *
     * @param id 要删除的记录ID
     * @return 删除条数
     */
    int deleteById(@Param("id") long id);

    /**
     * 根据ID批量删除
     *
     * @param idList 要删除的记录ID列表
     * @return 删除条数
     */
    int deleteByIds(List<Long> idList);
}