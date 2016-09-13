Ext.define('td.view.Main', {
    extend: 'Ext.Container',
    xtype: 'main',
    requires: [
        'Ext.List'
    ],
    config: {
    	id:'mainview',
    	fullscreen:true,
        layout:'card',
        items: [{
        	xtype:'td.view.ProjectList'
        }]
    }
});
