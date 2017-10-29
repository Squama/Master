drop table if exists tbl_materiel;
create table tbl_materiel 
(
   ID               VARCHAR(64)                   not null comment '物料内码',
   mat_number                 VARCHAR(20)                   null comment '物料编号',
   mat_name               VARCHAR(64)                   null comment '物料名称',
   mat_standard                 VARCHAR(64)                   null comment '物料规格',
   unit                   VARCHAR(64)                   null comment '单位',
   isValid                 VARCHAR(2)                    null comment '有效标识',
   create_name          VARCHAR(64)                   null comment '创建人',
   create_name_ID        VARCHAR(64)                   null comment '创建人的ID',
   create_time          DATE                           null comment '创建时间',
   update_name          VARCHAR(64)                   null comment '更新人',
   update_name_ID       VARCHAR(64)                   null comment '更新人ID',
   update_time          DATE                           null comment '更新时间',
   parent_ID		VARCHAR(64)			null comment '上级节点ID',
 type			VARCHAR(64)			null comment '节点类型',
 reserve1       VARCHAR(64)				 null comment '预留字段1',
reserve2       VARCHAR(64)			 null comment '预留字段2',
reserve3       VARCHAR(64)			 null comment '预留字段3',
   constraint PK_TBL_MATERIEL primary key (ID)
);

drop table if exists tbl_channel;

create table tbl_channel
(

ID               VARCHAR(64)                   	not null comment '物料内码',

mat_number        VARCHAR(20)          null comment '所属物料编号',

mat_name               VARCHAR(64)                   null comment '物料名称',
isValid                 VARCHAR(2)                    null comment '有效标识',

supplier                VARCHAR(64)                    null comment '供应商',

 price                DOUBLE                         null comment '单价',
	 create_name          VARCHAR(64)                   null comment '创建人',

create_name_ID        VARCHAR(64)                   null comment '创建人ID',

create_time          DATE                           null comment '创建时间',

update_name          VARCHAR(64)                   null comment '更新人',

update_name_ID       VARCHAR(64)                   null comment '更新人ID',

update_time          DATE                           null comment '更新时间',

reserve1       VARCHAR(64)		null comment '预留字段1',

reserve2       VARCHAR(64)		 null comment '预留字段2',

reserve3       VARCHAR(64)		 null comment '预留字段3',

constraint PK_TBL_CHANNEL primary key (ID)

);

drop table if exists tbl_budget;
create table tbl_budget
(
`budget_ID` varchar(64) NOT NULL COMMENT '预算ID',

`budget_name` varchar(255) NULL COMMENT '预算信息名称',

`project_ID` varchar(64) NOT NULL COMMENT '所属项目ID',

`budget_people_ID` varchar(64) NULL COMMENT '预算人员ID',

`approver_ID` varchar(64) NULL COMMENT '审核人ID',

`isApprover` int(10) NULL COMMENT '审核标志（0：未审核；1：已审核）',

`edit_people_ID` varchar(64) NULL COMMENT '编辑人ID（供应渠道）',

`isEdit` int(10) NULL COMMENT '编辑标志（0：未编辑；1已编辑）',


`reserve1` varchar(255) NULL COMMENT '预留字段1',

`reserve2` varchar(255) NULL COMMENT '预留字段2',

`reserve3` varchar(255) NULL COMMENT '预留字段3',

   constraint PK_TBL_BUDGET primary key (budget_ID)
);


drop table if exists tbl_purchase_history;

create table tbl_purchase_history
(
`purchase_ID` varchar(64) NOT NULL COMMENT '采购ID',

`budget_ID` varchar(64) NOT NULL COMMENT '预算ID',

`mat_number` varchar(64) NULL COMMENT '物料编号',

`supplier` varchar(255) NULL COMMENT '供应商',

`price` double(2,0) NULL COMMENT '单价',

`purchase_name_ID` varchar(64) NULL COMMENT '采购人员ID',

`purchase_time` date NULL COMMENT '采购人渠道编辑时间',

`reserve1` varchar(255) NULL COMMENT '预留字段1',

`reserve2` varchar(255) NULL COMMENT '预留字段2',

`reserve3` varchar(255) NULL COMMENT '预留字段3',
constraint PK_TBL_PURCHASE_HISTORY primary key (purchase_ID)

);


insert into tbl_dict
(id,deleted,code,name,parent_id)
values
('102901',0,'LS','临设',''),
('10290101',0,'LS_BF','板房','102901'),
('1029010101',0,'LS_BF_JC','建材','10290101'),
('1029010102',0,'LS_BF_LD','临电','10290101'),
('1029010103',0,'LS_BF_MC','门窗','10290101'),
('1029010104',0,'LS_BF_QT','其他','10290101'),

('10290102',0,'LS_TJ','土建','102901'),
('1029010201',0,'LS_TJ_ZTS','砖砼砂','10290102'),
('1029010202',0,'LS_TJ_LDLS','临电临水','10290102'),

('10290103',0,'LS_QT','其他','102901'),

('102902',0,'ZT','主体',''),
('10290201',0,'ZT_JC','建材','102902'),
('1029020101',0,'ZT_JC_GJ','钢筋','10290201'),
('1029020102',0,'ZT_JC_HNT','混凝土','10290201'),
('1029020103',0,'ZT_JC_Z','砖','10290201'),
('1029020104',0,'ZT_JC_SS','砂石','10290201'),
('1029020105',0,'ZT_JC_SN','水泥','10290201'),
('1029020106',0,'ZT_JC_MB','模板','10290201'),
('1029020107',0,'ZT_JC_GG','钢管','10290201'),
('1029020108',0,'ZT_JC_DT','顶托','10290201'),
('1029020109',0,'ZT_JC_FM','方木','10290201'),
('1029020110',0,'ZT_JC_ZSDZ','扎丝钉子','10290201'),
('1029020111',0,'ZT_JC_JQTQK','加气砼砌块','10290201'),
('1029020112',0,'ZT_JC_TXLG','T型拉杆','10290201'),
('1029020113',0,'ZT_JC_QT','其他','10290201'),
('1029020114',0,'ZT_JC_MGXG','PVC木工线管','10290201'),

('102903',0,'ZS','装饰',''),
('10290301',0,'ZS_MC','门窗','102903'),
('1029030101',0,'ZS_MC_M','门','10290301'),
('1029030102',0,'ZS_MC_C','窗','10290301'),
('10290302',0,'ZS_FS','防水','102903'),
('1029030201',0,'ZS_FS_WM','屋面','10290302'),
('1029030202',0,'ZS_FS_CW','厨卫','10290302'),
('10290303',0,'ZS_FS','粉刷','102903'),
('1029030301',0,'ZS_FS_FS','粉刷','10290303'),
('1029030302',0,'ZS_FS_YQTL','油漆涂料','10290303'),
('10290304',0,'ZS_DM','地面','102903'),
('10290305',0,'ZS_WM','屋面','102903'),
('10290306',0,'ZS_BW','保温','102903'),
('10290307',0,'ZS_QT','其他','102903'),

('102904',0,'AZ','安装',''),
('10290401',0,'AZ_SGC','水管材','102904'),
('1029040101',0,'AZ_SGC_GSZG','供水主管','10290401'),
('102904010101',0,'AZ_SGC_GSZG_GG','钢管类','1029040101'),
('1029040102',0,'AZ_SGC_GSPJ','供水配件','10290401'),
('102904010201',0,'AZ_SGC_GSPJ_FM','阀门类','1029040102'),
('102904010202',0,'AZ_SGC_GSPJ_FL','法兰.接头类','1029040102'),
('102904010203',0,'AZ_SGC_GSPJ_HJ','焊接配件类','1029040102'),
('102904010204',0,'AZ_SGC_GSPJ_PPJ','P管件类','1029040102'),
('102904010205',0,'AZ_SGC_GSPJ_GS','钢塑.配件','1029040102'),
('1029040103',0,'AZ_SGC_PS','排水','10290401'),
('1029040104',0,'AZ_SGC_SDRD','消防水电弱点','10290401'),
('1029040105',0,'AZ_SGC_QTCL','消防水电其他材料','10290401'),
('1029040106',0,'AZ_SGC_NQ','暖气','10290401'),
('102904010601',0,'AZ_SGC_NQ_GG','钢管类','1029040106'),
('102904010602',0,'AZ_SGC_NQ_NQPJ','暖气配件类','1029040106'),
('1029040107',0,'AZ_SGC_PPR','PPR水管配件','10290401'),
('1029040108',0,'AZ_SGC_PE','PE水管配件','10290401'),

('10290402',0,'AZ_SD','水电','102904'),
('1029040201',0,'AZ_SD_DX','电线','10290402'),
('1029040202',0,'AZ_SD_DL','电缆','10290402'),
('1029040203',0,'AZ_SD_QJ','桥架','10290402'),
('1029040204',0,'AZ_SD_PDX','配电箱','10290402'),
('1029040205',0,'AZ_SD_KG','开关面板','10290402'),
('1029040206',0,'AZ_SD_DL','电料','10290402'),
('1029040207',0,'AZ_SD_LB','漏保.闸刀','10290402'),

('102905',0,'AF','安防',''),
('10290501',0,'AF_AQ','安全','102905'),
('1029050101',0,'AF_AQ_GG','钢管','10290501'),
('1029050102',0,'AF_AQ_KJ','扣件','10290501'),
('1029050103',0,'AF_AQ_YQ','油漆','10290501'),
('10290502',0,'AF_DXHFH','定型化防护','102905'),
('1029050201',0,'AF_DXHFH_DTM','电梯门','10290502'),
('1029050202',0,'AF_DXHFH_FHJ','外挑防护架','10290502'),
('1029050203',0,'AF_DXHFH_XG','型钢','10290502'),
('1029050204',0,'AF_DXHFH_FHLP','防护栏棚','10290502'),
('1029050205',0,'AF_DXHFH_QT','其他','10290502'),
('10290503',0,'AF_XF','消防','102905'),

('102906',0,'SB','设备',''),
('10290601',0,'SB_SB','设备','102906'),
('1029060101',0,'SB_SB_CL','车辆','10290601'),
('1029060102',0,'SB_SB_TD','塔吊','10290601'),
('1029060103',0,'SB_SB_SGDT','施工电梯','10290601'),
('1029060104',0,'SB_SB_JBJ','搅拌机','10290601'),
('1029060105',0,'SB_SB_XXJX','小型机械','10290601'),
('1029060106',0,'SB_SB_GJMG','钢筋木工用','10290601'),
('1029060107',0,'SB_SB_ZX','装修用','10290601'),
('1029060108',0,'SB_SB_QT','其他','10290601'),

('102907',0,'BZJ','标准件',''),
('102908',0,'GJL','工具类',''),
('102909',0,'BGYP','办公用品','')
