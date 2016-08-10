package com.michael.async.handler;

import com.michael.async.EventHandler;
import com.michael.async.EventModel;
import com.michael.async.EventType;
import com.michael.model.Message;
import com.michael.model.User;
import com.michael.service.MessageService;
import com.michael.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by GWC on 2016/7/21.
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        System.out.println("liked");
        Message message = new Message();
        message.setFromId(3);//system user
        message.setToId(model.getEntityOwnerId());
        //message.setToId(model.getActorId());// send a message to myself
        User user = userService.getUser(model.getActorId());
        message.setContent("User " + user.getName() +
                " like your news, http://127.0.0.1:8080/news/" + model.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
