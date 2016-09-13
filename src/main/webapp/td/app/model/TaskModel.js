/**
 * Created by dux.fangl on 8/16/2016.
 */
Ext.define('td.model.TaskModel', {
	extend:'Ext.data.Model',
	xtype:'taskModel',
	config:{
		fields:[
			{name:'taskId'},
			{name:'creatorId'},
			{name:'executorId'},
			{name:'projectId'},
			{name:'done'},
			{name:'priority'},
			{name:'dueDate',type:'date'},
			{name:'startDate',type:'date'},
			{name:'endDate',type:'date'},
			{name:'note'},
			{name:'content'},
			{name:'accomplished'},
			{name:'taskGroupId'},
			{name:'members'}
		],
		idField:'taskId'
	}
});