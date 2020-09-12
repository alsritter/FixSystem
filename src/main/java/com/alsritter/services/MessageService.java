package com.alsritter.services;

import com.alsritter.mappers.MessageMapper;
import com.alsritter.pojo.Message;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageService {

    @Resource
    public MessageMapper messageMapper;

    public List<Message> getMessageList(){
        return messageMapper.getMessageList();
    }
}
