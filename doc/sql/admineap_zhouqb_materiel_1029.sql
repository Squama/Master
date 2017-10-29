drop table if exists tbl_materiel;
create table tbl_materiel 
(
   ID               VARCHAR(64)                   not null comment '��������',
   mat_number                 VARCHAR(20)                   null comment '���ϱ��',
    mat_name               VARCHAR(20)                   null comment '��������',
   mat_standard                 VARCHAR(64)                   null comment '���Ϲ��',
   unit                   VARCHAR(64)                   null comment '��λ',
   isValid                 VARCHAR(2)                    null comment '��Ч��ʶ',
   create_name          VARCHAR(64)                   null comment '������',
   create_name_ID        VARCHAR(64)                   null comment '������ID',
   create_time          DATE                           null comment '����ʱ��',
   update_name          VARCHAR(64)                   null comment '������',
   update_name_ID       VARCHAR(64)                   null comment '������ID',
   update_time          DATE                           null comment '����ʱ��',
   parent_ID		VARCHAR(64)			null comment '�ϼ��ڵ�ID',
 type			VARCHAR(64)			null comment '�ڵ�����',
 reserve1       VARCHAR(64)				 null comment 'Ԥ���ֶ�1',
reserve2       VARCHAR(64)			 null comment 'Ԥ���ֶ�2',
reserve3       VARCHAR(64)			 null comment 'Ԥ���ֶ�3',
   constraint PK_TBL_MATERIEL primary key (ID)
);

drop table if exists tbl_channel;

create table tbl_channel 
(
 
ID               VARCHAR(64)                   	not null comment '��������',
 
mat_number        VARCHAR(20)          null comment '�������ϱ��',
   
mat_name               VARCHAR(20)                   null comment '��������',
isValid                 VARCHAR(2)                    null comment '��Ч��ʶ',
   
supplier                VARCHAR(64)                    null comment '��Ӧ��',
  
 price                DOUBLE                         null comment '����',							
	 create_name          VARCHAR(64)                   null comment '������',
   
create_name_ID        VARCHAR(64)                   null comment '������ID',
   
create_time          DATE                           null comment '����ʱ��',
   
update_name          VARCHAR(64)                   null comment '������',
   
update_name_ID       VARCHAR(64)                   null comment '������ID',
   
update_time          DATE                           null comment '����ʱ��',
	 
reserve1       VARCHAR(64)		null comment 'Ԥ���ֶ�1',
	 
reserve2       VARCHAR(64)		 null comment 'Ԥ���ֶ�2',
	 
reserve3       VARCHAR(64)		 null comment 'Ԥ���ֶ�3',
   
constraint PK_TBL_CHANNEL primary key (ID)

);

drop table if exists tbl_purchase_history;

create table tbl_purchase_history 
(
   
ID               		VARCHAR(64)                  not null comment '��ʷ�ɹ�ID',
   
mat_number           VARCHAR(20)                  null comment '���ϱ��',
   
mat_name             VARCHAR(64)                  null comment '��������',
   
supplier             VARCHAR(64)                    null comment '��Ӧ��',
   
price                DOUBLE                         null comment '����',							
	 purchase_name        VARCHAR(64)                   null comment '�ɹ���Ա',
   
purchase_name_ID     VARCHAR(64)                   null comment '�ɹ���ԱID',
   
purchase_time        DATE                           null comment '�ɹ�ʱ��',
	 
project_name	VARCHAR(64)		null comment '������Ŀ',
   
project_ID	VARCHAR(64)		null comment '������ĿID',
	 
reserve1       			VARCHAR(64)			 null comment 'Ԥ���ֶ�1',
	 
reserve2      			  VARCHAR(64)		 null comment 'Ԥ���ֶ�2',
	 
reserve3       			VARCHAR(64)				 null comment 'Ԥ���ֶ�3',
   
constraint PK_TBL_PURCHASE_HISTORY primary key (ID)

);

insert into tbl_dict
(id,deleted,code,name,parent_id)
values
('102901',0,'LS','����',''),
('10290101',0,'LS_BF','�巿','102901'),
('1029010101',0,'LS_BF_JC','����','10290101'),
('1029010102',0,'LS_BF_LD','�ٵ�','10290101'),
('1029010103',0,'LS_BF_MC','�Ŵ�','10290101'),
('1029010104',0,'LS_BF_QT','����','10290101'),

('10290102',0,'LS_TJ','����','102901'),
('1029010201',0,'LS_TJ_ZTS','ש��ɰ','10290102'),
('1029010202',0,'LS_TJ_LDLS','�ٵ���ˮ','10290102'),

('10290103',0,'LS_QT','����','102901'),

('102902',0,'ZT','����',''),
('10290201',0,'ZT_JC','����','102902'),
('1029020101',0,'ZT_JC_GJ','�ֽ�','10290201'),
('1029020102',0,'ZT_JC_HNT','������','10290201'),
('1029020103',0,'ZT_JC_Z','ש','10290201'),
('1029020104',0,'ZT_JC_SS','ɰʯ','10290201'),
('1029020105',0,'ZT_JC_SN','ˮ��','10290201'),
('1029020106',0,'ZT_JC_MB','ģ��','10290201'),
('1029020107',0,'ZT_JC_GG','�ֹ�','10290201'),
('1029020108',0,'ZT_JC_DT','����','10290201'),
('1029020109',0,'ZT_JC_FM','��ľ','10290201'),
('1029020110',0,'ZT_JC_ZSDZ','��˿����','10290201'),
('1029020111',0,'ZT_JC_JQTQK','����������','10290201'),
('1029020112',0,'ZT_JC_TXLG','T������','10290201'),
('1029020113',0,'ZT_JC_QT','����','10290201'),
('1029020114',0,'ZT_JC_MGXG','PVCľ���߹�','10290201'),

('102903',0,'ZS','װ��',''),
('10290301',0,'ZS_MC','�Ŵ�','102903'),
('1029030101',0,'ZS_MC_M','��','10290301'),
('1029030102',0,'ZS_MC_C','��','10290301'),
('10290302',0,'ZS_FS','��ˮ','102903'),
('1029030201',0,'ZS_FS_WM','����','10290302'),
('1029030202',0,'ZS_FS_CW','����','10290302'),
('10290303',0,'ZS_FS','��ˢ','102903'),
('1029030301',0,'ZS_FS_FS','��ˢ','10290303'),
('1029030302',0,'ZS_FS_YQTL','����Ϳ��','10290303'),
('10290304',0,'ZS_DM','����','102903'),
('10290305',0,'ZS_WM','����','102903'),
('10290306',0,'ZS_BW','����','102903'),
('10290307',0,'ZS_QT','����','102903'),

('102904',0,'AZ','��װ',''),
('10290401',0,'AZ_SGC','ˮ�ܲ�','102904'),
('1029040101',0,'AZ_SGC_GSZG','��ˮ����','10290401'),
('102904010101',0,'AZ_SGC_GSZG_GG','�ֹ���','1029040101'),
('1029040102',0,'AZ_SGC_GSPJ','��ˮ���','10290401'),
('102904010201',0,'AZ_SGC_GSPJ_FM','������','1029040102'),
('102904010202',0,'AZ_SGC_GSPJ_FL','����.��ͷ��','1029040102'),
('102904010203',0,'AZ_SGC_GSPJ_HJ','���������','1029040102'),
('102904010204',0,'AZ_SGC_GSPJ_PPJ','P�ܼ���','1029040102'),
('102904010205',0,'AZ_SGC_GSPJ_GS','����.���','1029040102'),
('1029040103',0,'AZ_SGC_PS','��ˮ','10290401'),
('1029040104',0,'AZ_SGC_SDRD','����ˮ������','10290401'),
('1029040105',0,'AZ_SGC_QTCL','����ˮ����������','10290401'),
('1029040106',0,'AZ_SGC_NQ','ů��','10290401'),
('102904010601',0,'AZ_SGC_NQ_GG','�ֹ���','1029040106'),
('102904010602',0,'AZ_SGC_NQ_NQPJ','ů�������','1029040106'),
('1029040107',0,'AZ_SGC_PPR','PPRˮ�����','10290401'),
('1029040108',0,'AZ_SGC_PE','PEˮ�����','10290401'),

('10290402',0,'AZ_SD','ˮ��','102904'),
('1029040201',0,'AZ_SD_DX','����','10290402'),
('1029040202',0,'AZ_SD_DL','����','10290402'),
('1029040203',0,'AZ_SD_QJ','�ż�','10290402'),
('1029040204',0,'AZ_SD_PDX','�����','10290402'),
('1029040205',0,'AZ_SD_KG','�������','10290402'),
('1029040206',0,'AZ_SD_DL','����','10290402'),
('1029040207',0,'AZ_SD_LB','©��.բ��','10290402'),

('102905',0,'AF','����',''),
('10290501',0,'AF_AQ','��ȫ','102905'),
('1029050101',0,'AF_AQ_GG','�ֹ�','10290501'),
('1029050102',0,'AF_AQ_KJ','�ۼ�','10290501'),
('1029050103',0,'AF_AQ_YQ','����','10290501'),
('10290502',0,'AF_DXHFH','���ͻ�����','102905'),
('1029050201',0,'AF_DXHFH_DTM','������','10290502'),
('1029050202',0,'AF_DXHFH_FHJ','����������','10290502'),
('1029050203',0,'AF_DXHFH_XG','�͸�','10290502'),
('1029050204',0,'AF_DXHFH_FHLP','��������','10290502'),
('1029050205',0,'AF_DXHFH_QT','����','10290502'),
('10290503',0,'AF_XF','����','102905'),

('102906',0,'SB','�豸',''),
('10290601',0,'SB_SB','�豸','102906'),
('1029060101',0,'SB_SB_CL','����','10290601'),
('1029060102',0,'SB_SB_TD','����','10290601'),
('1029060103',0,'SB_SB_SGDT','ʩ������','10290601'),
('1029060104',0,'SB_SB_JBJ','�����','10290601'),
('1029060105',0,'SB_SB_XXJX','С�ͻ�е','10290601'),
('1029060106',0,'SB_SB_GJMG','�ֽ�ľ����','10290601'),
('1029060107',0,'SB_SB_ZX','װ����','10290601'),
('1029060108',0,'SB_SB_QT','����','10290601'),

('102907',0,'BZJ','��׼��',''),
('102908',0,'GJL','������',''),
('102909',0,'BGYP','�칫��Ʒ','')
