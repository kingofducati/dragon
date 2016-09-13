Ext.define('Dragon.OrganizationPanel',{
	extend: 'Ext.ux.desktop.Module',
	id:'organization-win',

    init : function(){
        this.launcher = {
            text: '通信录',
            iconCls:'accordion'
        };
    },
    createWindow : function(){
    	 var desktop = this.app.getDesktop();
         var win = desktop.getWindow('organization-win');
         
         if (!win) {
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
                 items: []
        	 });
         }
         
         return win;
    }
});