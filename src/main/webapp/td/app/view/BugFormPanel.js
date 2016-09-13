/**
 * Created by dux.fangl on 8/12/2016.
 */
Ext.define('td.view.BugFormPanel', {
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
   xtype: 'bugFormPanel',
   config:{
	   model:'td.model.BugListModel',
	   items: [
	       new Ext.Toolbar({
        		docked:'bottom',
				scrollable:true,
        		items:[
					{
						ui:'back',
						text:'返回',
						handler:function(){
							Ext.getCmp('mainPanel').setActiveItem(2);
						}
					},
					{
        			ui:'decline',
        			id:'deleteBugButton',
        			text:'删除',
        			handler:function(){
        				Ext.Msg.confirm("删除警告！", "本操作将删除此问题，确定?", function(buttonId,value,opt){
							if(buttonId=='yes'){
								var form=Ext.getCmp('bugForm');
								
								if(form.getRecord()!=null){
									var projectId=form.getRecord().get('projectId');
									var bugId=form.getRecord().get('bugId');
									form.submit({
										url:'../view?command=removeBug&projectId='+projectId+'&bugId='+bugId,
										method:'POST',
										timeout:180000,
										waitMsg:'请等候提交删除...',
										success:function(form,result,data){
											Ext.Msg.alert('删除', '删除成功', function(){
												Ext.getCmp('bugListPanel').getStore().load(
												{
													params:{'projectId':projectId},
													callback:function(r, options, success){														
														if(success==true){
															Ext.getCmp('bugListPanel').projectId=projectId;
															Ext.getCmp('bug_form_creatorId').getStore().load({params:{'projectId':projectId}});
															Ext.getCmp('bug_form_executorId').getStore().load({params:{'projectId':projectId}});
															
															Ext.getCmp('mainPanel').setActiveItem(2);
														}else{
															 Ext.Msg.alert("ERROR","装载问题清单数据时出错");
														}
													}
												});
												
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
        		}
				,{
					ui:'confirm-round',
					id:'chatButton',
					text:'讨论',
					handler:function(){
						var form=Ext.getCmp('bugForm');
						var projectId=form.getRecord().get('projectId');
						var bugId=form.getRecord().get('bugId');
						
						Ext.Ajax.request({ 
							url : '../view', 
							method : 'post', 
							timeout: 300000,
							params : { 
								'command' :'createChat',
								'id':bugId,
								'projectId':projectId
							}, 
							success : function(response, options) {
								var o = Ext.util.JSON.decode(response.responseText); 
								var chatId=o.chatId;
								
								Ext.Msg.alert('',o.msg); 
							}, 
							failure : function() { 
							} 
						});
					}
				}
				//,{
        		//	ui:'decline',
        		//	id:'cloneBugButton',
        		//	text:'复制',
        		//	handler:function(){
        		//		Ext.Msg.confirm("复制警告！", "本操作将复制本任务到一个新的问题，确定?", function(buttonId,value,opt){
				//			if(buttonId=='yes'){
				//				var form=Ext.getCmp('bugForm');
				//				var bugId=form.getRecord().get('bugId');
				//				
				//				Ext.getCmp('deleteBugButton').hide(true);
				//				Ext.getCmp('cloneBugButton').hide(true);
				//											
				//				form.parentBugId=bugId;
				//			}
        		//		});
        		//		
        		//	}
        		//}
				,{
                    xtype:'spacer'
				},{
        			ui:'save',
        			text:'保存',
        			handler:function(){
        				var form=Ext.getCmp('bugForm');
        				if(form.parentBugId!=null){
        					form.submit({
            					url:'../view?command=cloneBug&projectId='+form.projectId+'&parentBugId='+form.parentBugId,
            					method:'POST',
            					timeout:180000,
            					waitMsg:'请等候提交保存...',
            					success:function(form,result,data){
            						Ext.Msg.alert('复制', '复制成功', function(){
            							form.parentBugId=null;
            							Ext.getCmp('bugListPanel').getStore().load({params:{'command':'listBugs','projectId':form.projectId}});
            							//Ext.getCmp('mainPanel').setActiveItem(0);
            						});
            					},
            					failure:function(form,result){
            						Ext.Msg.alert('复制', '复制失败', function(){
            							form.parentBugId=null;
            							//Ext.getCmp('mainPanel').setActiveItem(0);
            						});
            					}
            				});
        				}else{
	        				form.submit({
	        					url:'../view?command=saveBug',
	        					method:'POST',
	        					timeout:180000,
	        					waitMsg:'请等候提交保存...',
	        					success:function(form,result,data){
	        						Ext.Msg.alert('保存', '保存成功', function(){
	        							Ext.getCmp('bugListPanel').getStore().load(
										{
											params:{'projectId':form.projectId},
											callback:function(r, options, success){														
												if(success==true){
													Ext.getCmp('bugListPanel').projectId=form.projectId;
													Ext.getCmp('bug_form_creatorId').getStore().load({params:{'projectId':form.projectId}});
													Ext.getCmp('bug_form_executorId').getStore().load({params:{'projectId':form.projectId}});
													
													Ext.getCmp('mainPanel').setActiveItem(2);
												}else{
													 Ext.Msg.alert("ERROR","装载问题清单数据时出错");
												}
											}
										});
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
	    	   title: '项目问题',
               defaults: {
                   labelWidth: '35%'
               },
               items: [
                   {
                	   xtype:'hiddenfield',
                	   name:'bugId'
                   },
				   {
					   xtype:'hiddenfield',
					   name:'projectId'
				   },
                   {
                	   xtype         : 'textareafield',
                       name          : 'bug',
                       label         : '问题',
					   maxRows		 : 4,
                       autoCapitalize: true,
                       required      : true,
                       clearIcon     : true
                   },
                   {
                	   xtype         : 'selectfield',
                       name          : 'creator',
					   id            : 'bug_form_creatorId',
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
                       name          : 'owner',
					   displayField  : 'name',
					   valueField    : 'userId',
					   id            : 'bug_form_executorId',
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
					   		{text:'', value:'0'},
							{text:'重要且紧急',value:'1'},
							{text:'紧急',value:'2'},
							{text:'重要',value:'3'},
							{text:'一般',value:'4'},
							{text:'暂缓',value:'5'},
							{text:'停止',value:'6'}
                       ]
                   },
				   {
                	   xtype		 : 'selectfield',
					   name          : 'state',
                       label		 : '状态',
					   required      : true,
                       options		 : [
					   		{text:'',value:'0'},
							{text:'已完成',value:'1'},
							{text:'进行中',value:'2'},
							{text:'暂缓',value:'3'},
							{text:'停止',value:'4'}
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