/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/8/2 9:54:10                             */
/*==============================================================*/


drop table if exists catalog;

drop table if exists course_detail;

drop table if exists course_model_relation;

drop table if exists course_model_type;

drop table if exists crouse;

drop table if exists discuss;

drop table if exists learn_active;

drop table if exists live_telecast;

drop table if exists question;

drop table if exists question_type;

drop table if exists resources;

drop table if exists test_paper;

drop table if exists test_paper_format;

drop table if exists test_paper_question;

drop table if exists test_result;

drop table if exists test_result_detail;

drop table if exists video_info;

/*==============================================================*/
/* Table: catalog                                               */
/*==============================================================*/
create table catalog
(
   catalog_id           int not null comment '目录id',
   name                 varchar(150) comment '目录名称',
   parent_id            int comment '父级目录id（根目录为0）',
   sort                 int comment '排序',
   course_id            int comment '课程id',
   status               tinyint comment '状态：0 禁用，1启用',
   bak1                 varchar(60) comment '备用1',
   bak2                 varchar(60) comment '备用2',
   bak3                 varchar(60) comment '备用3',
   primary key (catalog_id)
);

alter table catalog comment '课程目录表';

/*==============================================================*/
/* Table: course_detail                                         */
/*==============================================================*/
create table course_detail
(
   course_detail_id     int not null comment '主键id',
   course_id            int comment '课程id',
   model_type           int comment '所属模块分类id',
   catalog_id           int comment '目录id(为0时表示不绑定在章节上的课程资源)',
   resources_id         int comment '资源id',
   create_user          int comment '创建者id',
   create_time          date comment '创建时间',
   update_user          int comment '更新者id',
   update_time          date comment '更新时间',
   bak1                 varchar(60) comment '备用1',
   bak2                 varchar(60) comment '备用2',
   bak3                 varchar(60) comment '备用3',
   primary key (course_detail_id)
);

alter table course_detail comment '课程详情表';

/*==============================================================*/
/* Table: course_model_relation                                 */
/*==============================================================*/
create table course_model_relation
(
   course_model_relation_id int comment '主键id',
   course_id            int comment '课程id',
   course_model_type    int comment '模块id'
);

alter table course_model_relation comment '课程模块关系表';

/*==============================================================*/
/* Table: course_model_type                                     */
/*==============================================================*/
create table course_model_type
(
   course_model_type_id int not null comment 'id',
   name                 varchar(30) comment '名称',
   code                 varchar(20) comment '编码',
   sort                 int comment '排序',
   status               tinyint comment '状态：0 禁用；1 启用',
   primary key (course_model_type_id)
);

alter table course_model_type comment '课程模块分类表';

/*==============================================================*/
/* Table: crouse                                                */
/*==============================================================*/
create table crouse
(
   crouse_id            int not null auto_increment comment '课程id',
   name                 varchar(50) comment '课程名称',
   category_one         int comment '类目一级id',
   status               int comment '状态（是否完成）',
   category_two         int comment '类目二级id',
   org_id               int comment '机构id',
   create_user          int comment '创建者id',
   create_time          date comment '创建时间',
   update_user          int comment '更新者id',
   update_time          date comment '更新时间',
   bak1                 varchar(50) comment '备用1',
   bak2                 varchar(50) comment '备用2',
   bak3                 varchar(50) comment '备用3',
   primary key (crouse_id)
);

alter table crouse comment '课程表';

/*==============================================================*/
/* Table: discuss                                               */
/*==============================================================*/
create table discuss
(
   disscuss_id          int not null comment '主键id',
   student_id           int comment '学生id',
   course_id            int comment '课程id',
   resource_id          int comment '讨论资源id',
   content              varchar(5000) comment '讨论内容',
   parent_id            int comment '多级讨论回复的父id，如果回复的是一级讨论，则为0',
   create_time          date comment '创建时间',
   update_user          int comment '修改者id',
   update_time          date comment '修改时间',
   bak1                 varchar(60) comment '备用1',
   bak2                 varchar(60) comment '备用2',
   bak3                 varchar(60) comment '备用3',
   primary key (disscuss_id)
);

alter table discuss comment '讨论表';

/*==============================================================*/
/* Table: learn_active                                          */
/*==============================================================*/
create table learn_active
(
   learn_active_id      int comment '主键id',
   student_id           int comment '学生id',
   course_id            int comment '课程id',
   course_detail_id     int comment '课程详情id',
   learn_flag           tinyint comment '学习标志（0：未学习；1：学习）',
   learn_time           date comment '学习时间(只记录第一次，试卷应该是提交后才算提交完毕)',
   bak1                 varchar(60) comment '备用1',
   bak2                 varchar(60) comment '备用2',
   bak3                 varchar(60) comment '备用3'
);

alter table learn_active comment '学生学习行为表';

/*==============================================================*/
/* Table: live_telecast                                         */
/*==============================================================*/
create table live_telecast
(
   live_telecas_id      int comment '主键id',
   name                 varchar(100) comment '名称',
   start_time           date comment '开始时间',
   end_time             date comment '结束时间',
   des                  varchar(1000) comment '描述',
   check_url            varchar(500) comment '回调url',
   tec_url              varchar(500) comment '老师直播间url',
   ass_url              varchar(500) comment '助教直播间url',
   stu_url              varchar(500) comment '学生直播间url',
   rome_id              varchar(100) comment '直播间id',
   status               int comment '状态',
   playbackUrl          varchar(500) comment '直播回放地址',
   bak1                 varchar(60) comment '备用1',
   bak2                 varchar(60) comment '备用2',
   bak3                 varchar(60) comment '备用3'
);

alter table live_telecast comment '直播';

/*==============================================================*/
/* Table: question                                              */
/*==============================================================*/
create table question
(
   question_id          int not null auto_increment comment '试题id',
   category_one         int not null comment '类目一级id',
   category_two         int not null comment '类目二级id',
   course_id            int not null comment '课程id',
   knowledge_points     varchar(10) not null comment '知识点id,多个使用;分割',
   difficulty_level     int not null comment '难度等级(0：无;1:易；2：中；3：难)',
   question_type        int not null comment '试题类型id',
   question_body        varchar(2000) not null comment '题干',
   question_sound       varchar(50) comment '音频fileId',
   question_opt         varchar(2000) comment '选项(复用与综合体，表示子题id，使用;分割)',
   question_answer      varchar(200) comment '答案',
   question_resolve     varchar(2000) comment '解析',
   org_id               int not null comment '机构id',
   create_user          int not null comment '创建者id',
   create_time          date not null comment '创建时间',
   update_user          int comment '更新者id',
   update_time          date comment '更新时间',
   bak1                 varchar(60) comment '备用1',
   bak2                 varchar(60) comment '备用2',
   bak3                 varchar(60) comment '备用3',
   primary key (question_id)
);

alter table question comment '题库表';

/*==============================================================*/
/* Table: question_type                                         */
/*==============================================================*/
create table question_type
(
   question_type_id     int not null auto_increment comment '试题类型id',
   name                 varchar(20) not null comment '类型名称',
   is_objective         tinyint not null comment '是否是客观题（0:是;1:非）',
   code                 varchar(10) not null comment '编码',
   status               tinyint not null comment '状态：0：启用；1：禁用',
   primary key (question_type_id)
);

alter table question_type comment '试题类型';

/*==============================================================*/
/* Table: resources                                             */
/*==============================================================*/
create table resources
(
   resources_id         int not null comment '资源id',
   title                varchar(250) comment '标题（讨论问答）',
   type                 int comment '资源类型（0：测试；1：作业；2：讨论；3：问答；4：视频；5：直播；6文档；7文本；8资料）',
   org_id               int comment '机构id',
   content              varchar(5000) comment '资源内容',
   create_user          int comment '创建者id',
   creaye_time          date comment '创建时间',
   update_user          int comment '修改者id',
   update_time          date comment '修改时间',
   bak1                 varchar(60) comment '备用1',
   bak2                 varchar(60) comment '备用2',
   bak3                 varchar(60) comment '备用3',
   primary key (resources_id)
);

alter table resources comment '资源表';

/*==============================================================*/
/* Table: test_paper                                            */
/*==============================================================*/
create table test_paper
(
   test_paper_id        int not null auto_increment comment '试卷id',
   name                 varchar(50) not null comment '试卷名称',
   category_one         int not null comment '一级类目id',
   category_two         int not null comment '二级类目id',
   course_id            int not null comment '课程id',
   use_type             tinyint not null comment '用途:0 测试，1作业',
   time                 int comment '时长(单位  分)',
   start_time           date comment '有效期开始时间',
   end_time             date comment '有效期结束时间',
   is_score             tinyint not null comment '是否计分:0计分，1不计分',
   total_score          int comment '总分',
   form_type            int not null comment '组卷类型（0:人工组卷；1:智能组卷）',
   export_path          varchar(50) comment '导出文件path',
   export_time          date comment '导出时间',
   org_id               int not null comment '机构id',
   create_user          int not null comment '创建者id',
   create_time          date not null comment '创建时间',
   update_user          int comment '更新者id',
   update_time          date comment '更新时间',
   primary key (test_paper_id)
);

alter table test_paper comment '试卷';

/*==============================================================*/
/* Table: test_paper_format                                     */
/*==============================================================*/
create table test_paper_format
(
   test_paper_format_id int not null auto_increment comment 'id',
   test_paper_id        int not null comment '试卷id',
   question_num         int not null comment '试题数量',
   question_type        int not null comment '试题类型id',
   question_spec        int not null comment '试题分',
   primary key (test_paper_format_id)
);

alter table test_paper_format comment '试卷组成';

/*==============================================================*/
/* Table: test_paper_question                                   */
/*==============================================================*/
create table test_paper_question
(
   test_paper_question_id int not null auto_increment comment 'id',
   test_pager_id        int not null comment '试卷id',
   question_id          int not null comment '试题id',
   sort                 int not null comment '排序',
   resolve              varchar(2000) comment '解析',
   primary key (test_paper_question_id)
);

alter table test_paper_question comment '试卷题关联';

/*==============================================================*/
/* Table: test_result                                           */
/*==============================================================*/
create table test_result
(
   test_result_id       int not null auto_increment comment 'id',
   student_id           int not null comment '学生id',
   test_paper_id        int not null comment '试卷id',
   status               int not null comment '作业所处状态0:未开始答题；1:继续答题，2已提交；3批改中；4批改完成；5已过期',
   start_time           date comment '开始答题时间',
   end_time             date comment '完成答题时间',
   making_user          int comment '批改人',
   marking_time         date comment '批改时间',
   objective_score      int comment '客观题得分',
   subjective_score     int comment '主观题得分',
   total_score          int comment '总得分',
   primary key (test_result_id)
);

alter table test_result comment '学生测试结果/老师批改';

/*==============================================================*/
/* Table: test_result_detail                                    */
/*==============================================================*/
create table test_result_detail
(
   test_result_detail_id int not null auto_increment comment 'id',
   test_result_id       int not null comment '作答结果id',
   question_id          int not null comment '试题id',
   give_answer          varchar(200) comment '给出的答案',
   score                int comment '获得的分数',
   comment              varchar(2000) comment '评语',
   primary key (test_result_detail_id)
);

alter table test_result_detail comment '试题详情';

/*==============================================================*/
/* Table: video_info                                            */
/*==============================================================*/
create table video_info
(
   video_info_id        int comment '主键id',
   storeDatetime        varchar(50),
   bucketName           varchar(50),
   extensionName        varchar(50),
   endpoint             varchar(50),
   securitytoken        varchar(5000),
   accesskeysecret      varchar(200),
   storageType          varchar(50),
   expiration           varchar(50),
   key_world            varchar(50),
   accesskeyid          varchar(50),
   fileId               varchar(50),
   url                  varchar(200) comment '播放地址',
   create_time          date comment '创建时间',
   video_name           varchar(100) comment '视频名称',
   bak1                 varchar(50) comment '备用1',
   bak2                 varchar(50) comment '备用2',
   bak3                 varchar(50) comment '备用3'
);

alter table video_info comment '录播视频表';

