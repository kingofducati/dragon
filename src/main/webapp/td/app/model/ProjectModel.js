
/**
 * Created by dux.fangl on 8/15/2016.
 */
Ext.define('td.model.ProjectModel', {
	extend:'Ext.data.Model',
	xtype:'projectModel',
	config:{
		fields:[
			 {name:'projectId',type:'string'},
			 {name:'projectName',type:'string'},
			 {name:'projectCode',type:'string'},
			 {name:'nowTaskId',type:'string'},
			 {name:'projectOwnerId',type:'string'},
			 {name:'startTime',type:'date'},
			 {name:'projectGroup',type:'string'},
			 {name:'projectType',type:'string'},
			 {name:'projectZijin',type:'string'},
			 {name:'deployTime',type:'date'},
			 {name:'endTime',type:'date'}
		],
		idField:'projectId'
	}
});