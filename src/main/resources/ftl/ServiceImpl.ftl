import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author ${metaInfo.author}
 * @date ${metaInfo.date}
 */
@Service
public class ${tableInfo.uname}ServiceImpl implements ${tableInfo.uname}Service {
    @Resource
    private ${tableInfo.uname}Mapper ${tableInfo.lname}Mapper;

    private static final int DB_QUERY_LIMIT = 5000;//分批查询
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ${tableInfo.uname} get${tableInfo.uname}ById(long id){
        return this.${tableInfo.lname}Mapper.query${tableInfo.uname}ById(id);
    }

    @Override
    public Map<Long, ${tableInfo.uname}> get${tableInfo.uname}sByIds(List<Long> idList){
        Map<Long, ${tableInfo.uname}> res = Maps.newHashMapWithExpectedSize(CollectionUtils.size(idList));
        if (CollectionUtils.isEmpty(idList)) {
            return res;
        }

        for (List<Long> subIdList : CollectionHelper.splitListByRate(idList, DB_QUERY_LIMIT)) {//分批查询
            List<${tableInfo.uname}> ${tableInfo.uname}List = this.${tableInfo.lname}Mapper.query${tableInfo.uname}sByIds(subIdList);
            for(${tableInfo.uname} ${tableInfo.lname} : ${tableInfo.uname}List){
                res.put(${tableInfo.lname}.getId(), ${tableInfo.lname});
            }
        }
        return res;
    }

    @Override
    public int updateById(${tableInfo.uname} ${tableInfo.lname}){
        if(${tableInfo.lname} == null){
            return 0;
        }

        return this.${tableInfo.lname}Mapper.updateById(${tableInfo.lname});
    }

    @Override
    public int save(${tableInfo.uname} ${tableInfo.lname}){
        if(${tableInfo.lname} == null){
            return 0;
        }

        return this.${tableInfo.lname}Mapper.save(${tableInfo.lname});
    }

    @Override
    public void batchSave(List<${tableInfo.uname}> ${tableInfo.lname}List){
        if (CollectionUtils.isEmpty(${tableInfo.lname}List)) {
            return 0;
        }

        return this.${tableInfo.lname}Mapper.batchSave(${tableInfo.lname}List);
    }

    @Override
    public int deleteById(long id){
        return this.${tableInfo.lname}Mapper.deleteById(id);
    }

    @Override
    public int deleteByIds(List<Long> idList){
        return this.${tableInfo.lname}Mapper.deleteByIds(idList);
    }
}