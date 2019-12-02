package com.by.blcu.manager.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.dao.ManagerLogMapper;
import com.by.blcu.manager.model.ManagerLog;
import com.by.blcu.manager.model.sql.InputLog;
import com.by.blcu.manager.service.ManagerLogService;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* @Description: ManagerLogService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerLogServiceImpl extends AbstractService<ManagerLog> implements ManagerLogService {

    @Resource
    private ManagerLogMapper managerLogMapper;

    public RetResult<Integer> addLog(ManagerLog log, UserSessionHelper helper){
        if(log==null || StringHelper.IsNullOrWhiteSpace(log.getOptType())){
            return RetResponse.makeErrRsp("[操作类型]不能为空");
        }

        log.setLogId(ApplicationUtils.getUUID());
        Date datetime =new Date();
        log.setOptTime(datetime);
        log.setOptName(helper.getUserName());
        log.setCreateTime(datetime);
        log.setCreateBy(helper.getUserName());
        log.setModifyTime(datetime);
        log.setModifyBy(helper.getUserName());
        log.setOptIP(helper.getIp());
        Integer state = managerLogMapper.insert(log);

        return RetResponse.makeOKRsp(state);
    }

    @Async
    public Integer addLogAsync(String optType,String optDescription, UserSessionHelper helper){
        if(StringHelper.IsNullOrWhiteSpace(optType)){
            return 0;
        }
        ManagerLog log=new ManagerLog();
        log.setOptType(optType);
        log.setOptDescription(optDescription);

        log.setLogId(ApplicationUtils.getUUID());
        Date datetime =new Date();
        log.setOptTime(datetime);
        log.setOptName(helper.getUserName());
        log.setCreateTime(datetime);
        log.setCreateBy(helper.getUserName());
        log.setModifyTime(datetime);
        log.setModifyBy(helper.getUserName());
        log.setOptIP(helper.getIp());
        Integer state = managerLogMapper.insert(log);

        return state;
    }

    public List<ManagerLog> selectLogList(InputLog search){
        if(search==null){
            return null;
        }
        return managerLogMapper.selectLogList(search);
    }

    public Integer selectLogListCount(InputLog search){
        if(search==null){
            return 0;
        }
        return managerLogMapper.selectLogListCount(search);
    }

}