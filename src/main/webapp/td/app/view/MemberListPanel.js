/**
 * Created by dux.fangl on 8/16/2016.
 */
Ext.define('td.view.MemberListPanel', {
	extend:'Ext.List',
	//mainPanel:<-set 
	requires: [
        'td.store.UserListStore'
    ],
	xtype:'memberlist',
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
        			ui:'add',
        			id:'addUserButton',
        			text:'十',
        			handler:function(){
        				var form=Ext.getCmp('userListPanel');
						form.projectId=Ext.getCmp('memberListPanel').projectId;
						form.selectModel=true;
						form.getStore().load({params:{all:true},callback:function(r, options, success){
							Ext.getCmp('mainPanel').setActiveItem(form);
						}});
        			}
        		},{
        			ui:'decline',
        			id:'delUserButton',
        			text:'一',
        			handler:function(){
        				Ext.Msg.confirm("删除警告！", "本操作将从项目中删除成员，确定?", function(buttonId,value,opt){
							if(buttonId=='yes'){
								var form=Ext.getCmp('memberListPanel');
								var projectId=form.projectId;
								var record=form.getRecord();
								
								Ext.Ajax.request({ 
									url : '../view?command=removeMember', 
									method : 'post', 
									timeout: 300000,
									params : { 
										'projectId' :projectId,
										'userId':record.get('userId')
									}, 
									success : function(response, options) {
										form.getStore().load({params:{'projectId':projectId},callback:function(){
											
										}});		
									}, 
									failure : function() { 
										
									}
								});
							}
        					
        				});
        				
        			}
        		}]
        	})
		],
		store:{xtype:'userListStore'},
		itemTpl:new Ext.XTemplate(
			'<tpl for=".">',
				'<div class="container">',
					'<div>【{title}】{name}</div>',
					'<div>邮件：{email}<br>电话：{phone}</div>',
			'</tpl>'
		),
		listeners: {
			itemtap:function(list, index, target, record, e, eOpts ){
				list.setRecord(record);
				//console.log(list.getRecord());
			}
		}
	}
});