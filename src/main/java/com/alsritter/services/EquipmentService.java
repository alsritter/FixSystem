package com.alsritter.services;

import com.alsritter.mappers.EquipmentMapper;
import com.alsritter.pojo.Equipment;
import com.alsritter.proportion.WeightInterface;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Service
public class EquipmentService {
    @Resource
    public EquipmentMapper equipmentMapper;
    @Autowired
    public WeightInterface weightInterface;

    @Transactional
    public int addEquipment(
            String ename,
            String eclass,
            Integer egrade,
            Integer eweightGrade,
            String eworkerId,
            String usingUnit,
            String etype,
            String url,
            String address) {
        // 这个 equipment 是空的，单纯是用于当容器（下面也只是填充一个 id 属性）
        Equipment equipment = new Equipment();
        // 注意这里的 equipment，只是用来接收返回的 id 的
        int i = equipmentMapper.addEquipment(ename, eclass, egrade, eweightGrade, eworkerId, usingUnit, etype, url, address, equipment);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }
        return equipment.getId();
    }

    @Transactional
    public void updateEquipment(
            int id,
            String ename,
            String eclass,
            Integer egrade,
            Integer eweightGrade,
            String eworkerId,
            String usingUnit,
            String etype,
            String address) {
        int i = equipmentMapper.updateEquipment(id, ename, eclass, egrade, eweightGrade, eworkerId, usingUnit, etype, address);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void setState(int id, int state) {
        int i = equipmentMapper.setState(id, state);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateDate(int id) {
        int i = equipmentMapper.updateDate(id);
        if (i == 0) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }
    }


    public List<Equipment> getEquipmentList() {
        weightInterface.getWeight();
        return equipmentMapper.getEquipmentList();
    }

    public Equipment getEquipment(int id) {
        Equipment equipment = equipmentMapper.getEquipment(id);
        if (equipment == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }

        return equipment;
    }

    public List<String> getEquipmentClass() {
        return equipmentMapper.getEquipmentClass();
    }

    public List<Equipment> searchEquipment(String usingUnit, String ename, String etype, String eclass, Integer egrade, Integer state) {
        if (usingUnit == null && ename == null && etype == null && eclass == null && egrade == null && state == null) {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "关键字不能全部为空！");
        }
        return equipmentMapper.searchEquipment(usingUnit, ename, etype, eclass, egrade, state);
    }

    public int getEquipmentNumber() {
        return equipmentMapper.getEquipmentNumber();
    }

    public List<Map<String, Object>> getEquipmentStateRatio() {
        return equipmentMapper.getEquipmentStateRatio();
    }

    public List<Map<String, Object>> getEquipmentGradeRatio() {
        return equipmentMapper.getEquipmentGradeRatio();
    }

    public List<Map<String, Object>> getEquipmentClassRatio() {
        return equipmentMapper.getEquipmentClassRatio();
    }
}
