/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when it performs code generation tasks such as generating new
    models, controllers or views and when running "sencha app upgrade".

    Ideally changes to this file would be limited and most work would be done
    in other places (such as Controllers). If Sencha Cmd cannot merge your
    changes and its generated code, it will produce a "merge conflict" that you
    will need to resolve manually.
*/

Ext.application({
	
    name: 'td',
    
    requires: [
        'Ext.MessageBox',
        'Ext.picker.Date'
    ],
	stores:[
		'BugListStore',
		'UserListStore',
		'TaskListStore'
	],
	models:[
		'BugListModel',
		'td.model.ProjectModel',
		'UserModel',
		'TaskModel',
		'GroupModel'
	],
    views: [
        'BugList',
        'ProjectFormPanel',
        'MemberListPanel',
		'TaskListPanel',
		'TaskFormPanel',
		'BugFormPanel'
    ],

    icon: {
        '57': 'resources/icons/Icon.png',
        '72': 'resources/icons/Icon~ipad.png',
        '114': 'resources/icons/Icon@2x.png',
        '144': 'resources/icons/Icon~ipad@2x.png'
    },

    isIconPrecomposed: true,

    startupImage: {
        '320x460': 'resources/startup/320x460.jpg',
        '640x920': 'resources/startup/640x920.png',
        '768x1004': 'resources/startup/768x1004.png',
        '748x1024': 'resources/startup/748x1024.png',
        '1536x2008': 'resources/startup/1536x2008.png',
        '1496x2048': 'resources/startup/1496x2048.png'
    },

    launch: function() {
        // Destroy the #appLoadingIndicator element
        Ext.fly('appLoadingIndicator').destroy();
        
        Ext.define('project_td', {
            extend: 'Ext.data.Model',
            fields: ['id','name','bug_number','proccess','color','group', 'url','bugs'],
            idField: 'id'
        });

        // Initialize the main view
//        Ext.Viewport.add(Ext.create('td.view.Main'));
        var store=Ext.create('Ext.data.Store',{
        	groupField: 'group',
            model:'project_td',
            proxy: {
                type: 'ajax',
                url: '../view',
                timeout:180000,
                reader: {
                    type: 'json',
                    rootProperty: 'projects'
                }
            }
        });
        
        var tpl = new Ext.XTemplate(
            '<tpl for=".">',                
	        	'<div id="{id}" class="container">',
		        	'<img src="{url}" width="16" height="16"/>{name}',
		    		'<div class="x-progress" >',
		          		'<div class="x-progress-bar" style="width: {proccess};background-color: {color};"></div>',
		          		'<img src="../td/images/In_Progress.png"/>{proccess}',
		          	'</div>',
		        	'<div>',
		        	'<img src="../td/images/problem.png" width="16" height="16" />问题({bug_number}):',
		        	'<ul>',
		        		'<tpl for="bugs">',
		        		'<li>{#}.{bug}</li>',
		        		'</tpl>',
		        	'</ul></div>',
		        '</div>',
            '</tpl>'
        );
        var addProjectButton = Ext.create('Ext.Button',{
            text: '增加项目',
            handler: function() {
				var form = Ext.getCmp('projectForm');
            	form.reset();
				var record=Ext.create('td.model.ProjectModel',{
					projectType:'dev_type'
				});
				form.setRecord(record);
				
            	Ext.getCmp('deleteProjectButton').hide(false);
            	Ext.getCmp('cloneProjectButton').hide(false);
            	Ext.getCmp('listBugsButton').hide(false);
            	Ext.getCmp('listMembersButton').hide(false);
            	Ext.getCmp('listTasksButton').hide(false);
            	mainPanel.setActiveItem(1);
            }
        });
        
        var menu=Ext.create('Ext.Menu', {
        	id:'deleteMenu',
            items: [
                {
                    text: '删除',
                    iconCls: 'delete'
                }
            ]
        });
        Ext.Viewport.setMenu(menu,{
    		side:'top'
    	});
        
        var toolbar=new Ext.Toolbar({
            docked:'bottom',
			scrollable:{
				direction: 'horizontal',
				directionLock: true
			},
            items:[
                  {
					  text:'导出Excel',
					  handler:function(){
						  Ext.Msg.prompt("导出报表", "本操作导出Excel报表，请输入发送的邮箱：", function(buttonId,value,opt){
							  if(value!=null){
								  var plp=Ext.getCmp('projectListPanel');
								  plp.setMasked({
									   xtype: 'loadmask',
									   message: '等候加载导出Excle..'
								  });
						  
								  Ext.Ajax.request({ 
									url : '../view', 
									method : 'post', 
									timeout: 300000,
									params : { 
										'command' :'exportexcel',
										'email':value,
										'report':'',
										'projectType':'dev_type'
									}, 
									success : function(response, options) {
										var o = Ext.util.JSON.decode(response.responseText); 
										plp.unmask();
										
										Ext.Msg.alert('',o.msg); 
									}, 
									failure : function() { 
									} 
								});
							  }
						  });
					  }
				  },
                  {
                      xtype:'searchfield',
					  id:'searchbutton',
                      name:'搜索',
					  width:120,
					  placeHolder:'请输入项目名称',
					  listeners:{
						  change:function(){
							  	store.clearFilter();
								var sv=Ext.getCmp("searchbutton").getValue();
								var re = new RegExp(".*" + sv + ".*");
								if(sv!=""){
									store.filter(
										Ext.create('Ext.util.Filter', {property: "name", value: re })
									);
								}
						  }
					  }
                  },
				{
					xtype: 'selectfield',          
					id:'sel_sort', 
					width:150,
					name:'排序',  
					displayField:'text',
					valueField:'value',                
					options:[{
						text:'选择排序方式',
						value:''
					},{
						text:'按名称升序排列',
						value:'name-asc'
					},{
						text:'按名称降序排列',
						value:'name-desc'
					},{
						text:'按进度升序排列',
						value:'proccess-asc'
					},{
						text:'按进度降序排列',
						value:'proccess-desc'
					}], 
					listeners:{
						change:function(){
							var sort=Ext.getCmp("sel_sort").getValue();
							if(sort!="")
							{
								store.sort({ 
									property :sort.split('-')[0], 
									direction:sort.split('-')[1],
									transform:function(value){
										return getSpell(value,value);
									}
								});
							}
						}                    
					}
				},{
                      xtype:'spacer'
                  },
                  addProjectButton
            ]
        });
		var titleBar=new Ext.Toolbar({
            docked:'top',
			scrollable:{
				direction: 'horizontal',
				directionLock: true
			},
            items:[{
				xtype:'title',
				title:'项目跟进'
			},{
                      xtype:'spacer'
                  },
                  addProjectButton
			]
		});
		
        var dataview = Ext.create('Ext.List', {
            id:'projectListPanel',
            scrollable:'vertical',
            store: store,
            items:[titleBar,toolbar],
            itemTpl:tpl,
            grouped:true,
//            disclosureProperty:'disclosure',
//            preventSelectionOnDisclose:false,
//            onItemDisclosure:function(record, item, index, e){
//            	Ext.Msg.alert('Disclose', 'Disclose more info for ' + record.get('name'));
//            },
            listeners:{
                itemtap:function(list, index, target, record, e, eOpts ){
					var pf=Ext.getCmp('projectForm');
                	pf.load({
                		url:'../view?command=queryProject&projectId='+record.get('id'),
                		method:'GET',
                		timeout:300,
                		waitMsg:'请稍后，正在装载项目'+record.get('name')+'...',
                		success:function(form,result,data){
                			var reader=Ext.create('Ext.data.reader.Json',{
                				type: 'json',
                				model:'td.model.ProjectModel',
                				rootProperty: 'project'
                			});
                			var res=reader.read(data);
                			if(res){
                				form.setRecord(res.getRecords()[0]);
                			}
                			
                			Ext.getCmp('deleteProjectButton').show();
                        	Ext.getCmp('cloneProjectButton').show();
                        	Ext.getCmp('listBugsButton').show();
                        	Ext.getCmp('listMembersButton').show();
                        	Ext.getCmp('listTasksButton').show();
                        	Ext.getCmp('mainPanel').setActiveItem(pf);
                		},
                		reader:{
        				    type: 'json',
        				    rootProperty: 'project'
                		},
                		failure:function(form,result){
                			Ext.Msg.alert('readProjectError', 'result='+result, Ext.emptyFn);
                		},
                		scope:this
                	});
                	
                }
            }
        });
        
        var mainPanel=Ext.create('Ext.Panel',{
			id:'mainPanel',
        	fullscreen:true,
        	layout:'card',
        	items:[
				dataview,//0
				Ext.create('td.view.ProjectFormPanel',{//1
					id:'projectForm'
				}),
				Ext.create('td.view.BugList',{//2
					id:'bugListPanel'
				}),
				Ext.create('td.view.MemberListPanel',{//3
					id:'memberListPanel'
				}),
				//Ext.create('Ext.Panel',{//4
				//	layout: 'fit',
				//	id:'taskListPanel'
				//})
				Ext.create('td.view.TaskListPanel',{//4
					id:'taskListPanel'
				}),
				Ext.create('td.view.BugFormPanel',{//5
					id:'bugForm'
				}),
				Ext.create('td.view.UserListPanel',{//3
					id:'userListPanel'
				})
			]
        });
        
        mainPanel.setActiveItem(0);
        Ext.Viewport.add(mainPanel);
		
		dataview.setMasked({
		   xtype: 'loadmask',
		   message: '等候加载全部项目..'
		});
		store.load({params:{'command':'tdprojectlist'},callback:function(r, options, success){
			dataview.unmask();
		}});
//        Ext.Viewport.add(Ext.create('td.view.Main'));
    },

    onUpdated: function() {
        Ext.Msg.confirm(
            "Application Update",
            "This application has just successfully been updated to the latest version. Reload now?",
            function(buttonId) {
                if (buttonId === 'yes') {
                    window.location.reload();
                }
            }
        );
    }
});
