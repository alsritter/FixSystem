package com.alsritter.services;

import com.alsritter.mappers.ToolMapper;
import com.alsritter.pojo.Tool;
import com.alsritter.utils.MyDBError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ToolService {
    @Resource
    public ToolMapper toolMapper;

    public List<Tool> getToolList(){
        return toolMapper.getToolList();
    }

    @Transactional
    public int updateTool(int toolId, int toolCount) {
        int i = 0;
        try {
            i = toolMapper.updateTool(toolId, toolCount);
        } catch (RuntimeException e) {
            throw new MyDBError("更新工具数据出错", e);
        }
        return i;
    }

    @Transactional
    public int createTool(String toolName, int toolCount){
        int i = 0;
        try {
            i = toolMapper.createTool(toolName, toolCount);
        } catch (RuntimeException e) {
            throw new MyDBError("更新管理员的数据出现问题", e);
        }
        return i;
    }


    @Transactional
    public int deleteTool(int toolId) {
        int i = 0;
        try {
            i = toolMapper.deleteTool(toolId);
        } catch (RuntimeException e) {
            throw new MyDBError("删除工具失败", e);
        }
        return i;
    }
}
