package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.ManagerAreas;
import com.by.blcu.manager.model.extend.AreaTree;
import com.by.blcu.manager.service.ManagerAreasService;
import com.by.blcu.manager.umodel.AreaModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: ManagerAreasController类
* @author 耿鹤闯
* @date 2019/09/05 11:07
*/
@RestController
@RequestMapping("/managerAreas")
@Api(tags = "Manager省市区", description = "包含接口：\n" +
        "1、省市区-获取省【getProvince】\n" +
        "2、省市区-获取市【getCity】\n" +
        "3、省市区-获取区【getArea】\n"+
        "4、省市区-获取Tree型树数据【getAreaTree】\n")
public class ManagerAreasController {

    @Resource
    private ManagerAreasService managerAreasService;

    @ApiOperation(value = "省市区-获取省", notes = "修改用户信息，比如教师信息等")
    @GetMapping("/getProvince")
    public RetResult<List<AreaModel>> getProvince(@ApiParam(name = "pcode", value = "省份编码") @RequestParam String pcode) throws Exception {
        List<ManagerAreas> data = managerAreasService.getProvince(pcode);
        if(StringHelper.IsNullOrEmpty(data)){
            return RetResponse.makeErrRsp("暂无数据");
        }
        List<AreaModel> resultData =new ArrayList<>();
        data.forEach(t->{
            AreaModel model=new AreaModel();
            model.setCode(t.getCode());
            model.setName(t.getName());
            model.setPingyin(t.getPingyin());
            resultData.add(model);
        });
        return RetResponse.makeOKRsp(resultData);
    }


    @ApiOperation(value = "省市区-获取市", notes = "修改用户信息，比如教师信息等")
    @GetMapping("/getCity")
    public RetResult<List<AreaModel>> getCity(@ApiParam(name = "pcode", value = "省份编码") @RequestParam String pcode,@ApiParam(name = "ccode", value = "市编码") @RequestParam String ccode) throws Exception {
        List<ManagerAreas> data = managerAreasService.getCity(pcode,ccode);
        if(StringHelper.IsNullOrEmpty(data)){
            return RetResponse.makeErrRsp("暂无数据");
        }
        List<AreaModel> resultData =new ArrayList<>();
        data.forEach(t->{
            AreaModel model=new AreaModel();
            model.setCode(t.getCode());
            model.setName(t.getName());
            model.setPingyin(t.getPingyin());
            resultData.add(model);
        });
        return RetResponse.makeOKRsp(resultData);
    }

    @ApiOperation(value = "省市区-获取区", notes = "修改用户信息，比如教师信息等")
    @GetMapping("/getArea")
    public RetResult<List<AreaModel>> getArea(@ApiParam(name = "ccode", value = "市编码") @RequestParam String ccode,@ApiParam(name = "acode", value = "区编码") @RequestParam String acode) throws Exception {
        List<ManagerAreas> data = managerAreasService.getArea(ccode,acode);
        if(StringHelper.IsNullOrEmpty(data)){
            return RetResponse.makeErrRsp("暂无数据");
        }
        List<AreaModel> resultData =new ArrayList<>();
        data.forEach(t->{
            AreaModel model=new AreaModel();
            model.setCode(t.getCode());
            model.setName(t.getName());
            model.setPingyin(t.getPingyin());
            resultData.add(model);
        });
        return RetResponse.makeOKRsp(resultData);
    }

    @ApiOperation(value = "省市区-获取Tree型树数据", notes = "获取全部数据，Tree结构")
    @GetMapping("/getAreaTree")
    public RetResult<List<AreaTree>> getAreaTree(UserSessionHelper helper){
        List<AreaTree> resultData = managerAreasService.selectAreaTree(helper);
        return RetResponse.makeOKRsp(resultData);
    }

}