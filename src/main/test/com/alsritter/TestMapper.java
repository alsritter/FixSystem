package com.alsritter;

import com.alsritter.controller.AdminController;
import com.alsritter.mappers.WorkerMapper;
import com.alsritter.model.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class TestMapper {
    @Resource
    public WorkerMapper workerMapper;

    @Autowired
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    private AdminController adminController;

    @Test
    public void test(){
        List<Map<String, Object>> faultClassCount = workerMapper.getFaultClassCount("201895070999");
        faultClassCount.forEach(o->{
            o.forEach((k,v)->{
                log.debug(v.toString());
            });
        });
    }

    @Test
    public void testController(){
        ResponseTemplate<JSONObject> worker = adminController.getWorker("201895070999");
    }
}
