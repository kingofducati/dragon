/**
 * Created by dux.fangl on 8/10/2016.
 */
Ext.define('td.controller.Main', {
	 extend: 'Ext.app.Controller', 
	 config: {
		 refs: {
			 projectList:'mainview #td.view.ProjectList'
		 },
		 control: { 
			 
		 },
		 init:function(){
			 var pl=this.getProjectList();
			 pl.getStore().load({params:{'command':'tdprojectlist'}});
		 }
	 }
});