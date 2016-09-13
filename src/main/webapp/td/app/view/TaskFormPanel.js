/**
 * Created by dux.fangl on 8/12/2016.
 */
Ext.define('td.view.TaskFormPanel', {
	extend: 'Ext.form.Panel',
	requires: [
       'Ext.form.FieldSet',
       'Ext.field.Number',
       'Ext.field.Spinner',
       'Ext.field.Password',
       'Ext.field.Email',
       'Ext.field.Url',
       'Ext.field.DatePicker',
       'Ext.field.Select',
       'Ext.field.Hidden',
       'Ext.field.Radio',
       'td.model.ProjectModel',
       'td.view.fieldTime'
   ],
   xtype: 'taskFormPanel',
   config:{
	   model:'td.model.TaskModel',
	   items: [
	       new Ext.Toolbar({
        		docked:'bottom',
				scrollable:true,
        		items:[{
        			ui:'decline',
        			id:'deleteTaskButton',
        			text:'删除',
        			handler:function(){
        				Ext.Msg.confirm("删除警告！", "本操作将删除任务，确定?", function(buttonId,value,opt){
							if(buttonId=='yes'){
								var form=Ext.getCmp('taskForm');
								
								if(form.getRecord()!=null){
									var projectId=form.getRecord().get('projectId');
									var taskId=form.getRecord().get('taskId');
									form.submit({
										url:'../view?command=removeTask&projectId='+projectId+'&taskId='+taskId,
										method:'POST',
										timeout:180000,
										waitMsg:'请等候提交删除...',
										success:function(form,result,data){
											Ext.Msg.alert('删除', '删除成功', function(){
												var tlp=Ext.getCmp('taskListPanel');
												tlp.setMasked({
												   xtype: 'loadmask',
												   message: '等候加载刷新任务..'
												});
												tlp.getStore().load({params:{'command':'listTasks','projectId':tlp.projectId},callback:function(){
													tlp.unmask();
												}});
												
											});
										},
										failure:function(form,result){
											Ext.Msg.alert('删除', '删除失败', Ext.emptyFn);
										}
									});
								}
							}
        				});
        				
        			}
        		},
				//{
        		//	ui:'decline',
        		//	id:'cloneTaskButton',
        		//	text:'复制',
        		//	handler:function(){
        		//		Ext.Msg.confirm("复制警告！", "本操作将复制本任务到一个新的任务，确定?", function(buttonId,value,opt){
				//			if(buttonId=='yes'){
				//				var form=Ext.getCmp('taskForm');
				//				var taskId=form.getRecord().get('taskId');
				//				
				//				Ext.getCmp('deleteTaskButton').hide(true);
				//				Ext.getCmp('cloneTaskButton').hide(true);
				//											
				//				form.parentTaskId=taskId;
				//			}
        		//		});
        		//		
        		//	}
        		//},
				{
                    xtype:'spacer'
				},{
        			ui:'save',
        			text:'保存',
        			handler:function(){
        				var form=Ext.getCmp('taskForm');
						var projectId=form.getRecord().get('projectId');
        				if(form.parentTaskId!=null){
        					form.submit({
            					url:'../view?command=cloneTask&projectId='+form.projectId+'&parentTaskId='+form.parentTaskId,
            					method:'POST',
            					timeout:180000,
            					waitMsg:'请等候提交保存...',
            					success:function(form,result,data){
            						Ext.Msg.alert('复制', '复制成功', function(){
            							form.parentTaskId=null;
										
            							Ext.getCmp('taskListPanel').getStore().load({params:{'command':'listTasks','projectId':projectId}});
            							//Ext.getCmp('mainPanel').setActiveItem(0);
            						});
            					},
            					failure:function(form,result){
            						Ext.Msg.alert('复制', '复制失败', function(){
            							form.parentProjectId=null;
            							//Ext.getCmp('mainPanel').setActiveItem(0);
            						});
            					}
            				});
        				}else{
	        				form.submit({
	        					url:'../view?command=saveTask',
	        					method:'POST',
	        					timeout:180000,
	        					waitMsg:'请等候提交保存...',
	        					success:function(form,result,data){
									var tlp=Ext.getCmp('taskListPanel');
									tlp.setMasked({
									   xtype: 'loadmask',
									   message: '等候加载刷新任务..'
									});
									tlp.getStore().load({params:{'command':'listTasks','projectId':tlp.projectId},callback:function(){
										tlp.unmask();
									}});
	        						Ext.Msg.alert('保存', '保存成功', function(){							
	        						});
	        					},
	        					failure:function(form,result){
	        						Ext.Msg.alert('保存', '保存失败', Ext.emptyFn);
	        					}
	        				});
	        			}
        			}
        		}]
        	}),
        	{
	    	   xtype: 'fieldset',
	    	   title: '任务信息',
               defaults: {
                   labelWidth: '35%'
               },
               items: [
                   {
                	   xtype:'hiddenfield',
                	   name:'taskId'
                   },
				   {
					   xtype:'hiddenfield',
					   name:'taskGroupId'
				   },
				   {
					   xtype:'hiddenfield',
					   name:'projectId'
				   },
                   {
                	   xtype         : 'textfield',
                       name          : 'content',
                       label         : '任务名称',
                       autoCapitalize: true,
                       required      : true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'selectfield',
                       name          : 'creatorId',
					   id            : 'task_form_creatorId',
					   displayField  : 'name',
					   valueField    : 'userId',
                       label         : '创建人',
					   store		 : {xtype:'userListStore'},
                       autoCapitalize: true,
                       required      : true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'selectfield',
                       name          : 'executorId',
					   displayField  : 'name',
					   valueField    : 'userId',
					   id            : 'task_form_executorId',
                       label         : '执行人',
					   store		 : {xtype:'userListStore'},
                       autoCapitalize: true,
                       clearIcon     : true
                   },
				   {
                	   xtype		 : 'selectfield',
					   name          : 'priority',
                       label		 : '优先级',
					   required      : true,
                       options		 : [
							{text:'重要且紧急',value:'1'},
							{text:'紧急',value:'2'},
							{text:'重要',value:'3'},
							{text:'一般',value:'4'},
							{text:'暂缓',value:'5'},
							{text:'停止',value:'6'}
                       ]
                   },
                   {
                	   xtype         : 'uxFieldTime',
                       name          : 'dueDate',
                       label         : '计划完成时间',
                       dateFormat    : 'Y-m-d',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'uxFieldTime',
                       name          : 'startDate',
                       label         : '实际开始时间',
                       dateFormat    : 'Y-m-d',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'uxFieldTime',
                       name          : 'endDate',
                       label         : '实际完成时间',
                       dateFormat    : 'Y-m-d',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'checkboxfield',
                       name          : 'done',
                       label         : '是否完成',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'textareafield',
                       name          : 'note',
                       label         : '备注',
                       maxRows		 : 4,
                       autoCapitalize: true,
                       clearIcon     : true
                   }
               ]
        	}
	   ]
   }
});