package com.by.blcu.course.dto;

public class AutomaticCheck {
    /**
	 *VARCHAR
	 *主键dataId
	 */
    private String automaticCheckId;

    /**
	 *VARCHAR
	 *任务id
	 */
    private String taskid;

    /**
	 *INTEGER
	 *审核类型：0：图片；1：音频；2：视频；3文本；4：文档
	 */
    private Integer checkType;

    /**
	 *VARCHAR
	 *回调签名
	 */
    private String seed;

    /**
	 *VARCHAR
	 *涉黄检测值
	 */
    private String porn;

    /**
	 *VARCHAR
	 *涉黄违规详情，多条使用###分割
	 */
    private String pornDetail;

    /**
	 *VARCHAR
	 *涉政检测值
	 */
    private String terrorism;

    /**
	 *VARCHAR
	 *涉政违规详情，多条使用###分割
	 */
    private String terrorismDetail;

    /**
	 *VARCHAR
	 *广告及文字违规检测值
	 */
    private String ad;

    /**
	 *VARCHAR
	 *广告及文字违规详情，多条使用###分割
	 */
    private String adDetail;

    /**
	 *VARCHAR
	 *文本检测场景
	 */
    private String antispam;

    /**
	 *VARCHAR
	 *文本检测结果
	 */
    private String antispamDetail;

    /**
	 *VARCHAR
	 *自动审核的返回值
	 */
    private String resultStr;

    /**
	 *VARCHAR
	 *备用1
	 */
    private String bak1;

    /**
	 *VARCHAR
	 *备用2
	 */
    private String bak2;

    /**
	 *VARCHAR
	 *备用3
	 */
    private String bak3;

    /**
	 *VARCHAR
	 *备用4
	 */
    private String bak4;

    /**
	 *VARCHAR
	 *备用5
	 */
    private String bak5;

    /**
	 *LONGVARCHAR
	 *审核内容
	 */
    private String context;

    public String getAutomaticCheckId() {
        return automaticCheckId;
    }

    public void setAutomaticCheckId(String automaticCheckId) {
        this.automaticCheckId = automaticCheckId == null ? null : automaticCheckId.trim();
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid == null ? null : taskid.trim();
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed == null ? null : seed.trim();
    }

    public String getPorn() {
        return porn;
    }

    public void setPorn(String porn) {
        this.porn = porn == null ? null : porn.trim();
    }

    public String getPornDetail() {
        return pornDetail;
    }

    public void setPornDetail(String pornDetail) {
        this.pornDetail = pornDetail == null ? null : pornDetail.trim();
    }

    public String getTerrorism() {
        return terrorism;
    }

    public void setTerrorism(String terrorism) {
        this.terrorism = terrorism == null ? null : terrorism.trim();
    }

    public String getTerrorismDetail() {
        return terrorismDetail;
    }

    public void setTerrorismDetail(String terrorismDetail) {
        this.terrorismDetail = terrorismDetail == null ? null : terrorismDetail.trim();
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad == null ? null : ad.trim();
    }

    public String getAdDetail() {
        return adDetail;
    }

    public void setAdDetail(String adDetail) {
        this.adDetail = adDetail == null ? null : adDetail.trim();
    }

    public String getAntispam() {
        return antispam;
    }

    public void setAntispam(String antispam) {
        this.antispam = antispam == null ? null : antispam.trim();
    }

    public String getAntispamDetail() {
        return antispamDetail;
    }

    public void setAntispamDetail(String antispamDetail) {
        this.antispamDetail = antispamDetail == null ? null : antispamDetail.trim();
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr == null ? null : resultStr.trim();
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1 == null ? null : bak1.trim();
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2 == null ? null : bak2.trim();
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3 == null ? null : bak3.trim();
    }

    public String getBak4() {
        return bak4;
    }

    public void setBak4(String bak4) {
        this.bak4 = bak4 == null ? null : bak4.trim();
    }

    public String getBak5() {
        return bak5;
    }

    public void setBak5(String bak5) {
        this.bak5 = bak5 == null ? null : bak5.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", automaticCheckId=").append(automaticCheckId);
        sb.append(", taskid=").append(taskid);
        sb.append(", checkType=").append(checkType);
        sb.append(", seed=").append(seed);
        sb.append(", porn=").append(porn);
        sb.append(", pornDetail=").append(pornDetail);
        sb.append(", terrorism=").append(terrorism);
        sb.append(", terrorismDetail=").append(terrorismDetail);
        sb.append(", ad=").append(ad);
        sb.append(", adDetail=").append(adDetail);
        sb.append(", antispam=").append(antispam);
        sb.append(", antispamDetail=").append(antispamDetail);
        sb.append(", resultStr=").append(resultStr);
        sb.append(", bak1=").append(bak1);
        sb.append(", bak2=").append(bak2);
        sb.append(", bak3=").append(bak3);
        sb.append(", bak4=").append(bak4);
        sb.append(", bak5=").append(bak5);
        sb.append(", context=").append(context);
        sb.append("]");
        return sb.toString();
    }
}