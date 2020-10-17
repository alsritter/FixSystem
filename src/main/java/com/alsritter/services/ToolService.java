package com.alsritter.services;

import com.alsritter.mappers.ToolMapper;
import com.alsritter.pojo.Tool;
import com.alsritter.utils.MyDBError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class ToolService {
    @Resource
    public ToolMapper toolMapper;

    public List<Tool> getToolList(){
        return toolMapper.getToolList();
    }

    public int getToolNumber() {
        return toolMapper.getToolNumber();
    }

    public float getToolSumPrice() {
        return toolMapper.getToolSumPrice();
    }

    @Transactional
    public int updateTool(int toolId, String toolName, int toolCount, float price) {
        int i = 0;
        try {
            i = toolMapper.updateTool(toolId,toolName, toolCount,price);
        } catch (RuntimeException e) {
            throw new MyDBError("更新工具数据出错", e);
        }
        return i;
    }

    @Transactional
    public int createTool(String toolName, int toolCount, float price){
        int i = 0;
        try {
            i = toolMapper.createTool(toolName, toolCount, price);
        } catch (RuntimeException e) {
            throw new MyDBError("更新工具数据出错", e);
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


    public List<Map<String, Integer>> getToolNumberRatio() {
        return toolMapper.getToolNumberRatio();
    }

    public List<Map<String, Object>> getToolPriceRatio() {
        return toolMapper.getToolPriceRatio();
    }

    public List<Map<String, Object>> getToolSumPriceRatio() {
        return toolMapper.getToolSumPriceRatio();
    }

    public Double getAllToolSumPrice() {
        return toolMapper.getAllToolSumPrice();
    }
}
