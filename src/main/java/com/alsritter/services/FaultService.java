package com.alsritter.services;

import com.alsritter.mappers.FaultMapper;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FaultService {
    @Resource
    public FaultMapper faultMapper;

    public List<String> getFaultClassList(){
        return faultMapper.getFaultClassList();
    }

    // 检查这个错误类型是不是在数据库里的
    public void isExist(String faultName){
        String exist = faultMapper.isExist(faultName);
        if (exist == null) {
            throw new BizException(CommonEnum.BAD_REQUEST);
        }
    }
}
