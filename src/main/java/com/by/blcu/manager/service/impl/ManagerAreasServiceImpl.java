package com.by.blcu.manager.service.impl;

import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.manager.common.RedisHelper;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.dao.ManagerAreasMapper;
import com.by.blcu.manager.model.ManagerAreas;
import com.by.blcu.manager.model.sql.InputAreas;
import com.by.blcu.manager.service.ManagerAreasService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @Description: ManagerAreasService接口实现类
* @author 耿鹤闯
* @date 2019/09/05 11:07
*/
@Service
public class ManagerAreasServiceImpl extends AbstractService<ManagerAreas> implements ManagerAreasService {

    @Resource
    private ManagerAreasMapper managerAreasMapper;

    @Resource
    private RedisHelper redisHelper;

    public List<ManagerAreas> selectList(){
        //取缓存
        List<ManagerAreas> list = redisHelper.getAreaList("");
        if(!StringHelper.IsNullOrEmpty(list)){
            return list;
        }
        list=managerAreasMapper.selectAll();
        redisHelper.setAreaList("",list);
        return list;
    }

    //获取省
    public List<ManagerAreas> getProvince(String pcode){
        List<ManagerAreas> list = selectList();
        if(StringHelper.IsNullOrEmpty(list)){
            return null;
        }
        Stream<ManagerAreas> sql = list.stream().filter(t->t.getLevel()==1);
        if(!StringHelper.IsNullOrWhiteSpace(pcode)){
            sql = sql.filter(t->t.getCode().startsWith(pcode));
        }
        return  sql.collect(Collectors.toList());
    }
    //获取市
    public List<ManagerAreas> getCity(String pcode, String ccode){
        List<ManagerAreas> list = selectList();
        if(StringHelper.IsNullOrEmpty(list)){
            return null;
        }
        Stream<ManagerAreas> sql = list.stream().filter(t->t.getLevel()==2);
        if(!StringHelper.IsNullOrWhiteSpace(pcode)){
            if(pcode.length()>2){
                pcode=pcode.substring(0,2);
            }
            String pcodeInput = pcode;
            sql = sql.filter(t->t.getCode().startsWith(pcodeInput));
        }
        if(!StringHelper.IsNullOrWhiteSpace(ccode)){
            sql = sql.filter(t->t.getCode().startsWith(ccode));
        }
        return  sql.collect(Collectors.toList());
    }
    //获取区
    public List<ManagerAreas> getArea(String ccode, String acode){
        List<ManagerAreas> list = selectList();
        if(StringHelper.IsNullOrEmpty(list)){
            return null;
        }
        Stream<ManagerAreas> sql = list.stream().filter(t->t.getLevel()==3);
        if(!StringHelper.IsNullOrWhiteSpace(ccode)){
            if(ccode.length()>2){
                ccode=ccode.substring(0,4);
            }
            String ccodeInput = ccode;
            sql = sql.filter(t->t.getCode().startsWith(ccodeInput));
        }
        if(!StringHelper.IsNullOrWhiteSpace(acode)){
            sql = sql.filter(t->t.getCode().startsWith(acode));
        }
        return  sql.collect(Collectors.toList());
    }

}