package com.michael.async.handler;

import com.michael.async.EventHandler;
import com.michael.async.EventModel;
import com.michael.async.EventProducer;
import com.michael.async.EventType;
import com.michael.model.Message;
import com.michael.service.MessageService;
import com.michael.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by GWC on 2016/7/21.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        //judge illeagle login or not
        if (false) {
            Message message = new Message();
            message.setToId(model.getActorId());
            message.setContent("Login ip exception");
            message.setFromId(3);
            message.setCreatedDate(new Date());
            messageService.addMessage(message);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", model.getExt("username"));
            mailSender.sendWithHTMLTemplate(model.getExt("email"), "Login Exception", "mails/loginException.html", map);
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
