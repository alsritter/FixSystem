package com.alsritter.services;

import com.alsritter.mappers.MessageMapper;
import com.alsritter.pojo.Message;
import com.alsritter.utils.MyDBError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageService {

    @Resource
    public MessageMapper messageMapper;

    public List<Message> getMessageList(){
        return messageMapper.getMessageList();
    }

    @Transactional
    public int pushMessage(String adminId,String message){
        int i = 0;
        try {
            i = messageMapper.pushMessage(adminId, message);
        } catch (RuntimeException e) {
            throw new MyDBError("推送消息失败，失败原因：", e);
        }
        return i;
    }
}
