package com.michael.service;

import com.michael.dao.LoginTicketDAO;
import com.michael.dao.UserDAO;
import com.michael.model.LoginTicket;
import com.michael.model.User;
import com.michael.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by GWC on 2016/7/6.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    //Register
    //注册不成功应该返回一些信息，这里用map存储
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        //empty or not
        if (StringUtils.isEmpty(username)) {
            map.put("msgname", "Username can not be empty");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msgpwd", "Password can not be empty");
            return map;
        }

        //user exists or not
        User user = userDAO.selectByName(username);

        if (user != null) {
            map.put("msgname", "The username is registered!");
            return map;
        }

        //add codes of password strength here
        // to do
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));//take the first 5 characters of UUID as the salt
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        //login
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    //login
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        //empty or not
        if (StringUtils.isEmpty(username)) {
            map.put("msgname", "Username can not be empty");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msgpwd", "Password can not be empty");
            return map;
        }

        //user exists or not
        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msgname", "The username is not existed");
            return map;
        }

        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "Password is not correct");
            return map;
        }

        map.put("userId", user.getId());
        //give a ticket to the current user
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);//24 hours
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replace("-", ""));//UUID contain "-", so it should be deleted
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    //logout
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
