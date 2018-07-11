junrunsystem
负责人 
=======================================
A-dong
B-wang

        
版本历史
=======================================
v1.0.2.06
---------------------------------------
1.新增表tbl_project_sub项目子项，废除表tbl_labor_sub;
2.tbl_labor表增加人材机包和对应的消耗字段,去掉之前的索引，增加项目、合同名称的索引
3.tbl_budget去掉劳务合同的关联字段，增加项目子项关联字段


v1.0.2.05（未上线，2018-06-01，王志浩）
---------------------------------------
1.新增表：tbl_labor_sub,tbl_salary_volume;tabl_labor_sub唯一索引：laborID,SubName
2.修改表：tbl_labor,增加package_price包工包料总金额；
3.修改表：tbl_labor,去掉消耗字段
4.tbl_project_volume增加labor_sub_id,apply_pack,business_pack,final_pack
5.tbl_salary去掉volume_id;
6.tbl_budget增加project_name,labor_id,labor_name,labor_sub_id,labor_sub_name;
7.tbl_budget_import增加字段 is_group



v1.0.2.04（未上线，2018-05-10，王志浩）
---------------------------------------
1.增加部门和职务关联模块；
2.user表增加字段，并修改相关详情页面；
3.选择部门，联动职务下拉框。

v1.0.2.03（已上线，2018-05-08，董岩）
---------------------------------------
1.增加管理人员工资表录入和审核流程；
2.修复工程量清单编辑时提示时间段不可重叠的问题；
3.完善预算删除功能。
4.预算导入时，编号是纯数字时也会视为是部位。

