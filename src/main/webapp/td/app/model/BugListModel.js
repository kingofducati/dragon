/**
 * Created by dux.fangl on 8/11/2016.
 */
Ext.define('td.model.BugListModel', {
	extend:'Ext.data.Model',
	xtype:'bugModel',
	config:{
		fields:[
			{name:'bugId'},
			{name:'projectId'},
			{name:'taskId'},
			{name:'bug'},
			{name:'creator'},
			{name:'creatorName'},
			{name:'owner'},
			{name:'ownerName'},
			{name:'dueDate'},
			{name:'priority'},
			{name:'state'},
			{name:'chatId'},
			{name:'proccess'},
			{name:'color'},
			{name:'remark'}	
		],
		idField:'bugId'
		
	}
});