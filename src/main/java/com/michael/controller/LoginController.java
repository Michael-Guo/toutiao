package com.michael.controller;

import com.michael.async.EventModel;
import com.michael.async.EventProducer;
import com.michael.async.EventType;
import com.michael.service.UserService;
import com.michael.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Created by GWC on 2016/7/6.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    //register
    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int remember,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");//let cookie be valid in all of the website
                if (remember > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "Register Success");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("Register Exception" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "Register Exception");
        }
    }

    //login
    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int remember,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");//let cookie be valid in all of the website
                if (remember > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
                        .setExt("username", username).setExt("email", "kjgwc@126.com"));
                return ToutiaoUtil.getJSONString(0, "Login Success");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("Login Exception" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "Login Exception");
        }
    }

    //logout
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/"; //redirect to home page after log out
    }
}
