/**
 * Created by dux.fangl on 8/31/2016.
 */
Ext.define('projectStore', {
	extend:'Ext.data.Store',
	config:{
    	groupField: 'group',
    	model: 'Project',
        proxy: {
            type: 'ajax',
            url: 'view',
            timeout:180000,
            reader: {
                type: 'json',
                rootProperty: 'projects'
            }
        }
	}
});