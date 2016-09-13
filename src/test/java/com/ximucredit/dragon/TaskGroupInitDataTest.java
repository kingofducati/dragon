package com.ximucredit.dragon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.ximucredit.dragon.DO.TaskGroupDO;

public class TaskGroupInitDataTest {

	@Test
	public void testInitData() {
		String[] names={"1. 场景分析评估","2. 产品方案设计","3. 场景数据获取","4. 场景合同签署","5. 数据分析与风险政策产出","6. 产品推介","7. 系统对接","8. 业务对接"};
		
		
		String[][] nnames={
				{"1.1 场景评估","1.2 行业分析"},
				{"2.1 目标客群确定","2.2 产品原型确定","2.3 合作模式及业务流程产出","2.4 行业背景描述","2.5 产品规模收益预测","2.6 风控要点确定","2.7 产品可行性描述","2.8 产品参数确定"},
				{"3.1场景数据获取"},
				{"4.1 商务合同签署"},
				{"5.1 数据分析报告产出","5.2 场景数据宽表产出","5.3 风险管理细则产出","5.4 业务投放规划产出","5.5 资金需求及保证金安排","5.6 首批投放名单产出"},
				{"6.1 项目内审","6.2 资金立项","6.3 资金过会","6.4 资方协议签署","6.5 政策更新"},
				{"7.1 BRD文档产出","7.2 系统开发排期确定","7.3 系统开发"},
				{"8.1 试运营操作手册产出","8.2 试运营客户申请","8.3 试运营客户支用"}
		};
		
		initInsertSQL(names,nnames,"dev_type","target/initDevGroupData.sql");
		
//		String[] names1={"",""};
//		String[][] nnames1={
//				{},
//				{}
//		};
//		
//		initInsertSQL(names1,nnames1,"run_type","target/initFinGroupData.sql");
		
//		String[] names2={"1. 投放","2. 授权转化","3. 授信转化","4. 支用转化","5. 贷后管理","",""};
//		String[][] nnames2={
//				{"1.1 业务规划","1.2 投放名单产出（每月重置状态）","1.2 投放名单产出（每月重置状态）","1.4 投放管理系统化","1.5 客户服务短信上线"},
//				{"2.1 授权转化营销短信","2.2 授权入口推广（首页、APP九宫格、订单界面、微信H5、微信公众号、客户QQ/微信群）","2.3 授权界面和授权信息优化","2.3 授权界面和授权信息优化","2.5 市场活动推广"},
//				{"3.1 核身线上化","3.2 审批和资金推介自动化","3.3 审批时效优化至T＋1","3.4 授信材料和流程优化"},
//				{"4.1 首贷转化市场活动落地 （能接受产品的客户）","4.2 授信额度、定价测试和优化（不能接受产品的客户）","4.3 支用流程优化","4.4 支用转化营销短信","4.5 额度动态调整","4.6 额度规则系统化","4.7 风险定价规则产出及系统化"},
//				{"5.1 分润对账（每月重置）","5.2 展期功能上线","5.3 提前还款功能上线","5.4 分润对账系统化","5.5 手续费对账系统化","5.6 资金方&场景方对接全线上化","5.7 反欺诈系统化","5.8 催收接入"}
//		};
//		
//		initInsertSQL(names2,nnames2,"run_type","target/initRunGroupData.sql");
	}

	private void initInsertSQL(String[] names, String[][] nnames, String type,String fileName) {
		List<TaskGroupDO> roots=initRootData(names,"dev_type");
		StringBuilder sb=new StringBuilder();
		
		initTreeData(nnames,roots);
			
		generateInsertSQL(roots,sb);
		
		FileWriter fw=null;
		try {
			File f=new File(fileName);
			if(f.exists()){
				f.delete();
			}
			f.createNewFile();
			
			fw=new FileWriter(f);
			fw.write(sb.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(fw!=null){
					fw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void generateInsertSQL(List<TaskGroupDO> roots, StringBuilder sb) {
		for(TaskGroupDO root:roots){
			generateInsertSQL(root,sb);
			
			sb.append("\n");
			
			if(root.getChilden()!=null){
				generateInsertSQL(root.getChilden(), sb);
			}
		}
	}

	private void generateInsertSQL(TaskGroupDO node, StringBuilder sb) {
		sb.append("INSERT INTO t_taskgroup ("
				+ "taskgroup_id, "
				+ "taskgroup_name, "
				+ "parent_id, "
				+ "taskgroup_type ) VALUES ('");
		sb.append(node.getTaskGroupId());
		sb.append("','").append(node.getTaskGroupName());
		sb.append("','").append(node.getParentTaskGroupId()!=null?node.getParentTaskGroupId():"");
		sb.append("','").append(node.getTaskGroupType());
		sb.append("');");
	}

	private void initTreeData(String[][] nnames, List<TaskGroupDO> roots) {
		for(int i=0;i<roots.size();i++){
			TaskGroupDO node=roots.get(i);
			String[] names=nnames[i];
			
			initNode(names,node);
		}
	}

	private void initNode(String[] names, TaskGroupDO node) {
		List<TaskGroupDO> list=new ArrayList<TaskGroupDO>();
		for(String name:names){
			TaskGroupDO tg=new TaskGroupDO();
			tg.setTaskGroupId(UUID.randomUUID().toString().replaceAll("-", ""));
			tg.setTaskGroupName(name);
			tg.setTaskGroupType(node.getTaskGroupType());
			tg.setParentTaskGroupDO(node);
			tg.setParentTaskGroupId(node.getTaskGroupId());
			
			list.add(tg);
		}
		
		node.setChilden(list);
	}

	private List<TaskGroupDO> initRootData(String[] names,String type) {
		List<TaskGroupDO> list=new ArrayList<TaskGroupDO>();
		for(String name:names){
			TaskGroupDO tg=new TaskGroupDO();
			tg.setTaskGroupId(UUID.randomUUID().toString().replaceAll("-", ""));
			tg.setTaskGroupName(name);
			tg.setTaskGroupType(type);
			
			list.add(tg);
		}
		return list;
	}

}
