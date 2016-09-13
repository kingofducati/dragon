/**
 * Created by dux.fangl on 8/16/2016.
 */
Ext.define('td.view.TaskListPanel', {
	extend:'Ext.dataview.NestedList',
	requires: [
        'td.store.TaskListStore'
    ],
	xtype:'tasklist',
	// add a / for folder nodes in title/back button
	getTitleTextTpl: function(node) {
		return  '<tpl if="group == true">'+
						'<img src="../td/images/group.png" width="16" height="16" />{name}/'+
				'<tpl else>'+
						'<img src="../td/images/task.png" width="16" height="16" />{name}'+
				'</tpl>';
	},
	// add a / for folder nodes in the list
	getItemTextTpl: function(node) {
		return  '<div class="container">'+
					'<tpl if="group == true">'+
						'<img src="../td/images/group.png" width="16" height="16" />{name}/'+
					'<tpl else>'+
						'<img src="../td/images/task.png" width="16" height="16" />{name}'+
						'<div class="x-progress" >'+
							'<div class="x-progress-bar" style="width: {proccess};background-color: {color};"></div>'+
							'{proccess}'+
						'</div>'+
					'</tpl>'+
				'</div>';
	},
	config:{
		flex: 1,
		backButton:{ui: 'back', hidden: true},
		title:'任务列表',
		items:new Ext.Toolbar({
        		docked:'bottom',
				scrollable:true,
        		items:[{
        			ui:'back',
        			text:'返回项目',
        			handler:function(){
						//Ext.getCmp('taskListPanel').goToNode(Ext.getCmp('taskListPanel').getStore().getRoot());
						//Ext.getCmp('taskListPanel').getStore().removeAll(true);
        				Ext.getCmp('mainPanel').setActiveItem(1);
        			}
        		},{
                    xtype:'spacer'
				},{
					text: '新增任务',
					id:'addTaskButton',
					handler: function() {
						var tlp=Ext.getCmp('taskListPanel');
						var detailCard=tlp.getDetailCard();
						tlp.setActiveItem(detailCard);
						detailCard.parentTaskId=null;
						
						var data=Ext.create('td.model.TaskModel',{
							creatorId:'',
							projectId:tlp.projectId,
							taskGroupId:tlp.getLastNode().get('id')
						});
						detailCard.setRecord(data);
						Ext.getCmp('deleteTaskButton').hide(true);
					}
				}]
        }),
		displayField: 'name',
		detailCard:{xtype:'taskFormPanel',id:'taskForm'},
		store:{xtype:'taskListStore'},
		listeners: {
			leafitemtap: function(me, list, index, target, record, e, eOpts) {
				var detailCard = me.getDetailCard();
				detailCard.load({
					url:'../view?command=queryTask&projectId='+me.projectId+'&taskId='+record.get('id'),
					method:'GET',
					timeout:180000,
					waitMsg:'请稍后，正在装载项目'+record.get('name')+'...',
					success:function(form,result,data){
						var reader=Ext.create('Ext.data.reader.Json',{
							type: 'json',
							model:'td.model.TaskModel',
							rootProperty: 'task'
						});
						var res=reader.read(data);
						if(res){
							detailCard.setRecord(res.getRecords()[0]);
							Ext.getCmp('deleteTaskButton').show(false);
						}
					},
					failure:function(form,result){
						Ext.Msg.alert('readTaskError', 'result='+result, Ext.emptyFn);
					},
					scope:this
				});
				
			}
		}
	}
});