package com.michael.async;

import java.util.List;

/**
 * Created by GWC on 2016/7/21.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();//当前handler还要关注哪些事件
}
