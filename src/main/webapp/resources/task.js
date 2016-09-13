function createTaskWindow(p_title,p_projectId){
	var grid=createBugGrid(p_projectId);
	
	var win=Ext.create('widget.window', {
        title: p_title,
        modal:true,
        header: {
            titlePosition: 2,
            titleAlign: 'center'
        },
        defaults: {
            collapsible: true,
            split: true,
            bodyStyle: 'padding:5px'
        },
        closable: true,
        closeAction: 'close',
        maximizable: true,
        width: 1200,
        minWidth: 600,
        height: 400,
        layout: 'fit',
        items: [grid],
        loadBug:function(p_projectId){
        	grid.loadGrid(p_projectId);
        }
    });
	
	return win;
}

Ext.define('bug', {  
    extend: 'Ext.data.Model',  
    fields: [  
            { name: 'bugId' },
            { name: 'projectId'},
            { name: 'bug' },  
            { name: 'owner'},  
            { name: 'creator'},  
            { name: 'creatorName'},
            { name: 'taskId'},  
            { name: 'dueDate'},  
            { name: 'priority' },
            { name: 'state' }
    ],
    idField: 'id'
});


function createBugGrid(p_projectId){
	var innerGrid;
	
	var bugStore = new Ext.data.Store({  
	    model: 'bug',
	    autoSync:false,
	    sorters: [{
            property: 'priority',
            direction: 'DESC'
        }],
        pageSize:0,
        autoSync:false,
	    proxy: {
	        type: 'ajax',
	        url: 'view',
	        timeout:180000,
	        reader: {
	            type: 'json',
	            rootProperty: 'bugs'
	        }
	    },
	    listeners:[{
	    	'remove' :function ( store , records , index , isMove , eOpts ) {
//	    		Ext.Msg.alert('remove','records[0].data.id='+records[0].data['id']);
	    		Ext.Ajax.request({ 
					url : 'view?command=removeBug', 
					method : 'post', 
					timeout: 300000,
					params : {
						'projectId':records[0].get('projectId'),
						'bugId':records[0].data['id']
					}, 
					success : function(response, options) {
					}, 
					failure : function() { 
					} 
				});
	    	}
	    }]
	});
	
	
	var userBoxStore = new Ext.data.Store({  
		fields: ['user_id', 'user_name'],
	    proxy: {
	        type: 'ajax',
	        url: 'view',
	        timeout:180000,
	        reader: {
	            type: 'json',
	            rootProperty: 'members'
	        }
	    }
	});
	userBoxStore.load({params:{command:'members',projectId:p_projectId}});
	
	
	var comboUser = Ext.create("Ext.form.ComboBox", {
	    autoLoadOnValue:false,
	    triggerAction: 'all',
	    lazyRender: true,
	    editable: false,
	    store: userBoxStore,
	    queryMode: 'local',
        valueField: 'user_id',
        displayField: 'user_name'
    });
	
	var comboPriority = Ext.create("Ext.form.ComboBox", {
	    triggerAction: 'all',
	    lazyRender: true,
	    mode: 'local',
	    editable: false,
	    store: new Ext.data.ArrayStore({
            fields: ['priority_name', 'priority'],
            data: [
                ['', ''],
            	['重要且紧急', '1'],
            	['紧急', '2'], 
            	['重要', '3'], 
            	['一般', '4'], 
            	['暂缓', '5'], 
            	['停止', '6']
            ]
        }),
        valueField: 'priority',
        displayField: 'priority_name'
    });
	
	var comboState = Ext.create("Ext.form.ComboBox", {
	    triggerAction: 'all',
	    lazyRender: true,
	    mode: 'local',
	    editable: false,
	    store: new Ext.data.ArrayStore({
            fields: ['state_name', 'state'],
            data: [
                ['', ''],
            	['已完成', '1'],
            	['进行中', '2'], 
            	['暂缓', '3'], 
            	['停止', '4']
            ]
        }),
        valueField: 'state',
        displayField: 'state_name'
    });
	
	var comboTask = Ext.create("Ext.form.ComboBox", {
	    triggerAction: 'all',
	    lazyRender: true,
	    mode: 'local',
	    editable: false,
	    store: new Ext.data.ArrayStore({
            fields: ['task_name', 'taskId'],
            data: [
                ['', ''],
            	['1.1场景评估','t11'],
            	['1.2行业分析','t12'], 
            	['2.1目标客群','t21'],
            	['2.2产品原型','t22'],
            	['2.3合作模式','t23'],
            	['2.4行业背景','t24'],
            	['2.5收益预测','t25'],
            	['2.6风控要点','t26'],
            	['2.7可行性','t27'],
            	['2.8产品参数','t28'],
            	['3.1数据获取','t31'],
            	['4.1合同签署','t41'],
            	['5.1分析报告','t51'],
            	['5.2数据款表','t52'],
            	['5.3风险政策','t53'],
            	['5.4投放规则','t54'],
            	['5.5资金安排','t55'],
            	['5.6首批名单','t56'],
            	['6.1项目内审','t61'],
            	['6.2资金立项','t62'],
            	['6.3资金过会','t63'],
            	['6.4资方协议','t64'],
            	['6.5政策更新','t65'],
            	['7.1BRD文档 ','t71'],
            	['7.2开发排期','t72'],
            	['7.3系统开发','t73'],
            	['8.1操作手册','t81'],
            	['8.2客户申请','t82'],
            	['8.3客户支用','t83']
            ]
        }),
        valueField: 'taskId',
        displayField: 'task_name'
    });
	
	var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
        clicksToMoveEditor: 1,
        autoCancel: false
    });
	
	innerGrid = Ext.create('Ext.grid.Panel', {
		loadGrid:function(p_projectId){
			innerGrid.project_id=p_projectId;
			userBoxStore.load({
				params:{command:'members',projectId:innerGrid.project_id},
				callback:function(){
					bugStore.load({params:{command:'listbug',projectId:innerGrid.project_id}});
				}
			});
		},
		store: bugStore,
		collapsible: false,
		tbar:[
			{ xtype: 'button', text: '保存修改',
			   	handler:
			   		function(){
//			   			Ext.Msg.alert('save','save');
			   			bugStore.save({params:{'command':'saveBug',projectId:innerGrid.project_id},callback:function(){
			   				bugStore.load({params:{command:'listbug',projectId:innerGrid.project_id}});
			   			}});
			   		}  
			   		
			},
			{ xtype: 'button', text: '创建微信讨论组',itemId: 'createChat',
			   	handler:
			   		function(){
			   			if(this.records){
			   				for(var i=0;i<this.records.length;i++){
			   					var rec=this.records[i];
			   					Ext.Ajax.request({ 
									url : 'view', 
									method : 'post', 
									timeout: 300000,
									params : { 
										'command' :'createChat',
										'id':rec.getData().id,
										'projectId':rec.get('projectId')
									}, 
									success : function(response, options) {
										var o = Ext.util.JSON.decode(response.responseText); 
										Ext.Msg.alert('',o.msg); 
									}, 
									failure : function() { 
									} 
								});
			   				}
			   			}
			   		}  
			   		
			}
		],
		bbar: [{
            text: '添加问题',
            iconCls: 'employee-add',
            handler : function() {
                rowEditing.cancelEdit();

                // Create a model instance
                var r = Ext.create('bug', {  
                	bugId:'',
                	bug:'',
                	owner:'',
                	projectId:innerGrid.project_id,
                	creator:'',
                	taskId:'t11',
                	dueDate: '2016-07-20',
                	priority:2,
                	state:2
                });

                bugStore.insert(0, r);
                rowEditing.startEdit(r, 0);
            }
        }, {
            itemId: 'deleteBug',
            text: '删除问题',
            iconCls: 'employee-remove',
            handler: function() {
                var sm = innerGrid.getSelectionModel();
                rowEditing.cancelEdit();
                bugStore.remove(sm.getSelection());
                if (bugStore.getCount() > 0) {
                    sm.select(0);
                }
            },
            disabled: true
        }],
        plugins: [rowEditing],
        listeners: {
            'selectionchange': function(view, records) {
            	innerGrid.down('#deleteBug').setDisabled(!records.length);
            	innerGrid.down('#createChat').setDisabled(!records.length);
            	innerGrid.down('#createChat').records=records;
            }
        },
		columns: [
			{
	        	header: '问题编码',
	            tooltip:'编码',
	            dataIndex: 'bugId',
	            hidden: true
	    	},{
	        	header: '问题',
	            tooltip:'问题',
	            sortable: true,
	            dataIndex: 'bug',
	            groupable: false,
	            editor: {
	                // defaults to textfield if no xtype is supplied
	                allowBlank: false
	            },
	            width: 300
	        },{
	        	header: '归属人',
	            tooltip:'归属人',
	            sortable: true,
	            dataIndex: 'owner',
	            renderer: Ext.util.Format.comboRenderer(comboUser),
	            groupable: false,
	            editor:comboUser,
	            width: 140
	        },{
	        	header: '提问者ID',
	            tooltip:'提问者ID',
	            dataIndex: 'creator',
	            hidden: true
	    	},{
	        	header: '提问者',
	            tooltip:'提问者',
	            sortable: true,
	            dataIndex: 'creatorName',
	            groupable: false,
	            width: 140
	        },{
	        	header: '所属任务',
	            tooltip:'所属任务',
	            sortable: true,
	            dataIndex: 'taskId',
	            renderer: Ext.util.Format.comboRenderer(comboTask),
	            groupable: false,
	            editor:comboTask,
	            width: 200
	        },{
	        	header: '截止日期',
	            tooltip:'截止日期',
	            sortable: true,
	            dataIndex: 'dueDate',
	            groupable: false,
	            renderer: Ext.util.Format.dateRenderer('Y-m-d'),
	            field: {
	                xtype: 'datefield',
	                format:'Y-m-d'
	             },
	            width: 140
	        },{
	        	header: '优先级',
	            tooltip:'优先级',
	            sortable: true,
	            renderer: Ext.util.Format.comboRenderer(comboPriority),
	            dataIndex: 'priority',
	            groupable: false,
	            editor:comboPriority,
	            width: 100
	        },{
	        	header: '状态',
	            tooltip:'状态',
	            sortable: true,
	            dataIndex: 'state',
	            groupable: false,
	            renderer: Ext.util.Format.comboRenderer(comboState),
	            editor:comboState,
	            width: 100
	        }
	    ]
	});
	
	
	
	return innerGrid;
}