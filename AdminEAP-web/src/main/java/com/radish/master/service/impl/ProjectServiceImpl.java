/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.FileUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.MeasureVolume;
import com.radish.master.entity.Project;
import com.radish.master.entity.ProjectFileItem;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.project.ConstructionPlan;
import com.radish.master.pojo.Options;
import com.radish.master.service.ProjectService;
import com.radish.master.system.FileHelper;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年11月2日    Create this file
 * </pre>
 * 
 */
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl implements ProjectService {

    @Resource
    private RuntimePageService runtimePageService;

    private static Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    public File createFile(MultipartFile file) {

        String dirPath = PropertiesUtil.getValue("projectFilePath");
        return createFile(file, dirPath);
    }

    public File createFile(MultipartFile file, String dirPath) {

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String filePath = dirPath + "/" + (new Date().getTime()) + "_" + file.getOriginalFilename();
        File newFile = new File(filePath);
        try {
            InputStream ins = file.getInputStream();
            OutputStream os = new FileOutputStream(newFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }

    @Override
    public byte[] getImage(String path, String name) {
        try {

            // 调接口写读文件
            return FileHelper.showImageFile(name, path);
        } catch (Exception e) {
            logger.error("", e);
        }

        return new byte[0];
    }

    @Override
    public Result getManagerName(String id) {
        String name = this.redisDao.get("manager:" + id);
        if (StrUtil.isEmpty(name)) {
            User user = this.get(User.class, id);
            redisDao.save("org:" + id, user.getName());
            return new Result(true, user.getName(), "获取成功");
        } else {
            return new Result(true, name, "获取成功");
        }
    }

    @Override
    public String getFileField(String batchNo) {
        String result = "";

        if (StrUtil.isEmpty(batchNo)) {
            return result;
        }

        String hql = "from ProjectFileItem where batchNo=:batchNo";
        Map<String, Object> params = new HashMap<>();
        params.put("batchNo", batchNo);
        List<ProjectFileItem> itemList = this.find(hql, params);
        if (itemList.isEmpty()) {
            result = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < itemList.size(); i++) {
                sb.append(",");
                sb.append(itemList.get(i).getId());
            }
            result = sb.toString().substring(1);
        }
        return result;
    }

    @Override
    public Result deleteFileItem(String projectID, String fileField, String key) {
        ProjectFileItem item = this.get(ProjectFileItem.class, key);
        String batchNo = item.getBatchNo();
        FileUtil.delFile(item.getFilePath());
        this.delete(item);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("batchNo", batchNo);
        List<ProjectFileItem> list = this.find("from ProjectFileItem where batchNo = :batchNo", params);

        if (list.isEmpty()) {
            Project project = this.get(Project.class, projectID);
            String methodStr = "set" + fileField.toUpperCase().substring(0, 1) + fileField.substring(1);
            Method method;
            try {
                method = Project.class.getMethod(methodStr, new Class[] { String.class });
                method.invoke(project, new Object[] { null });
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return new Result(false, e.getMessage());
            }
            this.update(project);
        }

        return new Result(true);
    }

    @Override
    public List<Options> getLaborComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, contract_name data from tbl_labor where project_id=? AND Status='30'", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getLaborSubComboboxByLabor(String laborID) {
        return this.findMapBySql("select id value, sub_name data from tbl_labor_sub where labor_id=?", new Object[] { laborID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getProjectSubCombobox(String projectID) {
        return this.findMapBySql("select id value, sub_name data from tbl_project_sub where project_id=? ", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getPackCombobox(String projectID, String teamID) {
        return this.findMapBySql(
                "SELECT pd.id value, pd.`name` data FROM tbl_package_detail pd,tbl_package p WHERE pd.package_id = p.id AND p.project_id=? AND p.team_id=? AND p.status='30'",
                new Object[] { projectID, teamID }, new Type[] { StringType.INSTANCE, StringType.INSTANCE }, Options.class);
    }

    @Override
    public Result startVolumeFlow(ProjectVolume projectVolume, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        Labor labor = this.get(Labor.class, projectVolume.getLaborID());
        projectVolume.setStatus("20");

        this.update(projectVolume);

        String name = "项目：" + projectVolume.getProjectName() + "合同：" + labor.getContractName() + "工程量上报";

        // businessKey
        String businessKey = projectVolume.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
        Map<String, Object> suggestionParams = new HashMap<>();
        suggestionParams.put("businessKey", businessKey);
        suggestionParams.put("taskNode", "caozuoyuan");
        ActivitiSuggestion as = this.get(suggestionHql, suggestionParams);

        if (as == null) {
            as = new ActivitiSuggestion();
            as.setCreateDateTime(new Date());
            as.setUpdateDateTime(new Date());
            as.setBusinessKey(businessKey);
            as.setTaskNode("caozuoyuan");
            as.setApprove("true");
        }

        as.setSuggestion("");
        as.setOperator(SecurityUtil.getUser().getName());
        as.setUpdateDateTime(new Date());
        this.save(as);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }
    
    @Override
    public Result startMeasureVolumeFlow(MeasureVolume measureVolume, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        measureVolume.setStatus("20");

        this.update(measureVolume);

        String name = "项目：" + measureVolume.getProjectName() + "总价措施项目费工程量上报";

        // businessKey
        String businessKey = measureVolume.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
        Map<String, Object> suggestionParams = new HashMap<>();
        suggestionParams.put("businessKey", businessKey);
        suggestionParams.put("taskNode", "caozuoyuan");
        ActivitiSuggestion as = this.get(suggestionHql, suggestionParams);

        if (as == null) {
            as = new ActivitiSuggestion();
            as.setCreateDateTime(new Date());
            as.setUpdateDateTime(new Date());
            as.setBusinessKey(businessKey);
            as.setTaskNode("caozuoyuan");
            as.setApprove("true");
        }

        as.setSuggestion("");
        as.setOperator(SecurityUtil.getUser().getName());
        as.setUpdateDateTime(new Date());
        this.save(as);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }

    @Override
    public Result startLaborFlow(Labor labor, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        labor.setStatus("20");

        this.update(labor);

        String name = "项目：" + labor.getProjectName() + "合同：" + labor.getContractName() + "【审核】";

        // businessKey
        String businessKey = labor.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }

    @Override
    public Result startPlanFlow(ConstructionPlan plan, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        plan.setStatus("20");

        this.update(plan);

        String title = "10".equals(plan.getType()) ? "总" : "月度";

        String name = "项目：" + plan.getProjectName() + "栋号：" + plan.getBuilding() + "【施工" + title + "计划进度审核】";

        // businessKey
        String businessKey = plan.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }

    @Override
    public List<Options> getTeamComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, team_name data from tbl_project_team where project_id=? AND status = '10'", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }
    
    @Override
    public List<Options> getPointTeamComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, team_name data from tbl_project_team where project_id=? AND status = '20'", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getTeamManagerComboboxByProject(String projectID) {
        return this.findMapBySql(
                "SELECT u.id value, u.name data FROM tbl_user u, tbl_user_team ut, tbl_project_team pt WHERE u.id = ut.user_id AND ut.team_id = pt.id AND pt.project_id =? AND pt.status = '30'",
                new Object[] { projectID }, new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getTeamMemberNonManagerComboboxByTeam(String teamID) {
        return this.findMapBySql(
                "SELECT u.id value, u.name data FROM tbl_worker u, tbl_user_team ut, tbl_project_team pt WHERE u.id = ut.user_id AND ut.team_id = pt.id AND pt.id =?",
                new Object[] { teamID }, new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getMemberTeamComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, team_name data from tbl_project_team where project_id=?", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getMemberTeamComboboxByProjectAndStatus(String projectID, int status) {
        return this.findMapBySql("select id value, team_name data from tbl_project_team where project_id=? AND Status=?", new Object[] { projectID, status },
                new Type[] { StringType.INSTANCE, IntegerType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getUserCombobox() {
        return this.findMapBySql("select id value, name data from tbl_user where audit_status is not null", new Object[] {}, new Type[] { StringType.INSTANCE },
                Options.class);
    }

    @Override
    public List<Options> getWorkerCombobox() {
        return this.findMapBySql("select id value, name data from tbl_worker", new Object[] {}, new Type[] { }, Options.class);
    }

    @Override
    public Map<String, String> getUserTeamCombobox() {
        List<Options> tempList = this.findMapBySql("select user_id value, team_name data from tbl_user_team order by user_id desc", new Object[] {},
                new Type[] { StringType.INSTANCE }, Options.class);
        Map<String, String> map = new HashMap<String, String>();
        String userID = "";
        String userTeams = null;
        for (Options o : tempList) {
            if (!userID.equals(o.getValue())) {
                if (userTeams != null) {
                    map.put(userID, userTeams);
                }
                userTeams = o.getData();
                userID = o.getValue();
            } else {
                userTeams = userTeams + "," + o.getData();
            }
        }
        if (userTeams != null) {
            map.put(userID, userTeams);
        }
        return map;
    }

    @Override
    public List<ProjectVolume> checkTimePeriod(String projectID, String laborID, String projectSubID, String startTimeStr, String endTimeStr, String volumeID) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_project_volume ");
        buffer.append("WHERE UNIX_TIMESTAMP(start_time) <= UNIX_TIMESTAMP(:endTime) ");
        buffer.append("AND UNIX_TIMESTAMP(end_time) >= UNIX_TIMESTAMP(:startTime) ");
        buffer.append("AND project_id=:projectID AND labor_id=:laborID AND project_sub_id=:projectSubID ");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("endTime", endTimeStr);
        params.put("startTime", startTimeStr);
        params.put("projectID", projectID);
        params.put("laborID", laborID);
        params.put("projectSubID", projectSubID);

        if (!StrUtil.isEmpty(volumeID)) {
            buffer.append(" AND id <> :id");
            params.put("id", volumeID);
        }

        return this.findBySql(buffer.toString(), params, ProjectVolume.class);
    }
    
    @Override
    public List<MeasureVolume> checkMeasureTimePeriod(String projectID, String measureType, String projectSubID, String startTimeStr, String endTimeStr, String volumeID) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_measure_volume ");
        buffer.append("WHERE UNIX_TIMESTAMP(start_time) <= UNIX_TIMESTAMP(:endTime) ");
        buffer.append("AND UNIX_TIMESTAMP(end_time) >= UNIX_TIMESTAMP(:startTime) ");
        buffer.append("AND project_id=:projectID AND project_sub_id=:projectSubID AND measure_type=:measureType ");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("endTime", endTimeStr);
        params.put("startTime", startTimeStr);
        params.put("projectID", projectID);
        params.put("measureType", measureType);
        params.put("projectSubID", projectSubID);

        if (!StrUtil.isEmpty(volumeID)) {
            buffer.append(" AND id <> :id");
            params.put("id", volumeID);
        }

        return this.findBySql(buffer.toString(), params, MeasureVolume.class);
    }

}
