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
    	var org=Ext.create('Ext.panel.Panel',{
    		layout:'border',
    		width:'100%',
    		items:[{
    			xtype:'treepanel',
    			region:'west',
    			width:'50%'
    		},{
    			xtype:'dataview',
    			region:'center',
    			width:'50%',
    			tpl:new Ext.XTemplate(
    				'<div class="useritem">{name}</div>'
    			),
    			itemSelector:'div.useritem'
    		}]
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