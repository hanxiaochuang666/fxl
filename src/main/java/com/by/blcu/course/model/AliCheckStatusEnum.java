package com.by.blcu.course.model;

public enum AliCheckStatusEnum {

    NORMAL("normal","正常"),
    SPAM("spam","含垃圾信息"),
    AD("ad","广告"),
    POLITICS("politics","涉政"),
    BLOODY("bloody","血腥"),
    EXPLOSION("explosion","爆炸烟光"),
    OUTFIT("outfit","特殊装束"),
    LOGO("logo","特殊标识"),
    WEAPON("weapon","武器"),
    VIOLENCE("violence ","打斗"),
    CROWD("crowd","聚众"),
    PARADE("parade","游行"),
    CARCRASH("carcrash","车祸现场"),
    TERRORISM("terrorism","暴恐"),
    ABUSE("abuse","辱骂"),
    PORN("porn","色情"),
    SEXY("sexy","性感图片"),
    FLOOD("flood","灌水"),
    CONTRABAND("contraband","违禁"),
    MEANINGLESS("meaningless","无意义"),
    NPX("npx","牛皮藓广告"),
    QRCODE("qrcode","包含二维码"),
    PROGRAMCODE("programCode","包含小程序码"),
    CUSTOMIZED("customized","自定义");

    AliCheckStatusEnum(String status,String desc){
        this.status=status;
        this.desc=desc;
    }

    private String  status;
    private String   desc;

    public String getStatus (){
        return this.status;
    }

    public String getDesc(){
        return this.desc;
    }

    public static String getDescByStatus(String status){
        for (AliCheckStatusEnum aliCheckStatusEnum : AliCheckStatusEnum.values()) {
            if(aliCheckStatusEnum.getStatus().equals(status))
                return aliCheckStatusEnum.getDesc();
        }
        return null;
    }
}
