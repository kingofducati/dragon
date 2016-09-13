Ext.define('Dragon.DevProjects', {
    extend: 'Ext.ux.desktop.Module',

    id:'dev-project-win',

    init : function(){
        this.launcher = {
            text: '接入项目跟进表',
            iconCls:'accordion'
        };
    },

    createGrid : function(){
    	var combo = new comboTask();
    	
    	function fGridTooltips(value, metaData, record, rowIdx, colIdx, store) 
		{
			//==>用tooltip浮窗,显示编码后单元格内的值
			metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
			return value;
		}
		
		Ext.util.Format.comboRenderer = function (combo) {
		    return function (value) {
		        var record = combo.findRecord(combo.valueField, value);
		        return record
						? record.get(combo.displayField)
						: combo.valueNotFoundText;
		    };
		};
    	
        var grid = Ext.create('Ext.grid.Panel',{
	         scrollable:true,
	         store: new projectStore(),
	         viewConfig: {
	             trackOver: false,
	             scrollable:true
	//             markDirty: false//隐藏红三角
	         },
	         loadMask: true,
	         selModel: {
	             pruneRemoved: false
	         },
	         features: [{
	             ftype: 'groupingsummary',
	             groupHeaderTpl: '{columnName}: {name}',
	             showSummaryRow: false
	         }],
	         plugins: [
	         	Ext.create('Ext.grid.plugin.CellEditing', {
			        clicksToEdit: 1
			    }),
			    {
			    	ptype : 'rowexpander',
			    	expandOnDblClick:false,
			    	expandOnEnter:false,
			    	rowBodyTpl :new Ext.XTemplate(
			    		'<p>问题: {bugs} </p>'
	    			)
			    }
	        ],
	         selModel: {
	            selType: 'rowmodel'
	         },
	        lockedGridConfig: {
	            width: 360,
	            forceFit: true
	        },
	        lockedViewConfig: {
	//            scroll: 'horizontal'
	        	scroll:'vertical'
	        },
	        
	        // grid columns
	        columns:[{
	        	header: '行数',
	            xtype: 'rownumberer',
	            width: 60,
	            sortable: false,
	            tooltip:'行数',
	            resizable:false
	        },{
	        	header: '项目编码',
	            tooltip:'编码',
	            dataIndex: 'id',
	            hidden: true
	    }, {
	        	header: '编号',
	            tooltip:'编号',
	            sortable: true,
	            dataIndex: 'projectid',
	            resizable:false,
	            groupable: false,
	            renderer: fGridTooltips,
	            width: 70
	        }, {
	        	header: '项目名称',
	            tooltip:'项目名称',
	            flex: 1,
	            locked: true,
	            sortable: true,
	            dataIndex: 'name',
	            resizable:false,
	            groupable: false,
	            renderer: fGridTooltips,
	            width: 140,
	            editor: {
	                // defaults to textfield if no xtype is supplied
	                allowBlank: false
	            }
	        }, {
	        	header: '资金方',
	            tooltip:'资金方',
	            flex: 1,
	            locked: true,
	            sortable: true,
	            dataIndex: 'zijin',
	            resizable:false,
	            groupable: false,
	            renderer: fGridTooltips,
	            editor: {
	                // defaults to textfield if no xtype is supplied
	                allowBlank: false
	            },
	            width: 140
	        }, {
	            text: '项目分组',
	            tooltip:'group',
	            dataIndex: 'group',
	            hidden: true
	        }, {
	        	header: '负责人',
	            tooltip:'负责人',
	            sortable: true,
	            dataIndex: 'creator',
	            resizable:false,
	            groupable: false,
	            renderer: fGridTooltips,
	            width: 60,
	            editor: {
	                // defaults to textfield if no xtype is supplied
	                allowBlank: false
	            }
	        }, {
	        	header: '上线时间',
	            tooltip:'上线时间',
	            sortable: true,
	            dataIndex: 'deployTime',
	            renderer: Ext.util.Format.dateRenderer('Y-m-d'),
	            field: {
	               xtype: 'datefield',
	               format:'Y-m-d'
	            },
	            resizable:false,
	            groupable: false,
	            width: 140
	        }, {
	            text: '当前阶段ID',
	            tooltip:'当前阶段ID',
	            dataIndex: 'nowTaskID',
	            field: combo,
	            renderer: Ext.util.Format.comboRenderer(combo),
	            resizable:false,
	            groupable: false,
	            width: 140
	        }, {
	            text: '项目问题',
	            dataIndex: 'bugs',
	            resizable:false,
	            groupable: false,
	            hidden:true,
	            width: 140
	        }, {
	            text: '1.场景分析评估',
	            columns: [{
	                text: '1.1场景评估',
	                dataIndex: 'bf65707ba1c2427ab6ea0bdf5e49a510',
	                resizable:false,
	                renderer: fGridTooltips,
	                width: 100,
	                groupable: false
	            }, {
	                text: '1.2行业分析',
	                dataIndex: '00911e029cbc4cd9b2f37aefc526d597',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }]
	        }, {
	            text: '2.产品方案设计',
	            columns: [{
	                text: '2.1目标客群',
	                dataIndex: 'c6ff516c1be54cc0a06aaffb3413e0b3',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '2.2产品原型',
	                dataIndex: 'baac2b314fd845928fed79d3f13cedea',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '2.3合作模式',
	                dataIndex: 'f850a7da85b9407fa25be0ccbc97d588',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '2.4行业背景',
	                dataIndex: '23de72cc2aee4e8b9f10f1173c3f3abe',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '2.5收益预测',
	                dataIndex: '7765693e632b4450bf59093ad1863925',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '2.6风控要点',
	                dataIndex: 'a0524de1ed5c4d91b1ff80571fa7b140',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '2.7可行性',
	                dataIndex: '2bcd69aaa7844fb7a535875a5f97e4b8',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '2.8产品参数',
	                dataIndex: '7db0b51870c8494dab19a7ae5874bf7d',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }]
	        }, {
	            text: '3.场景数据获取',
	            columns: [{
	                text: '3.1数据获取',
	                dataIndex: '2ac4685d58b147ffb64d617e230ef4bf',
	                width: 160,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }]
	        }, {
	            text: '4.场景合同签署',
	            columns: [{
	                text: '4.1合同签署',
	                dataIndex: '1081f651366c4a568f804866f81eae94',
	                width: 160,
	                resizable:false,
	                renderer: fGridTooltips,
	                renderer: fGridTooltips,
	                groupable: false
	            }]
	        }, {
	            text: '5.数据分析与风险政策产出',
	            columns: [{
	                text: '5.1分析报告',
	                dataIndex: '2f72a834cd9c4d89ab802d006c6faed1',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '5.2数据款表',
	                dataIndex: '67bf28112d374b3ba90395bca4f2baa6',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '5.3风险政策',
	                dataIndex: 'd8b13bd186104f60aea1181baf951edc',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	                
	            }, {
	                text: '5.4投放规则',
	                dataIndex: '07aef9b10fc74ff799b0d768c15f53b7',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '5.5资金安排',
	                dataIndex: '18fac3b44bf44b4f8e6bb26e655dd63d',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '5.6首批名单',
	                dataIndex: 'ed548a7947e043aa8861558405f86c82',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }]
	        }, {
	            text: '6.产品推介',
	            columns: [{
	                text: '6.1项目内审',
	                dataIndex: '51a552b5545d4273b584929599a88d47',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '6.2资金立项',
	                dataIndex: 'adc077e5e50640d2863f7f502be33df4',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '6.3资金过会',
	                dataIndex: '6bbba7dd42354cc5bba21dad1e1df269',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '6.4资方协议',
	                dataIndex: 'd219f713965a406298071c694e2074d0',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '6.5政策更新',
	                dataIndex: '68caa347ff6b4cc4b9e74819844237e3',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }]
	        }, {
	            text: '7.系统对接',
	            columns: [{
	                text: '7.1BRD文档',
	                dataIndex: '3a2866eccd09401e9af594c0a1862354',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '7.2开发排期',
	                dataIndex: '3f40d9c789bb494791220ef57404a4a5',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '7.3系统开发',
	                dataIndex: 'be773cd1871549749d9d5f90a28646ba',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }]
	        }, {
	            text: '8.试运营业务对接',
	            columns: [{
	                text: '8.1操作手册',
	                dataIndex: 'd6615b1be7ba487dbbb638695d03e6fe',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '8.2客户申请',
	                dataIndex: '7e260e880cd9403b984165ae10d8b204',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }, {
	                text: '8.3客户支用',
	                dataIndex: '07ba394fe0944703a19b96325aa0cf7e',
	                width: 100,
	                resizable:false,
	                renderer: fGridTooltips,
	                groupable: false
	            }]
	        }]
		});

        this.grid=grid;
    
    	grid.on('celldblclick',function( gridPanel , td , cellIndex , record , tr , rowIndex , e , eOpts ){
         		var name=record.data['name'];
     			var p_id=record.data['id'];
     			
     			var title='项目【'+name+'】问题跟进';
     			if(!this.win){
	         		this.win=createTaskWindow(title);
     			}else{
     				this.win.setTitle(title);
     			}
     			
     			if(!this.win.isVisible()){
     				this.win.show();
     			}
     			
     			this.win.loadBug(p_id);
         	});
    
    	grid.getStore().load({
    		params: {
    			command:'list'
    		}
    	});
    	
        return grid;
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('dev-project-win');
        
        if (!win) {
        	var grid=this.createGrid();
        	
            win = desktop.createWindow({
                id: 'dev-project-win',
                title: '项目跟进表',
                width: 800,
                height: 400,
                resizable:true,
                //iconCls: 'accordion',
                //animCollapse: false,
                //constrainHeader: true,
                //bodyBorder: Ext.themeName !== 'neptune',
                scrollable:true,
                //border: false,
				tbar: [
		           { xtype: 'button', text: '导出Excel',handler:function(){
		           			Ext.Msg.prompt("导出报表", "本操作导出Excel报表，请输入发送的邮箱：", function(buttonId,value,opt){
		           				//var grid=Ext.getCmp('dev-project-win').grid;
								  if(value!=null){
									  Ext.Ajax.request({ 
										url : 'view', 
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
											
											Ext.Msg.alert('',o.msg); 
										}, 
										failure : function() { 
										} 
									});
								  }
						  });
					} 
				   },
		           { xtype: 'button', text: '保存修改',
		           	handler:
		           		function(){
		           			grid.getStore().save({params:{'command':'save'}});
		           		}  
		           		
		           },
		           { xtype: 'button', iconcls:'flush',text: '刷新',handler:function(){
		           		grid.getStore().load({
				    		params: {
				    			command:'list'
				    		}
				    	});
				    }
			       },
		           { xtype: 'button', iconcls:'flush',text: '刷新缓存',handler:function(){
		           		grid.getStore().load({
				    		params: {
				    			command:'flush'
				    		}
				    	});
				    }
			       }
		         ],
		         layout:'fit',
                items: [
                    grid
                ]
            });
            
            win.grid=grid;
        }else{
        	win.grid.getStore().load({
	    		params: {
	    			command:'list'
	    		}
	    	});
        }

        return win;
    }
});
