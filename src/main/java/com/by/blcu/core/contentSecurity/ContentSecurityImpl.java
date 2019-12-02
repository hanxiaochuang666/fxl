package com.by.blcu.core.contentSecurity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.green.model.v20180509.*;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.AutomaticCheck;
import com.by.blcu.course.dto.Catalog;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.model.AliCheckStatusEnum;
import com.by.blcu.course.service.IAutomaticCheckService;
import com.by.blcu.course.service.ICatalogService;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.service.FileService;
import com.by.blcu.mall.service.RedisService;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.service.ILearnActiveService;
import com.by.blcu.resource.service.IResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service("contentSecurity")
@Slf4j
public class ContentSecurityImpl extends AbstractContentSecurity{

    @Resource(name = "automaticCheckService")
    private IAutomaticCheckService automaticCheckService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Autowired
    private ICatalogService catalogService;

    @Resource
    private FileService fileService;

    @Resource(name = "courseDetailService")
    private ICourseDetailService courseDetailService;

    @Resource
    private RedisService redisService;

    @Resource(name="learnActiveService")
    private ILearnActiveService learnActiveService;

    @Override
    public List<AutomaticCheck> ImageAsyncCheck(List<String> imageLst) throws Exception{
        IAcsClient client = super.getClient();
        ImageAsyncScanRequest imageAsyncScanRequest = new ImageAsyncScanRequest();
        imageAsyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        imageAsyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        imageAsyncScanRequest.setEncoding("utf-8");
        imageAsyncScanRequest.setRegionId(AbstractContentSecurity.regionId);
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        for (String s : imageLst) {
            String uuid = UUID.randomUUID().toString();
            Map<String, Object> task = new LinkedHashMap<String, Object>();
            task.put("dataId",uuid);
            task.put("time", new Date());
            task.put("url", s);
            tasks.add(task);
        }
        JSONObject data = new JSONObject();
        data.put("tasks", tasks);
        data.put("scenes", AbstractContentSecurity.scenesLst);
        log.info("回调地址:"+AbstractContentSecurity.callbackUrl);
        data.put("callback",AbstractContentSecurity.callbackUrl);
        String seed=UUID.randomUUID().toString();
        log.info("请求seed:"+seed);
        data.put("seed", seed);
        imageAsyncScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        //设置超时时间
        imageAsyncScanRequest.setConnectTimeout(Integer.valueOf(AbstractContentSecurity.connectTimeout).intValue());
        imageAsyncScanRequest.setReadTimeout(Integer.valueOf(AbstractContentSecurity.readTimeout).intValue());
        HttpResponse httpResponse = client.doAction(imageAsyncScanRequest);

        if(httpResponse.isSuccess()){
            JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
            log.info(JSON.toJSONString(scrResponse, true));
            return responseOpt(scrResponse,seed,0);
        }else{
            log.info("response not success. status:" + httpResponse.getStatus());
            return null;
        }
    }

    @Override
    public List<AutomaticCheck> VideoAsyncCheck(List<String> videoLst)throws Exception {
        IAcsClient client = super.getClient();
        VideoAsyncScanRequest videoAsyncScanRequest = new VideoAsyncScanRequest();
        videoAsyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        videoAsyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST);
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        for (String s : videoLst) {
            Map<String, Object> task = new LinkedHashMap<String, Object>();
            String uuid = UUID.randomUUID().toString();
            task.put("dataId",uuid);
            task.put("interval", AbstractContentSecurity.interval);
            task.put("length", 3600);
            task.put("url", s);
            tasks.add(task);
        }
        JSONObject data = new JSONObject();
        data.put("tasks", tasks);
        data.put("scenes", AbstractContentSecurity.scenesLst);
        log.info("回调地址:"+AbstractContentSecurity.callbackUrl);
        data.put("callback",AbstractContentSecurity.callbackUrl);
        String seed=UUID.randomUUID().toString();
        log.info("请求seed:"+seed);
        data.put("seed", seed);
        videoAsyncScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        videoAsyncScanRequest.setConnectTimeout(Integer.valueOf(AbstractContentSecurity.connectTimeout).intValue());
        videoAsyncScanRequest.setReadTimeout(Integer.valueOf(AbstractContentSecurity.readTimeout).intValue());
        HttpResponse httpResponse = client.doAction(videoAsyncScanRequest);
        if(httpResponse.isSuccess()){
            JSONObject jo = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
            log.info(JSON.toJSONString(jo, true));
            return responseOpt(jo,seed,2);
        }else{
            log.info("response not success. status:" + httpResponse.getStatus());
            return null;
        }
    }

    private List<AutomaticCheck> responseOpt(JSONObject jo,String seed,int checkType){
        List<AutomaticCheck> automaticCheckList=new ArrayList<>();
        if (200 == jo.getInteger("code")) {
            JSONArray taskResults = jo.getJSONArray("data");
            for (Object taskResult : taskResults) {
                if(200 == ((JSONObject)taskResult).getInteger("code")){
                    String taskId = ((JSONObject)taskResult).getString("taskId");
                    //插入审核创建信息
                    AutomaticCheck automaticCheck = new AutomaticCheck();
                    String dataId = ((JSONObject)taskResult).getString("dataId");
                    String url = ((JSONObject)taskResult).getString("url");
                    log.info("taskId = [" + taskId + "]");
                    log.info("dataId = [" + dataId + "]");
                    log.info("url = [" + url + "]");
                    automaticCheck.setAutomaticCheckId(dataId);
                    automaticCheck.setTaskid(taskId);
                    automaticCheck.setContext(url);
                    automaticCheck.setCheckType(checkType);
                    automaticCheck.setSeed(seed);
                    automaticCheckList.add(automaticCheck);
                }else{
                    log.info("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                }
            }
        }
        return automaticCheckList;
    }

    @Override
    public List<AutomaticCheck>  FileAsyncCheck(List<String> fileLst) throws Exception {
        IAcsClient client = super.getClient();
        FileAsyncScanRequest asyncScanRequest = new FileAsyncScanRequest();
        asyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        asyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        asyncScanRequest.setRegionId(AbstractContentSecurity.regionId);
        asyncScanRequest.setConnectTimeout(Integer.valueOf(AbstractContentSecurity.connectTimeout));
        asyncScanRequest.setReadTimeout(Integer.valueOf(AbstractContentSecurity.readTimeout));
        List<String> tempList = new ArrayList<>();
        List<AutomaticCheck> automaticCheckList=new ArrayList<>();
        int i=0;
        HashMap<String, String> hashMap = new HashMap<>();
        while (fileLst.size() > 0) {
            if (fileLst.size() / 5 > 0) {
                tempList.addAll(fileLst.subList(i*5,5));
                fileLst = fileLst.subList(i * 5 + 5, fileLst.size());
            }else {
                tempList.addAll(fileLst);
                fileLst.clear();
            }
            List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
            for (String s : tempList) {
                Map<String, Object> task = new LinkedHashMap<String, Object>();
                String uuid = UUID.randomUUID().toString();
                task.put("dataId",uuid);
                task.put("url", s);
                hashMap.put(uuid,s);
                tasks.add(task);
            }
            JSONObject data = new JSONObject();
            /**
             * antispam: 文本反垃圾
             */
            data.put("textScenes", Arrays.asList("antispam"));

            /**
             * 图片做图片对应场景的检测
             */
            data.put("imageScenes", AbstractContentSecurity.scenesLst);
            data.put("tasks", tasks);
            log.info("回调地址:"+AbstractContentSecurity.callbackUrl);
            data.put("callback", AbstractContentSecurity.callbackUrl);
            String seed = UUID.randomUUID().toString();
            log.info("请求seed:"+seed);
            data.put("seed", seed);
            asyncScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
            HttpResponse httpResponse = client.doAction(asyncScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                log.info(JSON.toJSONString(scrResponse, true));
                automaticCheckList.addAll(responseOpt(scrResponse, seed, 4));
            } else {
                log.info("response not success. status:" + httpResponse.getStatus());
            }
            i++;
        }
        for (AutomaticCheck automaticCheck : automaticCheckList) {
            if(hashMap.containsKey(automaticCheck.getAutomaticCheckId())){
                automaticCheck.setContext(hashMap.get(automaticCheck.getAutomaticCheckId()));
            }
        }
        return automaticCheckList;
    }

    /**
     * 音频检测每次传的音频数最大为100个元素，我们现在每次传送5个，间隔2秒
     * @param voiceLst
     * @return
     * @throws Exception
     */
    @Override
    public List<AutomaticCheck> VoiceAsyncCheck(List<String> voiceLst) throws Exception{
        IAcsClient client = super.getClient();
        VoiceAsyncScanRequest asyncScanRequest = new VoiceAsyncScanRequest();
        asyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        asyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        asyncScanRequest.setRegionId(AbstractContentSecurity.regionId);
        asyncScanRequest.setConnectTimeout(Integer.valueOf(AbstractContentSecurity.connectTimeout));
        asyncScanRequest.setReadTimeout(Integer.valueOf(AbstractContentSecurity.readTimeout));
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        for (String s : voiceLst) {
            Map<String, Object> task1 = new LinkedHashMap<String, Object>();
            String uuid = UUID.randomUUID().toString();
            task1.put("dataId",uuid);
            task1.put("url", s);
            tasks.add(task1);
        }
        JSONObject data = new JSONObject();
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        log.info("回调地址:"+AbstractContentSecurity.callbackUrl);
        data.put("callback",AbstractContentSecurity.callbackUrl);
        String seed=UUID.randomUUID().toString();
        log.info("请求seed:"+seed);
        data.put("seed", seed);
        asyncScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        HttpResponse httpResponse = client.doAction(asyncScanRequest);
        if (httpResponse.isSuccess()) {
            JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
            log.info(JSON.toJSONString(scrResponse, true));
            return responseOpt(scrResponse,seed,1);
        }else {
            log.info("response not success. status:" + httpResponse.getStatus());
            return null;
        }
    }

    /**
     * 审核试卷时，在业务层面做好字符长度最大为10000的限制，可以使用bak1作为下一个截取的审核id存储
     * @param textMap
     * @return
     * @throws Exception
     */
    @Override
    public List<AutomaticCheck>  TextCheck(Map<Integer,String> textMap)throws Exception {
        IAcsClient client = super.getClient();
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(AbstractContentSecurity.regionId);
        textScanRequest.setConnectTimeout(Integer.valueOf(AbstractContentSecurity.connectTimeout));
        textScanRequest.setReadTimeout(Integer.valueOf(AbstractContentSecurity.readTimeout));
        List<Map<String, Object>> tasks = new ArrayList<>();
        Map<String,String> parentAndChild=new HashMap<>();
        Map<String,Integer> cacheResourceIdMap=new HashMap<>();
        for(Map.Entry<Integer,String> entry:textMap.entrySet()){
            Integer mapKey = entry.getKey();
            String mapValue = entry.getValue();
            List<Map<String, Object>> resMap =new ArrayList<>();
            String uuid = UUID.randomUUID().toString();
            cacheResourceIdMap.put(uuid,mapKey);
            getValidTextTask(resMap,mapValue,parentAndChild,uuid);
            tasks.addAll(resMap);
        }
        List<AutomaticCheck> automaticCheckList=new ArrayList<>();
        for(int i=0;i<tasks.size();i+=100) {
            List<Map<String, Object>> temp=null;
            if(i+100<tasks.size())
                temp=tasks.subList(i,i+100);
            else
                temp=tasks.subList(i,tasks.size());
            JSONObject data = new JSONObject();
            data.put("scenes", Arrays.asList("antispam"));
            data.put("tasks", temp);
            for (Map<String, Object> stringObjectMap : temp) {
                log.info("dataId:"+stringObjectMap.get("dataId").toString());
                log.info("content:"+stringObjectMap.get("content").toString());
            }
            textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                log.info("文本检测返回:"+JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            log.info(((JSONObject) taskResult).toJSONString());
                            String dataId = ((JSONObject) taskResult).getString("dataId");
                            AutomaticCheck AutomaticCheck = new AutomaticCheck();
                            AutomaticCheck.setAutomaticCheckId(dataId);
                            if (parentAndChild.containsKey(dataId)) {
                                String s = parentAndChild.get(dataId);
                                AutomaticCheck.setBak1(s);
                            }
                            if (cacheResourceIdMap.containsKey(dataId)) {
                                Integer resourceId = cacheResourceIdMap.get(dataId);
                                AutomaticCheck.setBak2(resourceId.toString());
                            }
                            String content = ((JSONObject) taskResult).getString("content");
                            AutomaticCheck.setContext(content);
                            String taskId = ((JSONObject) taskResult).getString("taskId");
                            AutomaticCheck.setTaskid(taskId);
                            AutomaticCheck.setCheckType(3);
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            AutomaticCheck.setResultStr(((JSONObject) taskResult).toJSONString());
                            for (Object sceneResult : sceneResults) {
                                String label = ((JSONObject) sceneResult).getString("label");
                                AutomaticCheck.setAntispam(label);
                                String scene = ((JSONObject) sceneResult).getString("scene");
                                String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                                JSONArray details = ((JSONObject) taskResult).getJSONArray("details");
                                if (!StringUtils.isEmpty(details)) {
                                    String s = details.toJSONString();
                                    AutomaticCheck.setAntispamDetail(s);
                                }
                            }
                            automaticCheckList.add(AutomaticCheck);
                        } else {
                            log.info("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
                        }
                    }
                } else {
                    log.info("detect not success. code:" + scrResponse.getInteger("code"));
                }
            } else {
                log.info("response not success. status:" + httpResponse.getStatus());
            }
        }
        return automaticCheckList;
    }

    private void getValidTextTask(List<Map<String, Object>> resMap,String str,Map<String,String> map,String uuid){
        int length = StringUtils.getLength(str);
        if(length<=10000){
            Map<String, Object> task1 = new LinkedHashMap<String, Object>();
            task1.put("dataId", uuid);
            task1.put("content", str);
            resMap.add(task1);
            return;
        }
        String subString = StringUtils.getSubString(str, 10000);
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", uuid);
        task1.put("content", subString);
        resMap.add(task1);
        String s1 = UUID.randomUUID().toString();
        map.put(uuid,s1);
        String s = str.substring(subString.length());
        getValidTextTask(resMap,s,map,s1);
    }


    private static JSONObject getScanResult(IAcsClient client, String taskId) {
        VoiceAsyncScanResultsRequest getResultsRequest = new VoiceAsyncScanResultsRequest();
        getResultsRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        getResultsRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        getResultsRequest.setEncoding("utf-8");
        getResultsRequest.setRegionId(regionId);


        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("taskId", taskId);
        tasks.add(task1);

        /**
         * 请务必设置超时时间
         */
        getResultsRequest.setConnectTimeout(3000);
        getResultsRequest.setReadTimeout(6000);

        try {
            getResultsRequest.setHttpContent(JSON.toJSONString(tasks).getBytes("UTF-8"), "UTF-8", FormatType.JSON);

            HttpResponse httpResponse = client.doAction(getResultsRequest);
            if (httpResponse.isSuccess()) {
                return JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
            } else {
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean checkCallBack(String checksum, String content) {
        log.info(checksum);
        log.info("回传内容"+content);
        if(StringUtils.isEmpty(checksum)){
            log.info("返回没有checksum");
            return false;
        }
        JSONObject jsonObject = JSON.parseObject(content);
        if(!jsonObject.containsKey("dataId")){
            log.info("返回没有dataId");
            return false;
        }
        if(!jsonObject.containsKey("taskId")){
            log.info("taskId");
            return false;
        }
        if(!jsonObject.containsKey("code")
                || !jsonObject.getString("code").equals("200")){
            log.info("返回码无效");
            return false;
        }
        String dataId = jsonObject.getString("dataId");
        if(redisService.hasKey("automatic_check"+dataId)){
            log.info("异步检测id为"+dataId+"正在处理结果");
            return false;
        }else {
            redisService.setWithExpire("automatic_check"+dataId, dataId, 300);
        }
        AutomaticCheck automaticCheck = automaticCheckService.selectByPrimaryKey(jsonObject.getString("dataId"));
        if(null==automaticCheck
                ||  StringUtils.isEmpty(automaticCheck.getSeed())){
            log.info("无效的dataId");
            redisService.delete("automatic_check"+dataId);
            return false;
        }else if(null!=automaticCheck.getPorn() && null!=automaticCheck.getAd() && null!=automaticCheck.getTerrorism()){
            log.info("dataId 为"+jsonObject.getString("dataId")+"已经处理");
            redisService.delete("automatic_check"+dataId);
            return true;
        }
        String checkSumRes = null;
        checkSumRes = getCheckSum(automaticCheck.getSeed(), content,automaticCheck.getContext());
        if(!checkSumRes.equals(checksum)){
            log.info("验证码错误");
            log.info("接收到的checksum:"+checksum);
            log.info("计算的checksum:"+checkSumRes);
            redisService.delete("automatic_check"+dataId);
            return false;
        }
        if(!jsonObject.containsKey("results")
            && !jsonObject.containsKey("imageResults")
                && !jsonObject.containsKey("results")){
            log.info("没有返回结果results");
            redisService.delete("automatic_check"+dataId);
            return false;
        }
        automaticCheck.setResultStr(content);
        JSONArray results = jsonObject.getJSONArray("results");
        if(null==results){
            results=new JSONArray();
            JSONArray imageResults = jsonObject.getJSONArray("imageResults");
            if(null!=imageResults&&imageResults.size()>0){
                for (Object imageResult : imageResults) {
                    JSONObject results1 = (JSONObject)imageResult;
                    JSONArray results2 = results1.getJSONArray("results");
                    if(null!=results2){
                        results.addAll(results2);
                    }else {
                        JSONArray objects = new JSONArray();
                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1.put("scene","porn");
                        jsonObject1.put("label","normal");
                        objects.add(jsonObject1);
                        JSONObject jsonObject2=new JSONObject();
                        jsonObject2.put("scene","terrorism");
                        jsonObject2.put("label","normal");
                        objects.add(jsonObject2);
                        JSONObject jsonObject3=new JSONObject();
                        jsonObject3.put("scene","ad");
                        jsonObject3.put("label","normal");
                        objects.add(jsonObject3);
                        results.addAll(objects);

                    }

                }

            }
            JSONArray textResults = jsonObject.getJSONArray("textResults");
            if(null!=textResults&&textResults.size()>0){
                for (Object textResult : textResults) {
                    JSONObject results1 = (JSONObject)textResult;
                    JSONArray results2 = results1.getJSONArray("results");
                    if(null!=results2){
                        results.addAll(results2);
                    }else {
                        JSONArray objects = new JSONArray();
                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1.put("scene","antispam");
                        jsonObject1.put("label","normal");
                        objects.add(jsonObject1);
                        results.addAll(objects);
                    }

                }

            }
        }
        for (Object result : results) {
            JSONObject obj=(JSONObject)result;
            if(obj.containsKey("scene")){
                String scene = obj.getString("scene");
                String label = obj.getString("label");
                if(scene.equals("porn")){
                    automaticCheck.setPorn(label);
                    automaticCheck.setPornDetail(AliCheckStatusEnum.getDescByStatus(label));
                }else if(scene.equals("terrorism")){
                    automaticCheck.setTerrorism(label);
                    automaticCheck.setTerrorismDetail(AliCheckStatusEnum.getDescByStatus(label));
                }else if(scene.equals("ad")){
                    automaticCheck.setAd(label);
                    automaticCheck.setAdDetail(AliCheckStatusEnum.getDescByStatus(label));
                }else if(scene.equals("antispam")){
                    automaticCheck.setAntispam(label);
                    automaticCheck.setAntispamDetail(AliCheckStatusEnum.getDescByStatus(label));
                }
            }
        }
        syncCheckStatus(automaticCheck);
        rollBackData(automaticCheck);
        redisService.delete("automatic_check"+dataId);
        return true;
    }

    /**
     * 回滚数据
     * @param automaticCheck
     */
    private void rollBackData(AutomaticCheck automaticCheck){
        int checkStatusByAuto = getCheckStatusByAuto(automaticCheck);
        String bak5 = automaticCheck.getBak5();
        /**
         * 后台最好针对这种需要回滚的异步审核，做一个定时任务的回滚
         */
        if(1==checkStatusByAuto && !StringUtils.isEmpty(bak5)){
            //回滚数据
            if("course_detail".equals(bak5)){
                String bak4 = automaticCheck.getBak4();
                String bak3 = automaticCheck.getBak3();
                String bak1 = automaticCheck.getBak1();
                if(!StringUtils.isEmpty(bak3)){
                    CourseDetail courseDetail = JSON.parseObject(bak3, CourseDetail.class);
                    Map<String, Object> courseMap = MapUtils.initMap("courseId", courseDetail.getCourseId());
                    courseMap.put("catalogId",courseDetail.getCourseDetailId());
                    courseMap.put("resourcesId",courseDetail.getResourcesId());
                    List<CourseDetail> objects = courseDetailService.selectList(courseMap);
                    if(null!=objects && objects.size()>0){
                        CourseDetail courseDetail1 = objects.get(0);
                        Integer courseDetailId = courseDetail1.getCourseDetailId();
                        courseDetailService.deleteByPrimaryKey(courseDetailId);
                        //删除学生学习行为
                        if(StringUtils.isEmpty(bak4)){
                            Map<String, Object> courseMaps = MapUtils.initMap("courseId", courseDetail1.getCourseId());
                            courseMaps.put("courseDetailId",courseDetail1.getCourseDetailId());
                            learnActiveService.deleteByParams(courseMaps);
                        }
                        //resourcesService.deleteByPrimaryKey(courseDetail.getResourcesId());
                    }
                }
                if(!StringUtils.isEmpty(bak4)){
                    CourseDetail courseDetail = JSON.parseObject(bak4, CourseDetail.class);
                    courseDetail.setCourseDetailId(null);
                    courseDetailService.insertSelective(courseDetail);
                }
            }else if("file".equals(bak5)){
                String bak4 = automaticCheck.getBak4();
                String bak3 = automaticCheck.getBak3();
                if(!StringUtils.isEmpty(bak3)){
                    CourseDetail courseDetail = JSON.parseObject(bak3, CourseDetail.class);
                    String fileId = courseDetail.getBak1();
                    List<Resources> content = resourcesService.selectList(MapUtils.initMap("content", fileId));
                    if(null!=content &&content.size()>0){
                        Resources resources = content.get(0);
                        Map<String, Object> objectMap = MapUtils.initMap("courseId", courseDetail.getCourseId());
                        objectMap.put("catalogId",courseDetail.getCatalogId());
                        objectMap.put("resourcesId",resources.getResourcesId());
                        List<CourseDetail> objects = courseDetailService.selectList(objectMap);
                        if(null!=objects && objects.size()>0) {
                            CourseDetail courseDetail1 = objects.get(0);
                            Integer courseDetailId = courseDetail1.getCourseDetailId();
                            courseDetailService.deleteByPrimaryKey(courseDetailId);
                            //删除学生学习行为
                            if (StringUtils.isEmpty(bak4)) {
                                Map<String, Object> courseMaps = MapUtils.initMap("courseId", courseDetail1.getCourseId());
                                courseMaps.put("courseDetailId", courseDetail1.getCourseDetailId());
                                learnActiveService.deleteByParams(courseMaps);
                            }
                            //resourcesService.deleteByPrimaryKey(courseDetail.getResourcesId());
                        }
                    }
                }
                if(!StringUtils.isEmpty(bak4)){
                    CourseDetail courseDetail = JSON.parseObject(bak4, CourseDetail.class);
                    courseDetail.setCourseDetailId(null);
                    courseDetailService.insertSelective(courseDetail);
                }
            }

            AutomaticCheck automaticCheck1 = new AutomaticCheck();
            automaticCheck1.setAutomaticCheckId(automaticCheck.getAutomaticCheckId());
            automaticCheck1.setBak5("");
            automaticCheckService.updateByPrimaryKeySelective(automaticCheck1);
        }
        //审核通过，则要删除需要回滚的数据
        /*if(2==checkStatusByAuto && !StringUtils.isEmpty(bak5)) {
            String bak4 = automaticCheck.getBak4();
            if(!StringUtils.isEmpty(bak4)){
                CourseDetail courseDetail = JSON.parseObject(bak4, CourseDetail.class);
                try {
                    resourcesService.removeByResourceType(courseDetail.getResourcesId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/

    }
    private void syncCheckStatus(AutomaticCheck automaticCheck){
        Date date = new Date();
        String automaticCheckId = automaticCheck.getAutomaticCheckId();
        Map<String, Object> checkMap = MapUtils.initMap("checkId", automaticCheckId);
        List<Resources> resourcesLst = resourcesService.selectList(checkMap);
        if(null!=resourcesLst && resourcesLst.size()>0){
            Resources resources = resourcesLst.get(0);
            int checkStatusByAuto = getCheckStatusByAuto(automaticCheck);
            resources.setCheckStatus(checkStatusByAuto);
            resources.setCheckTime(date);
            resourcesService.updateByPrimaryKeySelective(resources);
            return;
        }
        List<Catalog> catalogLst = catalogService.selectList(checkMap);
        if(null!=catalogLst && catalogLst.size()>0){
            Catalog catalog = catalogLst.get(0);
            catalog.setCheckStatus(getCheckStatusByAuto(automaticCheck));
            catalog.setCheckTime(date);
            catalogService.updateByPrimaryKeySelective(catalog);
            return;
        }
        Condition condition=new Condition(File.class);
        condition.createCriteria().andEqualTo("checkId",automaticCheckId);
        List<File> files = fileService.selectByCondition(condition);
        if(null!=files && files.size()>0){
            File file = files.get(0);
            fileService.update(file);
            return;
        }
        automaticCheckService.updateByPrimaryKeySelective(automaticCheck);
    }
    private int getCheckStatusByAuto(AutomaticCheck automaticCheck){
        if((!StringUtils.isEmpty(automaticCheck.getPorn()) && !automaticCheck.getPorn().equals(AliCheckStatusEnum.NORMAL.getStatus()))
                || (!StringUtils.isEmpty(automaticCheck.getAd()) && !automaticCheck.getAd().equals(AliCheckStatusEnum.NORMAL.getStatus()))
                || (!StringUtils.isEmpty(automaticCheck.getAntispam()) && !automaticCheck.getAntispam().equals(AliCheckStatusEnum.NORMAL.getStatus()))
                || (!StringUtils.isEmpty(automaticCheck.getTerrorism()) && !automaticCheck.getTerrorism().equals(AliCheckStatusEnum.NORMAL.getStatus())))
            return 1;
        else
            return 2;
    }
    private String getCheckSum(String seed, String content,String newUrl){
        JSONObject jsonObject = JSON.parseObject(content);
        Map<String,String> tihuanMap=new HashMap<>();
        jiexi(jsonObject,tihuanMap);
        for (Map.Entry<String, String> stringStringEntry : tihuanMap.entrySet()) {
            content=content.replace(stringStringEntry.getKey(),stringStringEntry.getValue());
        }
        log.info("待加密字符串:"+AbstractContentSecurity.uid+seed+content);
        return ApplicationUtils.sha256Hex(AbstractContentSecurity.uid+seed+content);
    }
    private void jiexi(JSONObject jsonObject,Map<String,String> tihuanMap){
        for (Map.Entry<String, Object> entry :jsonObject.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue().toString();
            if("url".equals(key)){//&amp;
                tihuanMap.put(value,value.replaceAll("&amp;","&"));
            }
            if(JSON.isValidObject(value)) {
                JSONObject jsonObject1 = JSON.parseObject(value);
                int size = jsonObject1.size();
                if (size > 0)
                    jiexi(jsonObject1, tihuanMap);
            }else if(JSON.isValidArray(value)){
                JSONArray objects = JSON.parseArray(value);
                for (Object object : objects) {
                    jiexi((JSONObject)object,tihuanMap);
                }
            }
        }
    }
    /**
     * 异步检测超时未返回的处理
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void syncCheckOutTimeExec(){
        List<AutomaticCheck> automaticCheckLst=new ArrayList<>();
        List<AutomaticCheck> objects = automaticCheckService.selectList(MapUtils.initMap("bak5", "course_detail"));
        if(null!=objects && objects.size()>0)
            automaticCheckLst.addAll(objects);
        List<AutomaticCheck> objects1 = automaticCheckService.selectList(MapUtils.initMap("bak5", "file"));
        if(null!=objects1 && objects1.size()>0)
            automaticCheckLst.addAll(objects1);
        for (AutomaticCheck automaticCheck : automaticCheckLst) {
            //回滚数据
            if("course_detail".equals(automaticCheck.getBak5())){
                String bak4 = automaticCheck.getBak4();
                String bak3 = automaticCheck.getBak3();
                if(!StringUtils.isEmpty(bak3)){
                    CourseDetail courseDetail = JSON.parseObject(bak3, CourseDetail.class);
                    Map<String, Object> courseMap = MapUtils.initMap("courseId", courseDetail.getCourseId());
                    courseMap.put("catalogId",courseDetail.getCourseDetailId());
                    courseMap.put("resourcesId",courseDetail.getResourcesId());
                    List<CourseDetail> objects3 = courseDetailService.selectList(courseMap);
                    if(null!=objects3 && objects3.size()>0){
                        Integer courseDetailId = objects3.get(0).getCourseDetailId();
                        courseDetailService.deleteByPrimaryKey(courseDetailId);
                    }
                }
                if(!StringUtils.isEmpty(bak4)){
                    CourseDetail courseDetail = JSON.parseObject(bak4, CourseDetail.class);
                    courseDetail.setCourseDetailId(null);
                    courseDetailService.insertSelective(courseDetail);
                }
            }else if("file".equals(automaticCheck.getBak5())){
                String bak4 = automaticCheck.getBak4();
                String bak3 = automaticCheck.getBak3();
                if(!StringUtils.isEmpty(bak4)){
                    CourseDetail courseDetail = JSON.parseObject(bak4, CourseDetail.class);
                    courseDetail.setCourseDetailId(null);
                    courseDetailService.insertSelective(courseDetail);
                }
                if(!StringUtils.isEmpty(bak3)){
                    CourseDetail courseDetail = JSON.parseObject(bak3, CourseDetail.class);
                    String fileId = courseDetail.getBak1();
                    List<Resources> content = resourcesService.selectList(MapUtils.initMap("content", fileId));
                    if(null!=content &&content.size()>0){
                        Resources resources = content.get(0);
                        Map<String, Object> objectMap = MapUtils.initMap("courseId", courseDetail.getCourseId());
                        objectMap.put("catalogId",courseDetail.getCatalogId());
                        objectMap.put("resourcesId",resources.getResourcesId());
                        courseDetailService.deleteByParams(objectMap);
                        resourcesService.deleteByPrimaryKey(resources.getResourcesId());
                    }
                }
            }

            AutomaticCheck automaticCheck1 = new AutomaticCheck();
            automaticCheck1.setAutomaticCheckId(automaticCheck.getAutomaticCheckId());
            automaticCheck1.setBak5("");
            automaticCheckService.updateByPrimaryKeySelective(automaticCheck1);
        }
    }
}
