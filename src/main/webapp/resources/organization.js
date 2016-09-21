Ext.define('UserModel', {
	extend:'Ext.data.Model',
	fields:[
		{name:'userId'},
		{name:'email'},
		{name:'phone'},
		{name:'name'},
		{name:'title'},
		{name:'image'},
		{name:'gender'}
	],
	idField:'userId'
});

Ext.define('Dragon.OrganizationPanel',{
	extend: 'Ext.ux.desktop.Module',
	id:'organization-win',

    init : function(){
        this.launcher = {
            text: '通信录',
            iconCls:'accordion'
        };
    },
    createOrgPanel:function(){
		Ext.util.Format.comboRenderer = function (combo) {
		    return function (value) {
		        var record = combo.findRecord(combo.valueField, value);
		        return record
						? record.get(combo.displayField)
						: combo.valueNotFoundText;
		    };
		};
		
    	var treeStore = Ext.create('Ext.data.TreeStore', {
            proxy: {
                type: 'ajax',
                url: 'view?command=organization'
            },
            nodeParam : 'parentId',
            fields: ['text', 'id', 'leaf']
        });
    	
    	var uvstore = new Ext.data.JsonStore({
    		proxy: {
                type: 'ajax',
                url: 'view?command=listUsers',
                reader: {
                    type: 'json',
                    rootProperty: 'users'
                }
            },
    		model:'UserModel',
			autoLoad:false
    	});
    	
    	var imageTpl = new Ext.XTemplate(
		    '<tpl for="users">',
		        '<div style="margin-bottom: 10px;" class="thumb-wrap">',
		          '<img src="{image}" width="16" height="16"/>',
		          '<br/><span>{name}</span>',
		        '</div>',
		    '</tpl>'
		);
    	
//    	var uview = Ext.create('Ext.view.View', {
//    	    store: uvstore,
//    	    tpl: imageTpl,
//    	    itemSelector: 'div.thumb-wrap',
//    	    emptyText: 'No images available',
//    	    id:'userviewpanel',
//    	    overClass:'x-view-over',
//			region:'center',
//			width:'70%'
//    	});
    	
    	var combogender = Ext.create("Ext.form.ComboBox", {
    	    triggerAction: 'all',
    	    lazyRender: true,
    	    mode: 'local',
    	    editable: false,
    	    store: new Ext.data.ArrayStore({
                fields: ['gender_name', 'gender'],
                data: [
                    ['男', '1'],
                	['女', '2']
                ]
            }),
            valueField: 'gender',
            displayField: 'gender_name'
        });
    	
    	var uview = Ext.create('Ext.grid.Panel', {
    		store: uvstore,
    		id:'userviewpanel',
    		region:'center',
    		width:'70%',
    		columns:[{
	        	header: '行数',
	            xtype: 'rownumberer',
	            width: 30,
	            sortable: false,
	            tooltip:'行数',
	            resizable:false
	        },{
	            dataIndex: 'userId',
	            hidden: true
		    }, {
				header : '头像',
				dataIndex : 'image',
				resizable:false,
				renderer : function(value) {
					return "<img src='"+value+"' width='48' height='48'/>";
				}
			}, {
	        	header: '名字',
	            tooltip:'用户姓名',
	            sortable: true,
	            dataIndex: 'name',
	            width: 70,
	            editor: {
	                allowBlank: false
	            }
	        }, {
	        	header: '邮箱',
	            tooltip:'用户邮箱',
	            sortable: true,
	            dataIndex: 'email',
	            width: 70,
	            editor: {
	                allowBlank: false
	            }
	        }, {
	        	header: '手机',
	            tooltip:'用户手机号码',
	            sortable: true,
	            dataIndex: 'phone',
	            width: 70,
	            editor: {
	                allowBlank: false
	            }
	        }, {
	        	header: '职位',
	            tooltip:'用户职位',
	            sortable: true,
	            dataIndex: 'title',
	            width: 70,
	            editor: {
	                allowBlank: false
	            }
	        }, {
	        	header: '性别',
	            tooltip:'性别',
	            sortable: true,
	            dataIndex: 'gender',
	            renderer: function(value){
	            	if('1'==value) return '男';
	            	else return '女';
	            },
	            editor:combogender,
	        }]
    	});
    	
    	var org=Ext.create('Ext.panel.Panel',{
    		layout:'border',
    		width:'100%',
    		items:[{
    			xtype:'treepanel',
    			region:'west',
    			width:'30%',
    			store:treeStore,
    			rootVisible:false,
    			listeners:{
    				itemclick:function(tree , record , item , index , e , eOpts){
    					var id=record.get('id');
    					uvstore.load({params:{departId:id}});
    				}
    			}
    		},uview]
    	});
    	
    	return org;
    },
    createWindow : function(){
    	 var desktop = this.app.getDesktop();
         var win = desktop.getWindow('organization-win');
         
         if (!win) {
        	 var org=this.createOrgPanel();
        	 
        	 win = desktop.createWindow({
                 id: 'organization-win',
                 title: '通信录',
                 width: 800,
                 height: 400,
                 resizable:true,
                 scrollable:true,
                 //border: false,
 				 tbar: [],
 				 layout:'fit',
                 items: [org]
        	 });
         }
         
         return win;
    }
});