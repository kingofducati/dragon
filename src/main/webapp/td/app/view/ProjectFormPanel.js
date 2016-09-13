/**
 * Created by dux.fangl on 8/12/2016.
 */
Ext.define('td.view.ProjectFormPanel', {
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
   xtype: 'projectFormPanel',
   config:{
	   model:'td.model.ProjectModel',
	   items: [
	       new Ext.Toolbar({
        		docked:'bottom',
				scrollable:true,
        		items:[{
        			ui:'back',
        			text:'返回',
        			handler:function(){
        				Ext.getCmp('mainPanel').setActiveItem(0);
        			}
        		},{
        			ui:'decline',
        			id:'deleteProjectButton',
        			text:'删除',
        			handler:function(){
        				Ext.Msg.confirm("删除警告！", "本操作将删除项目，确定?", function(buttonId,value,opt){
							if(buttonId=='yes'){
								var form=Ext.getCmp('projectForm');
								var projectId=form.getRecord().get('projectId');
								form.submit({
									url:'../view?command=deleteProject&projectId='+projectId,
									method:'POST',
									timeout:180000,
									waitMsg:'请等候提交删除...',
									success:function(form,result,data){
										Ext.Msg.alert('删除', '删除成功', function(){
											Ext.getCmp('projectListPanel').getStore().load({params:{'command':'tdprojectlist'}});
											Ext.getCmp('mainPanel').setActiveItem(0);
										});
									},
									failure:function(form,result){
										Ext.Msg.alert('删除', '删除失败', Ext.emptyFn);
									}
								});
							}
        				});
        				
        			}
        		},{
        			ui:'decline',
        			id:'cloneProjectButton',
        			text:'复制',
        			handler:function(){
        				Ext.Msg.confirm("复制警告！", "本操作将复制本项目到一个新的项目，确定?", function(buttonId,value,opt){
							if(buttonId=='yes'){
								var form=Ext.getCmp('projectForm');
								var projectId=form.getRecord().get('projectId');
								
								Ext.getCmp('deleteProjectButton').hide(false);
								Ext.getCmp('cloneProjectButton').hide(false);
								Ext.getCmp('listBugsButton').hide(false);
								Ext.getCmp('listMembersButton').hide(false);
								Ext.getCmp('listTasksButton').hide(false);
								
								form.parentProjectId=projectId;
							}
        					
        				});
        				
        			}
        		},{
                    xtype:'spacer'
				},{
        			ui:'save',
        			text:'保存',
        			handler:function(){
        				var form=Ext.getCmp('projectForm');
        				if(form.parentProjectId!=null){
        					form.submit({
            					url:'../view?command=cloneProject&parentProjectId='+form.parentProjectId,
            					method:'POST',
            					timeout:180000,
            					waitMsg:'请等候提交保存...',
            					success:function(form,result,data){
            						Ext.Msg.alert('复制', '复制成功', function(){
            							form.parentProjectId=null;
            							Ext.getCmp('projectListPanel').getStore().load({params:{'command':'tdprojectlist'}});
            							Ext.getCmp('mainPanel').setActiveItem(0);
            						});
            					},
            					failure:function(form,result){
            						Ext.Msg.alert('复制', '复制失败', function(){
            							form.parentProjectId=null;
            							Ext.getCmp('mainPanel').setActiveItem(0);
            						});
            					}
            				});
        				}else{
	        				form.submit({
	        					url:'../view?command=saveProject',
	        					method:'POST',
	        					timeout:180000,
	        					waitMsg:'请等候提交保存...',
	        					success:function(form,result,data){
	        						Ext.Msg.alert('保存', '保存成功', function(){
	        							Ext.getCmp('projectListPanel').getStore().load({params:{'command':'tdprojectlist'}});
            							Ext.getCmp('mainPanel').setActiveItem(0);
	        						});
	        					},
	        					failure:function(form,result){
	        						Ext.Msg.alert('保存', '保存失败', Ext.emptyFn);
	        					}
	        				});
	        			}
        			}
        		},{
        			ui:'load',
        			id:'listBugsButton',
        			text:'查看问题清单',
        			handler:function(){
						if(this.pf==null){
							this.pf=Ext.getCmp('projectForm');
						}
						if(this.bugListPanel==null){
							this.bugListPanel=Ext.getCmp('bugListPanel');
						}
						var ppf=this.pf;
						var tlp=this.bugListPanel;
        				var projectId=ppf.getRecord().get('projectId');
						
						
						ppf.setMasked({
						   xtype: 'loadmask',
						   message: '等候加载问题..'
						});
						
        				var projectId=Ext.getCmp('projectForm').getRecord().get('projectId');
        				Ext.getCmp('bugListPanel').getStore().load({params:{'projectId':projectId},callback:function(r, options, success){
							ppf.unmask();
							
							if(success==true){
								tlp.projectId=projectId;
								Ext.getCmp('bug_form_creatorId').getStore().load({params:{'projectId':projectId}});
								Ext.getCmp('bug_form_executorId').getStore().load({params:{'projectId':projectId}});
								
								Ext.getCmp('mainPanel').setActiveItem(2);
							}else{
								 Ext.Msg.alert("ERROR","装载问题清单数据时出错");
							}
						}});
        				
        			}
        		},{
        			ui:'user',
        			id:'listMembersButton',
        			text:'查看成员列表',
        			handler:function(){
        				var projectId=Ext.getCmp('projectForm').getRecord().get('projectId');
						Ext.getCmp('memberListPanel').projectId=projectId;
        				Ext.getCmp('memberListPanel').getStore().load({params:{'projectId':projectId}});
        				Ext.getCmp('mainPanel').setActiveItem(3);
        			}
        		},{
        			ui:'file',
        			id:'listTasksButton',
        			text:'查看任务列表',
        			handler:function(){
						if(this.pf==null){
							this.pf=Ext.getCmp('projectForm');
						}
						if(this.taskListPanel==null){
							this.taskListPanel=Ext.getCmp('taskListPanel');
						}
						var ppf=this.pf;
						var tlp=this.taskListPanel;
        				var projectId=ppf.getRecord().get('projectId');
						
						if(tlp.projectId==null||tlp.projectId!=projectId){
							tlp.getStore().removeAll();
							tlp.projectId=projectId;
							
							ppf.setMasked({
							   xtype: 'loadmask',
							   message: '等候加载任务..'
							});
							
							tlp.getStore().projectId=projectId;
							tlp.getStore().load({callback:function(r, options, success){
								ppf.unmask();
								
								if(success==true){
									Ext.getCmp('task_form_creatorId').getStore().load({params:{'projectId':projectId}});
									Ext.getCmp('task_form_executorId').getStore().load({params:{'projectId':projectId}});
								}else{
									 Ext.Msg.alert("ERROR","装载任务树数据时出错");
								}
							}});
							
							tlp.goToNode(tlp.getStore().getRoot());
						}
						
						Ext.getCmp('mainPanel').setActiveItem(tlp);
        			}
        		}]
        	}),
        	{
	    	   xtype: 'fieldset',
	    	   title: '项目信息',
               defaults: {
                   labelWidth: '35%'
               },
               items: [
                   {
                	   xtype:'hiddenfield',
                	   name:'projectId'
                   },
                   {
                	   xtype         : 'textfield',
                       name          : 'projectName',
                       label         : '项目名称',
                       autoCapitalize: true,
                       required      : true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'textfield',
                       name          : 'projectCode',
                       label         : '项目代号',
                       autoCapitalize: true,
                       required      : true,
                       clearIcon     : true
                   },
                   {
                	   xtype		 : 'selectfield',
					   name          : 'projectType',
                       label		 : '项目类型',
					   required      : true,
                       options		 : [
                        {text: '场景接入',  value: 'dev_type'},
                        {text: '资金接入', value: 'fin_type'},
                        {text: '线上运营',  value: 'run_type'}
                       ]
                   },
                   {
                	   xtype         : 'textfield',
                       name          : 'projectZijin',
                       label         : '资金方',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'textfield',
                       name          : 'projectOwnerId',
                       label         : '负责人',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'textfield',
                       name          : 'nowTaskId',
                       label         : '当前阶段',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'uxFieldTime',
//                	   _picker        : {
//                		   dayText:'日',
//                		   monthText:'月',
//                		   yearText:'年',
//                		   slotOrder:['年','月','日']
//                	   },
                       name          : 'deployTime',
                       label         : '计划上线时间',
                       dateFormat    : 'Y-m-d',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'uxFieldTime',
                       name          : 'startTime',
                       label         : '开始时间',
                       dateFormat    : 'Y-m-d',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'uxFieldTime',
                       name          : 'endTime',
                       label         : '完成时间',
                       dateFormat    : 'Y-m-d',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'checkboxfield',
                       name          : 'finished',
                       label         : '是否完成',
                       autoCapitalize: true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'textareafield',
                       name          : 'remark',
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