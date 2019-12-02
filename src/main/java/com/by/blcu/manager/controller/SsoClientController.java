package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.SsoClient;
import com.by.blcu.manager.service.SsoClientService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: SsoClientController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@RequestMapping("/ssoClient")
@ApiIgnore
public class SsoClientController {

    @Resource
    private SsoClientService ssoClientService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(SsoClient ssoClient) throws Exception{
      ssoClient.setId(ApplicationUtils.getUUID());
       Integer state = ssoClientService.insert(ssoClient);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = ssoClientService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(SsoClient ssoClient) throws Exception {
        Integer state = ssoClientService.update(ssoClient);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<SsoClient> selectById(@RequestParam String id) throws Exception {
        SsoClient ssoClient = ssoClientService.selectById(id);
        return RetResponse.makeOKRsp(ssoClient);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<SsoClient>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<SsoClient>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<SsoClient> list = ssoClientService.selectAll();
        PageInfo<SsoClient> pageInfo = new PageInfo<SsoClient>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}