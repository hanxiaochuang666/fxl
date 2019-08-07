package com.by.blcu.mall.controller;

import com.by.blcu.core.constant.ExcelConstant;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ExcelUtils;
import com.by.blcu.mall.model.ExcelData;
import com.by.blcu.mall.model.UserInfo;
import com.by.blcu.mall.service.UserInfoService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("excel")
@ApiIgnore
public class ExcelController {

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/test")
    public  RetResult<Integer> test(){
        int rowIndex = 0;
        List<UserInfo> list = userInfoService.selectAll();
        ExcelData data = new ExcelData();
        data.setName("hello");
        List<String> titles = new ArrayList<String>();
        titles.add("ID");
        titles.add("userName");
        //titles.add("password");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList<List<Object>>();
        for(int i = 0, length = list.size();i<length;i++){
            UserInfo userInfo = list.get(i);
            List<Object> row = new ArrayList<Object>();
            row.add(userInfo.getId());
            row.add(userInfo.getUserName());
            //row.add(userInfo.getPassword());
            rows.add(row);
        }
        data.setRows(rows);
        try{
            rowIndex = ExcelUtils.generateExcel(data, ExcelConstant.FILE_PATH + ExcelConstant.FILE_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }
        return RetResponse.makeOKRsp(Integer.valueOf(rowIndex));
    }

   
}
