/**
 * Created by dux.fangl on 8/11/2016.
 */
Ext.define('td.view.BugList', {
	extend:'Ext.List',
	//mainPanel:<-set 
	requires: [
        'td.store.BugListStore'
    ],
	xtype:'buglist',
	config:{
		items:[new Ext.Toolbar({
        		docked:'bottom',
				scrollable:true,
        		items:[{
        			ui:'back',
        			text:'返回',
        			handler:function(){
        				Ext.getCmp('mainPanel').setActiveItem(1);
        			}
        		},{
                    xtype:'spacer'
				},{
					text: '增加问题',
					handler: function() {
						var form=Ext.getCmp('bugForm');
						var mainPanel=Ext.getCmp('mainPanel');
						var bugList=Ext.getCmp('bugListPanel');
						
						form.reset();
						var record=Ext.create('td.model.BugListModel',{
							projectId:bugList.projectId,
							priority:'4',
							state:'2'							
						});
						form.setRecord(record);
						form.projectId=bugList.projectId;
						
						Ext.getCmp('deleteBugButton').hide(false);
						Ext.getCmp('chatButton').hide(false);
						

						mainPanel.setActiveItem(5);
					}
				}]
        	})
		],
		store:{xtype:'bugStore'},
		itemTpl:new Ext.XTemplate(
			'<tpl for=".">',
				'<div class="container">',
					'<div>{bug}【{creatorName}】</div>',
					'<div class="x-progress" >',
						'<div class="x-progress-bar" style="width: {proccess};background-color: {color};"></div>',
						'{proccess}',
					'</div>',
					'<div>终止时间：{dueDate}<br>归属人：{ownerName}<br>优先级：{priority}<br>状态：{state}</div>',
				'</div>',
			'</tpl>'
		),
		listeners: {
			itemtap:function(list, index, target, record, e, eOpts ){
				var form=Ext.getCmp('bugForm');
				form.setRecord(record);
				form.projectId=list.projectId;
				Ext.getCmp('deleteBugButton').show();
				Ext.getCmp('chatButton').show();
				Ext.getCmp('mainPanel').setActiveItem(5);
			}
		}
	}
});