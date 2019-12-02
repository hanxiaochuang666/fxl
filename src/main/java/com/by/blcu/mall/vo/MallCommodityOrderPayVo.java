package com.by.blcu.mall.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.by.blcu.mall.model.MallPaymentInvoice;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MallCommodityOrderPayVo implements Serializable {
    private static final long serialVersionUID = -3615151831112855428L;

    /**
     * 序号
     */
    @Excel(name = "序号",orderNum = "0",width = 7)
    private Integer num;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 用户ID
     */
    private String userName;

    /**
     * 二级分类
     */
    @Excel(name = "二级分类",orderNum = "6",width = 25)
    private String ccName;

    /**
     * 学生姓名
     */
    @Excel(name = "学生姓名",orderNum = "3")
    private String name;

    /**
     * 创建日期
     */
    @Excel(name = "交易时间",orderNum = "1",databaseFormat="yyyy-MM-dd",format="yyyy-MM-dd",width = 15)
    private Date createTime;

    /**
     * 机构
     */
    private String org;

    /**
     * 机构名字
     */
    //@Excel(name = "机构名字",orderNum = "18",width = 25)
    private String orgName;

    /**
     * 订单状态（删除：0，未删除：1）
     */
    private Integer orderStatus;

    /**
     * 是否需要开发票（否：0，是：1）
     */
    private Integer isBill;

    /**
     * 是否已经开发票（否：0，是：1）
     */
    private Integer isdrawBill;

    /**
     * 订单号
     */
    @Excel(name = "商户订单号",orderNum = "5",width = 25)
    private String orderNo;

    /**
     * 支付状态(未支付：0，已支付：1)
     */
    private Integer paymentStatus;

    /**
     * 总金额
     */
    @Excel(name = "实交学费",orderNum = "12")
    private Double amount;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 手机号
     */
    private String telNum;

    /**
     * 支付方式
     */
    @Excel(name = "交易方式", replace = { "易宝_1", "校园_2", "汇款_3", "现场_4", "_null" },orderNum = "2",isImportField = "true")
    private Integer payType;

    /**
     * 原价
     */
    @Excel(name = "应交学费",orderNum = "10")
    private Double costPrice;


    /**
     * 开始日期
     */
    private Date createTimeStart;

    /**
     * 结束日期
     */
    private Date createTimeEnd;

    /**
     * 课程名称
     */
    @Excel(name = "商品名称",orderNum = "7")
    private String courseName;

    /**
     * 授课类型
     */
    private Integer lessonType;

    /**
     * 课时
     */
    @Excel(name = "培训课时",orderNum = "8")
    private Integer classTime;

    /**
     * 原价
     */

    private Double price;

    /**
     * 优惠价
     */

    private Double preferential;

    /**
     * 课时标准
     */
    //@Excel(name = "课时标准",orderNum = "9")
    private Double standard;

    /**
     * 课时标准展示
     */
    @Excel(name = "课时标准",orderNum = "9")
    private String standardView;

    /**
     * 优惠金额
     */
    @Excel(name = "优惠金额",orderNum = "11")
    private Double paidTuition;

    /**
     *VARCHAR
     *发票类型
     */
    @Excel(name = "发票类型", replace = { "增值税专用发票_0", "增值税普通发票_1", "_null" },orderNum = "14",isImportField = "true")
    private String type;

    /**
     *INTEGER
     *发票抬头（0 个人，1单位）
     */
    @Excel(name = "发票抬头", replace = { "个人_0", "单位_1", "_null" },orderNum = "13",isImportField = "true")
    private Integer invoice;

    /**
     *VARCHAR
     *公司名称
     */
    @Excel(name = "公司名称",orderNum = "15")
    private String companyName;

    /**
     *VARCHAR
     *纳税人识别号
     */
    @Excel(name = "纳税人识别号",orderNum = "16",width = 20)
    private String taxpayerNumber;

    /**
     *VARCHAR
     *邮寄地址
     */
    @Excel(name = "邮寄地址",orderNum = "17")
    private String address;

    /**
     *VARCHAR
     *邮政编码
     */
    @Excel(name = "邮政编码",orderNum = "18")
    private String zipCode;

    /**
     *VARCHAR
     *注册地址
     */
    @Excel(name = "注册地址",orderNum = "19")
    private String registeredAddress;

    /**
     *VARCHAR
     *注册电话
     */
    @Excel(name = "注册电话",orderNum = "20")
    private String registeredPhone;

    /**
     *VARCHAR
     *开户行
     */
    @Excel(name = "开户行",orderNum = "21")
    private String bank;

    /**
     *VARCHAR
     *公司账号
     */
    @Excel(name = "公司账号",orderNum = "22")
    private String companyAccount;

    /**
     *VARCHAR
     *汇款人
     */
    @Excel(name = "汇款账户名",orderNum = "4",width = 15)
    private String remittance;

}