/**
 * Created by dux.fangl on 8/11/2016.
 */
Ext.define('td.store.BugListStore', {
	extend:'Ext.data.Store',
	xtype:'bugStore',
	requires: [
        'td.model.BugListModel'
    ],
	config:{
		model:'td.model.BugListModel',
		autoSync:false,
	    sorters: [{
            property: 'priority',
            direction: 'DESC'
        }],
        pageSize:0,
		proxy: {
            type: 'ajax',
            url: '../view?command=listbug',
            timeout:180000,
            reader: {
                type: 'json',
                rootProperty: 'bugs'
            }
        },
	    listeners:[{
	    	'remove' :function ( store , records , index , isMove , eOpts ) {
//	    		Ext.Msg.alert('remove','records[0].data.id='+records[0].data['id']);
	    		Ext.Ajax.request({ 
					url : 'view?command=removeBug', 
					method : 'post', 
					timeout: 300000,
					params : {
						'projectId':records[0].get('projectId'),
						'bugId':records[0].data['id']
					}, 
					success : function(response, options) {
					}, 
					failure : function() { 
					} 
				});
	    	}
	    }]
	}
});