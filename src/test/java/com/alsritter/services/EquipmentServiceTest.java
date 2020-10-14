package com.alsritter.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EquipmentServiceTest {
    @Autowired
    public EquipmentService equipmentService;

    @Test
    public void test() {
        equipmentService.updateEquipment(
                7,
                "监控屏幕",
                "屏幕",
                2,
                360,
                "201625070128",
                "信息学院",
                "B021",
                null);
    }
}