/**
 * Created by dux.fangl on 8/12/2016.
 */
Ext.define('td.view.UserListPanel', {
   extend: 'Ext.List',
   xtype: 'userListPanel',
   config:{
	   model:'td.model.UserModel',
	   store:{xtype:'userListStore'},
	   items: [
	       new Ext.Toolbar({
        		docked:'bottom',
				scrollable:true,
        		items:[
					{
						ui:'back',
						text:'返回',
						handler:function(){
							Ext.getCmp('mainPanel').setActiveItem(3);
						}
					},{
                      xtype:'searchfield',
					  id:'searchUserbutton',
                      name:'搜索',
					  placeHolder:'请输入员工名称',
					  listeners:{
						  change:function(){
							  var store=Ext.getCmp('userListPanel').getStore();
							  	store.clearFilter();
								var sv=Ext.getCmp("searchUserbutton").getValue();
								var re = new RegExp(".*" + sv + ".*");
								if(sv!=""){
									store.filter(
										Ext.create('Ext.util.Filter', {property: "name", value: re })
									);
								}
						  }
					  }
                  }
				]
        	})
		],
		itemTpl:new Ext.XTemplate(
			'<tpl for=".">',
				'<div class="container">',
					'<div>【{title}】{name}</div>',
					'<div>邮件：{email}<br>电话：{phone}</div>',
			'</tpl>'
		),
		listeners: {
			itemtap:function(list, index, target, record, e, eOpts ){
				if(list.selectModel==true){
					Ext.Ajax.request({ 
						url : '../view?command=addMember', 
						method : 'post', 
						timeout: 300000,
						params : { 
							'projectId' :list.projectId,
							'userId':record.get('userId')
						}, 
						success : function(response, options) {
							Ext.getCmp('memberListPanel').getStore().load({params:{'projectId':list.projectId},callback:function(){
								Ext.getCmp('mainPanel').setActiveItem(3);
							}});
							
						}, 
						failure : function() { 
							Ext.getCmp('mainPanel').setActiveItem(3);
						}
					});
				}else{
					
				}
			}
		}
   }
});