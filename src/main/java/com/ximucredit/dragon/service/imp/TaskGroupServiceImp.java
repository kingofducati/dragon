package com.ximucredit.dragon.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ximucredit.dragon.DO.TaskGroupDO;
import com.ximucredit.dragon.dao.TaskGroupMapper;
import com.ximucredit.dragon.service.TaskGroupService;

@Service("taskGroupService")
public class TaskGroupServiceImp implements TaskGroupService {
	@Autowired
	private TaskGroupMapper taskGroupMapper;
	
	public Map<String, List<TaskGroupDO>> loadTaskGroupTree(List<String> types) {
		if(types!=null&&types.size()>0){
			Map<String, List<TaskGroupDO>> map=new HashMap<String, List<TaskGroupDO>>();
			for(String type:types){
				List<TaskGroupDO> list=getRoot(type);
				if(list!=null){
					loadTree(list,null);
					
					map.put(type, list);
				}
			}
			
			return map;
		}
		return null;
	}

	public List<TaskGroupDO> getRoot(String type) {
		List<TaskGroupDO> list=taskGroupMapper.findRootByType(type);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	public void getTree(TaskGroupDO root) {
		List<TaskGroupDO> list=taskGroupMapper.selectTaskGroupsByParent(root.getTaskGroupId());
		if(list!=null&&list.size()>0){
			root.setChilden(list);
			
			loadTree(list,root);
		}
	}
	
	private void loadTree(List<TaskGroupDO> list,TaskGroupDO parent) {
		for(TaskGroupDO taskGroupDO:list){
			taskGroupDO.setParentTaskGroupDO(parent);
			
			List<TaskGroupDO> children=taskGroupMapper.selectTaskGroupsByParent(taskGroupDO.getTaskGroupId());
			if(children!=null){
				taskGroupDO.setChilden(children);
				
				loadTree(children,taskGroupDO);
			}
		}
	}

	public void deleteTaskGroup(String taskGroupId) {
		taskGroupMapper.deleteTaskGroupById(taskGroupId);
	}

	public void deleteGroupTree(String type) {
		taskGroupMapper.deleteTaskGroupsByType(type);
	}

	public void insertTaskGroup(TaskGroupDO taskGroupDO) {
		taskGroupMapper.insert(taskGroupDO);
	}

	public void updateTaskGroup(TaskGroupDO taskGroupDO) {
		taskGroupMapper.updateByPrimaryKey(taskGroupDO);
	}

}
