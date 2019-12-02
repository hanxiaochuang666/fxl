drop table if exists manager_account;

drop table if exists manager_account_extend;

drop table if exists manager_account_role_r;

drop table if exists manager_log;

drop table if exists manager_org_apply;

drop table if exists manager_organization;

drop table if exists manager_permission;

drop table if exists manager_role;

drop table if exists manager_role_permission_r;

drop table if exists manager_teacher_apply;

drop table if exists manager_teacher_exp;

drop table if exists sso_client;

drop table if exists sso_user;

/*==============================================================*/
/* Table: manager_account                                       */
/*==============================================================*/
create table manager_account
(
   account_id           varchar(32) not null comment '后台用户表Id',
   user_id              varchar(32) not null comment '用户表Id',
   user_name            national varchar(50) not null comment '用户名',
   type                 int comment '用户类型( 1本部人员；2.教师；3第三方机构，4租户)',
   org_code             national varchar(25) comment '所属组织编码',
   status               int not null comment '用户状态（1. 正常 ，2 停用）',
   is_manager           tinyint(1) comment '是否是管理者',
   sort                 int comment '排序',
   is_deleted           tinyint(1) comment '是否删除',
   create_time          datetime not null comment '添加时间',
   create_by            national varchar(50) not null comment '添加人',
   modify_time          datetime not null comment '修改时间',
   modify_by            national varchar(50) not null comment '修改人',
   primary key (account_id),
   key AK_UQ_UserName (user_name)
);

alter table manager_account comment '后台用户表';

/*==============================================================*/
/* Table: manager_account_extend                                */
/*==============================================================*/
create table manager_account_extend
(
   account_id           varchar(32) not null comment '后台用户表Id',
   user_id              varchar(32) not null comment '用户表Id',
   user_name            varchar(50) not null comment '用户账号',
   description          national varchar(500) comment '教师简介',
   teach_course         varchar(300) comment '讲授课程',
   attribute            json comment '自定义属性(JSON)',
   create_time          datetime not null comment '添加时间',
   create_by            varchar(200) not null comment '添加人',
   modify_time          datetime comment '修改时间',
   modify_by            varchar(200) comment '修改人',
   primary key (account_id)
);

alter table manager_account_extend comment '用户扩展信息';

/*==============================================================*/
/* Table: manager_account_role_r                                */
/*==============================================================*/
create table manager_account_role_r
(
   account_role_r_id    varchar(32) not null comment '账号角色关联表Id',
   role_id              varchar(32) not null comment '角色ID',
   account_id           varchar(32) not null comment '用户ID',
   create_time          datetime not null comment '添加时间',
   create_by            national varchar(50) not null comment '添加人',
   modify_time          datetime not null comment '修改时间',
   modify_by            national varchar(50) not null comment '修改人',
   primary key (account_role_r_id)
);

alter table manager_account_role_r comment '账号角色关联表';

/*==============================================================*/
/* Table: manager_log                                           */
/*==============================================================*/
create table manager_log
(
   log_id               varchar(32) not null comment '表Id',
   opt_name             national varchar(50) not null comment '操作人',
   opt_type             national varchar(25) not null comment '操作类型',
   opt_description      national varchar(2000) comment '操作描述',
   opt_time             datetime not null comment '操作时间',
   opt_i_p              national varchar(50) comment '操作的IP地址',
   create_time          datetime not null comment '添加时间',
   create_by            national varchar(50) not null comment '添加人',
   modify_time          datetime not null comment '修改时间',
   modify_by            national varchar(50) not null comment '修改人',
   primary key (log_id)
);

alter table manager_log comment '系统日志表';

/*==============================================================*/
/* Table: manager_org_apply                                     */
/*==============================================================*/
create table manager_org_apply
(
   org_apply_id         varchar(32) not null comment '机构入驻申请表Id',
   user_name            national varchar(50) not null comment '申请人账号',
   apply_name           varchar(50) comment '申请人姓名',
   phone                varchar(20) comment '手机',
   email                varchar(50) comment 'Email',
   organization_pic     varchar(200) comment '机构图标',
   organization_name    varchar(100) comment '机构名称',
   organization_description varchar(200) comment '机构简介',
   organization_brief   varchar(100) comment '一句话介绍',
   main_category        int comment '主营类目',
   commitment_pic       varchar(200) comment '承诺书图片',
   organization_nature  int comment '机构性质（1 有营业执照；2 没有营业执照）',
   license              varchar(200) comment '营业执照/教育资质证书',
   number               varchar(200) comment '营业执照注册号/证书编号',
   license_term         int comment '证件有效期（1 长期有效；2 固定时限）',
   license_term_time    date comment '证件有效期至',
   legal_front_pic      varchar(200) comment '法人身份证正面照',
   legal_back_pic       varchar(200) comment '法人身份证背面照',
   verify_status        int not null comment '审核状态（0 未审核，1 审核成功，2 审核失败）',
   verify_note          varchar(3000) comment '审核备注',
   note                 varchar(3000) comment '备注',
   create_time          datetime not null comment '添加时间',
   create_by            varchar(200) not null comment '添加人',
   modify_time          datetime comment '修改时间',
   modify_by            varchar(200) comment '修改人',
   primary key (org_apply_id)
);

alter table manager_org_apply comment '机构入驻申请表';

/*==============================================================*/
/* Table: manager_organization                                  */
/*==============================================================*/
create table manager_organization
(
   organization_id      varchar(32) not null comment '机构表d',
   apply_id             varchar(32) comment '申请表Id',
   org_code             national varchar(50) comment '入驻者组织编码',
   type                 int comment '用户类型( 1.本部；2.教师；3第三方机构；4租户)',
   user_name            national varchar(50) not null comment '负责人账号',
   real_name            varchar(50) comment '负责人真实姓名',
   status               int not null comment '状态（1. 正常 ，2 停用）',
   is_deleted           tinyint(1) comment '是否删除',
   create_time          datetime not null comment '添加时间',
   create_by            national varchar(50) not null comment '添加人',
   modify_time          datetime not null comment '修改时间',
   modify_by            national varchar(50) not null comment '修改人',
   Column_13            char(10),
   primary key (organization_id),
   key AK_Key_2 (org_code)
);

alter table manager_organization comment '机构表（入驻的机构或教师个人）';

/*==============================================================*/
/* Table: manager_permission                                    */
/*==============================================================*/
create table manager_permission
(
   permission_id        varchar(32) not null comment '表Id',
   menu_name            national varchar(50) comment '菜单名称',
   menu_code            national varchar(50) not null comment '菜单编号',
   parent_id            varchar(32) not null comment '所属父ID(顶级为0)',
   type                 int not null comment '类型（1菜单 2按扭，3API）',
   is_system            tinyint(1) comment '是否系统级别',
   is_display           tinyint(1) not null comment '是否可见',
   url                  national varchar(50) comment '地址',
   icon                 national varchar(25) comment '菜单图标（只存在于一级菜单）',
   status               int not null comment '菜单状态（1 正常 ，2 停用）',
   sort                 int comment '排序',
   is_deleted           tinyint(1) comment '是否删除',
   note                 national varchar(250) comment '备注',
   create_time          datetime not null comment '添加时间',
   create_by            national varchar(50) not null comment '添加人',
   modify_time          datetime not null comment '修改时间',
   modify_by            national varchar(50) not null comment '修改人',
   primary key (permission_id)
);

alter table manager_permission comment '权限表';

/*==============================================================*/
/* Table: manager_role                                          */
/*==============================================================*/
create table manager_role
(
   role_id              varchar(32) not null comment '角色表Id',
   role_name            national varchar(50) not null comment '角色名称',
   status               int not null comment '角色状态（1 正常 ，2 停用）',
   is_system            tinyint(1) comment '是否系统级别',
   org_code             national varchar(25) not null comment '所属组织编码',
   description          national varchar(250) comment '角色描述',
   is_deleted           tinyint(1) not null comment '是否删除',
   create_time          datetime not null comment '添加时间',
   create_by            national varchar(50) not null comment '添加人',
   modify_time          datetime not null comment '修改时间',
   modify_by            national varchar(50) not null comment '修改人',
   primary key (role_id)
);

alter table manager_role comment '角色表';

/*==============================================================*/
/* Table: manager_role_permission_r                             */
/*==============================================================*/
create table manager_role_permission_r
(
   role_permission_r_id varchar(32) not null comment '角色权限表Id',
   role_id              varchar(32) not null comment '角色ID',
   permission_id        varchar(32) not null comment '菜单ID',
   create_time          datetime not null comment '添加时间',
   create_by            national varchar(50) not null comment '添加人',
   modify_time          datetime comment '修改时间',
   modify_by            national varchar(50) comment '修改人',
   primary key (role_permission_r_id)
);

alter table manager_role_permission_r comment '角色权限表';

/*==============================================================*/
/* Table: manager_teacher_apply                                 */
/*==============================================================*/
create table manager_teacher_apply
(
   teacher_apply_id     varchar(32) not null comment '教师入驻申请表Id',
   real_name            varchar(20) comment '真实姓名',
   head_pic             varchar(200) comment '头像图片',
   main_category        int comment '主营类目',
   q_q                  varchar(20) comment 'QQ',
   email                varchar(50) comment 'Email',
   personal_profile     varchar(200) comment '个人简介',
   phone                varchar(20) comment '手机',
   commitment_pic       varchar(200) comment '承诺书图片',
   identification       varchar(30) comment '身份证',
   id_front_pic         varchar(200) comment '手持身份证正面照',
   id_back_pic          varchar(200) comment '手持身份证背面照',
   teacher_certificate  varchar(200) comment '教师资格证',
   educationr_certificate varchar(200) comment '学历证',
   professionalr_certificate varchar(200) comment '专业证书',
   otherr_certificate   varchar(200) comment '其他',
   verify_status        int not null comment '审核状态（0 未审核，1 审核成功，2 审核失败）',
   verify_note          varchar(3000) comment '审核备注',
   note                 varchar(3000) comment '备注',
   create_time          datetime not null comment '添加时间',
   create_by            varchar(200) not null comment '添加人',
   modify_time          datetime comment '修改时间',
   modify_by            varchar(200) comment '修改人',
   primary key (teacher_apply_id)
);

alter table manager_teacher_apply comment '教师入驻申请表';

/*==============================================================*/
/* Table: manager_teacher_exp                                   */
/*==============================================================*/
create table manager_teacher_exp
(
   teacher_exp_id       varchar(32) not null comment '教师工作经历表d',
   teacher_apply_id     varchar(32) not null comment '教师入驻申请表Id',
   category             int not null comment '类别（1 教学工作经历；2 教学工作成果）',
   start_time           date comment '开始时间',
   end_time             date comment '结束时间',
   description          varchar(100) not null comment '经历描述',
   sort                 int comment '排序',
   create_time          datetime not null comment '添加时间',
   create_by            varchar(200) not null comment '添加人',
   modify_time          datetime comment '修改时间',
   modify_by            varchar(200) comment '修改人',
   primary key (teacher_exp_id)
);

alter table manager_teacher_exp comment '教师工作经历表';

/*==============================================================*/
/* Table: sso_client                                            */
/*==============================================================*/
create table sso_client
(
   id                   varchar(32) not null comment '表Id',
   client_name          varchar(100) comment '客户端名称',
   client_id            varchar(50) not null comment '客户端Id',
   client_secret        national varchar(500) comment '客户端密钥',
   description          varchar(2000) comment '客户端描述',
   create_time          datetime not null comment '添加时间',
   create_by            varchar(200) not null comment '添加人',
   modify_time          datetime comment '修改时间',
   modify_by            varchar(200) comment '修改人',
   primary key (id, client_id)
);

alter table sso_client comment '客户端表';

/*==============================================================*/
/* Table: sso_user                                              */
/*==============================================================*/
create table sso_user
(
   user_id              varchar(32) not null comment '统一用户表Id',
   user_name            national varchar(50) not null comment '账号',
   password             national varchar(50) not null comment '密码',
   real_name            varchar(50) comment '真实姓名',
   status               int not null comment '用户状态（1 正常，2停用，3锁定）',
   header_url           varchar(200) comment '头像地址',
   nick_name            varchar(50) comment '昵称',
   sex                  int comment '性别(0 未知；1女；2男)',
   phone                varchar(30) comment '手机号',
   email                varchar(50) comment '邮箱',
   q_q_open_id          varchar(100) comment '绑定QQ',
   wechat_open_id       varchar(100) comment '绑定微信',
   education            varchar(20) comment '学历',
   province             varchar(10) comment '所在省',
   city                 varchar(10) comment '所在市',
   consignee_name       varchar(20) comment '收货人姓名',
   consignee_phone      varchar(20) comment '收货人手机号',
   consignee_province   varchar(20) comment '收货人地址（省）',
   consignee_city       varchar(20) comment '收货人地址（市）',
   consignee_district   varchar(20) comment '收货人地址（区）',
   consignee_address    varchar(200) comment '收货人地址（详细）',
   sort                 int comment '排序',
   is_deleted           tinyint(1) comment '是否删除',
   create_time          datetime not null comment '添加时间',
   create_by            national varchar(50) not null comment '添加人',
   modify_time          datetime not null comment '修改时间',
   modify_by            national varchar(50) not null comment '修改人',
   primary key (user_id),
   key AK_Key_2 (user_name),
   key AK_Key_3 (phone)
);

alter table sso_user comment '统一用户表';

alter table manager_account_role_r add constraint account_role_r_ibfk_1 foreign key (role_id)
      references manager_role (role_id);

alter table manager_account_role_r add constraint account_role_r_ibfk_2 foreign key (account_id)
      references manager_account (account_id);

alter table manager_role_permission_r add constraint role_menu_r_ibfk_1 foreign key (role_id)
      references manager_role (role_id);

alter table manager_role_permission_r add constraint role_menu_r_ibfk_2 foreign key (permission_id)
      references manager_permission (permission_id);

alter table manager_teacher_exp add constraint FK_Reference_7 foreign key (teacher_apply_id)
      references manager_teacher_apply (teacher_apply_id) on delete restrict on update restrict;
