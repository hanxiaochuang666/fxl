package com.by.blcu.mall.api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;


@Controller
@ApiIgnore
public class HelloController {

    public final static Logger logger = LoggerFactory.getLogger(HelloController.class);
    @RequestMapping("/test")
    public String test(){
        return "login";
    }
   
}