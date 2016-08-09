package com.michael.async;

import com.alibaba.fastjson.JSONObject;
import com.michael.util.JedisAdapter;
import com.michael.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by GWC on 2016/7/21.
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);


    public boolean fireEvent(EventModel model) {
        try {
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            logger.error("FireEvent Exception" + e.getMessage());
            return false;
        }
    }
}
