/**
 * Created by dux.fangl on 8/31/2016.
 */
Ext.define('comboTask',{ 
	extend:"Ext.form.ComboBox", 
	config:{
	    typeAhead: true,
	    triggerAction: 'all',
	    lazyRender: true,
	    mode: 'local',
	    editable: true,
	    store: new Ext.data.ArrayStore({
	        fields: ['nowTaskName', 'nowTaskID'],
	        data: [
	            ['', ''],
	        	['1.场景分析评估', '1'],
	        	['2.产品方案设计', '2'], 
	        	['3.场景数据获取', '3'], 
	        	['4.场景合同签署', '4'], 
	        	['5.数据分析与风险政策产出', '5'], 
	        	['6.产品推介', '6'], 
	        	['7.系统对接', '7'], 
	        	['8.试运营业务对接', '8']
	        ]
	    }),
	    valueField: 'nowTaskID',
	    displayField: 'nowTaskName'
	}
});