package com.radish.master.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.CodeException;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.ProAccountDetExport;
import com.radish.master.entity.Project;
import com.radish.master.entity.common.UserExport;
import com.radish.master.entity.volumePay.ProjectPay;
import com.radish.master.pojo.Options;
import com.radish.master.system.Arith;
import com.radish.master.system.ReportSXSSF;
import com.radish.master.system.TimeUtil;
import com.radish.master.system.report.Column;

@Controller
@RequestMapping("/proaccount")
public class ProAccountController {
	String prefix = "/proAccount/";
	
	@Autowired
	private BaseService baseService;
	@Resource
	private RuntimePageService runtimePageService;
	@Autowired
	private SessionFactory sessionFactory;
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@RequestMapping("/addList")
	public String addList(HttpServletRequest request){
		List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		request.setAttribute("xm", JSONArray.toJSONString(xm));
		return prefix +"addList";
	}
	@RequestMapping("/addListGs")
	public String addListGs(HttpServletRequest request){
		return prefix +"addListGs";
	}
	
	@RequestMapping("/addAccountIndex")
	public String addAccountIndex(HttpServletRequest request){
		String id = request.getParameter("id");
		if(id==null){
			List xm = baseService.findMapBySql("select id value, project_name data from tbl_project where id not in(select zm.projectId from tbl_projectAccount zm)", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
			request.setAttribute("projectOptions", JSONArray.toJSONString(xm));
		}else{
			List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
			request.setAttribute("projectOptions", JSONArray.toJSONString(xm));
		}
		
		request.setAttribute("zmid", id);
		return prefix +"addAccountIndex";
	}
	
	@RequestMapping("/accountDetList")
	public String accountDetList(HttpServletRequest request){
		String zmid = request.getParameter("id");
		request.setAttribute("zmid", zmid);
		
		ProAccount zm = baseService.get(ProAccount.class, zmid);
		request.setAttribute("zm", JSONArray.toJSONString(zm));
		return prefix +"accountDetList";
	}
	@RequestMapping("/accountDetListGs")
	public String accountDetListGs(HttpServletRequest request){
		String zmid = request.getParameter("id");
		request.setAttribute("zmid", zmid);
		
		ProAccount zm = baseService.get(ProAccount.class, zmid);
		request.setAttribute("zm", JSONArray.toJSONString(zm));
		return prefix +"accountDetListGs";
	}
	@RequestMapping("/addDetIndex")
	public String addDetIndex(HttpServletRequest request){
		String zmid = request.getParameter("zmid");
		String zmmxid = request.getParameter("zmmxid");
		User u = SecurityUtil.getUser();
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u where u.audit_status = 10 ", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		request.setAttribute("zmid", zmid);
		request.setAttribute("zmmxid", zmmxid);
		request.setAttribute("userId", u.getId());
		return prefix +"addDetIndex";
	}
	/**
	 * 复核审核页
	 * @param request
	 * @param zm
	 * @return
	 */
	@RequestMapping("/auidt/{id}")//审核查看页
	public String auidLook(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("zmmxid",id);
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u where u.audit_status = 10 ", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		return prefix+"auidLook";
		
	}
	
	@RequestMapping("/saveAccount")
	@ResponseBody
	public Result saveAccount(HttpServletRequest request,ProAccount zm){
		String id = request.getParameter("id");
		Result r = new Result();
		if(id==null){//新增
			zm.setAllMoney("0");
			baseService.save(zm);
			r.setData(zm);
		}else{//修改
			ProAccount z = baseService.get(ProAccount.class, id);
			z.setAccountName(zm.getAccountName());
			baseService.update(z);
			r.setData(z);
		}
		
		r.setSuccess(true);
		return r;
		
	}
	@RequestMapping("/getAccount")
	@ResponseBody
	public Result getAccount(HttpServletRequest request){
		String id = request.getParameter("id");
		ProAccount zm = baseService.get(ProAccount.class, id);
		Result r = new Result();
		r.setData(zm);
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/saveAccountDet")
	@ResponseBody
	public Result saveAccountDet(HttpServletRequest request,ProAccountDet mx){
		Arith arith = new Arith();
		String mxid = request.getParameter("id");
		String money = request.getParameter("money");
		Result r = new Result();
		ProAccount zm = baseService.get(ProAccount.class, mx.getProjectAccountId());
		
		if(mxid==null){//新增
			if("1".equals(mx.getZmtype())){//收入
				mx.setInMoney(money);
				zm.setAllMoney(arith.add(Double.valueOf(money), Double.valueOf(zm.getAllMoney()))+"");
			}else if("2".equals(mx.getZmtype())){//支出
				mx.setOutMoney(money);
				zm.setAllMoney(arith.sub(Double.valueOf(zm.getAllMoney()), Double.valueOf(money))+"");
			}
			//User sh = baseService.get(User.class, mx.getAuditId());
			User jz = baseService.get(User.class, mx.getAccounterId());
			//mx.setAuditName(sh.getName());
			mx.setAccounter(jz.getName());
			mx.setStatus("10");
			mx.setCreateId(SecurityUtil.getUserId());
			baseService.save(mx);
			r.setData(mx);
		}else{//编辑
			ProAccountDet m = baseService.get(ProAccountDet.class, mxid);
			//先修改账本总额
			if("1".equals(m.getZmtype())){
				
				zm.setAllMoney(arith.sub(Double.valueOf(zm.getAllMoney()), Double.valueOf(m.getInMoney()))+"");
				m.setInMoney("");
			}else if("2".equals(m.getZmtype())){
				zm.setAllMoney(arith.add(Double.valueOf(m.getOutMoney()), Double.valueOf(zm.getAllMoney()))+"");
				m.setOutMoney("");
			}
			
			if("1".equals(mx.getZmtype())){//收入
				m.setInMoney(money);
				zm.setAllMoney(arith.add(Double.valueOf(money), Double.valueOf(zm.getAllMoney()))+"");
			}else if("2".equals(mx.getZmtype())){//支出
				m.setOutMoney(money);
				zm.setAllMoney(arith.sub(Double.valueOf(zm.getAllMoney()), Double.valueOf(money))+"");
			}
			
			//User sh = baseService.get(User.class, mx.getAuditId());
			User jz = baseService.get(User.class, mx.getAccounterId());
			//m.setAccounterId(mx.getAccounterId());
			//m.setAuditName(sh.getName());
			m.setAccounter(jz.getName());
			m.setZmtype(mx.getZmtype());
			m.setCreateDate(mx.getCreateDate());
			m.setAbstracts(mx.getAbstracts());
			m.setRemark(mx.getRemark());
			
			baseService.update(m);
			r.setData(m);
		}
		r.setSuccess(true);
		return r;
	}
	
	@RequestMapping("/getAccountDet")
	@ResponseBody
	public Result getAccountDet(HttpServletRequest request){
		String mxid = request.getParameter("id");
		ProAccountDet mx  = baseService.get(ProAccountDet.class, mxid);
		ProAccount zm = baseService.get(ProAccount.class, mx.getProjectAccountId());
		Result r = new Result();
		r.setData(mx);
		r.setSuccess(true);
		r.setCode(zm.getAccountName());
		return r;
	}
	@RequestMapping("/delAccountDet")
	@ResponseBody
	public Result delAccountDet(HttpServletRequest request){
		Arith arith = new Arith();
		String id =request.getParameter("id");
		ProAccountDet m = baseService.get(ProAccountDet.class, id);
		ProAccount zm = baseService.get(ProAccount.class, m.getProjectAccountId());
		if("1".equals(m.getZmtype())){
			
			zm.setAllMoney(arith.sub(Double.valueOf(zm.getAllMoney()), Double.valueOf(m.getInMoney()))+"");
			
		}else if("2".equals(m.getZmtype())){
			zm.setAllMoney(arith.add(Double.valueOf(m.getOutMoney()), Double.valueOf(zm.getAllMoney()))+"");
			
		}
		
		baseService.delete(m);
		baseService.update(zm);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	
	@RequestMapping("/doAudit")
	@ResponseBody
	public Result start(String id) {
		ProAccountDet m = baseService.get(ProAccountDet.class, id);
		ProAccount zm = baseService.get(ProAccount.class, m.getProjectAccountId());
		
		m.setStatus("20");
		User user = SecurityUtil.getUser();
		baseService.update(m);
		

        String name =user.getName()+"提交账目复核";

        // businessKey
        String businessKey = m.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("ProAccountCheck", name, variables, user.getId(), businessKey);
    }
	
	  @RequestMapping(value = "/getexcel", method = RequestMethod.GET)
	    @ResponseBody
	    public String exportExcel(ProAccountDet det, HttpServletRequest request, HttpServletResponse response) {
	        try {
	        	String sj1 = request.getParameter("sj1");
	        	String sj2 = request.getParameter("sj2");
	            ArrayList<String[]> rows = new ArrayList<>();
	            System.out.println(det.toString());
	            String[] header = { "序号", "账目日期", "摘要", "进账", "出账", "备注", "状态"};
	            rows.add(header);
	            getExcel(det, rows,sj1,sj2);
	            Column[] columns = getColumn();
	            ReportSXSSF r = new ReportSXSSF("账目明细", "", columns, rows);

	            response.setContentType("application/octet-stream;charset=utf-8");
	            response.setHeader("Content-Disposition", "attachment; filename=\"account_ " + TimeUtil.getCurrentDate() + ".xls\"");
	            OutputStream output = response.getOutputStream();
	            r.export(output);

	        } catch (Exception e) {
	            return null;
	        }
	        return null;
	    }
	  private ArrayList<String[]> getExcel(ProAccountDet det, ArrayList<String[]> rows,String sj1,String sj2) throws CodeException {
	        StringBuilder buffer = new StringBuilder();
	        
	        buffer.append("select a.id id, a.createDate,a.abstracts,a.inMoney,a.outMoney,a.remark, ");
	        buffer.append("CASE WHEN a.status=\'10\' THEN \'新建\' WHEN \'20\' THEN \'复核确认\' WHEN \'30\' THEN \'完成\' ELSE \'复核驳回\' END as status ");
	        buffer.append("from tbl_projectaccount_det a  ");
	        buffer.append("WHERE 1=1 ");
	        buffer.append("and a.createDate BETWEEN '"+sj1+"' and '"+sj2+"' ");
	        
	        
	        Map<String, Object> params = new HashMap<String, Object>();

	        if (!StrUtil.isEmpty(det.getProjectAccountId())) {
	            buffer.append(" AND a.projectAccountId= '"+det.getProjectAccountId()+"' ");
	        }

	        if (!StrUtil.isEmpty(det.getAbstracts())) {
	            buffer.append(" AND a.abstracts LIKE '%"+det.getAbstracts()+"%' ");
	        }
	        buffer.append("order by a.createDate desc ");
	        try {
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            List<ProAccountDetExport> users = baseService.findBySql(buffer.toString(), params, ProAccountDetExport.class);
	            BigDecimal allin = new BigDecimal("0.0");
	            BigDecimal allout = new BigDecimal("0.0");
	            BigDecimal zero = new BigDecimal("0.0");
	            for (int i = 0; i < users.size(); i++) {
	            	ProAccountDetExport userColumn = users.get(i);

	                String[] cells = new String[7];
	                cells[0] = String.valueOf(i + 1);
	                cells[1] = sdf.format(userColumn.getCreateDate());
	                cells[2] = userColumn.getAbstracts();
	                cells[3] = userColumn.getInMoney();
	                cells[4] = userColumn.getOutMoney();
	                cells[5] = userColumn.getRemark();
	                cells[6] = userColumn.getStatus();
	                        
	                rows.add(cells);
	                allin = allin.add(userColumn.getInMoney()==null?zero:new BigDecimal(userColumn.getInMoney()));
	                allout = allout.add(userColumn.getOutMoney()==null?zero:new BigDecimal(userColumn.getOutMoney()));
	            }
	            String[] cells = new String[7];
	            for (int i = 0; i < cells.length; i++) {
	                cells[i] = "";
	            }
	            cells[1] = "总计";
	            cells[3] =allin +"";
	            cells[4] =allout +"";
	            rows.add(cells);
	        } catch (Exception e) {
	            throw new CodeException(e.getMessage());
	        }
	        return rows;
	    }

	    private Column[] getColumn() {
	        Column[] columns = new Column[7];

	        columns[0] = new Column();
	        columns[0].setAlign(Column.ALIGN_CENTER);
	        columns[0].setWidth(2000);

	        columns[1] = new Column();
	        columns[1].setAlign(Column.ALIGN_CENTER);
	        columns[1].setWidth(4000);

	        columns[2] = new Column();
	        columns[2].setAlign(Column.ALIGN_CENTER);
	        columns[2].setWidth(6000);

	        columns[3] = new Column();
	        columns[3].setAlign(Column.ALIGN_CENTER);
	        columns[3].setWidth(4000);

	        columns[4] = new Column();
	        columns[4].setAlign(Column.ALIGN_CENTER);
	        columns[4].setWidth(4000);

	        columns[5] = new Column();
	        columns[5].setAlign(Column.ALIGN_CENTER);
	        columns[5].setWidth(6000);

	        columns[6] = new Column();
	        columns[6].setAlign(Column.ALIGN_CENTER);
	        columns[6].setWidth(3000);

	        return columns;
	    }
}
