/**
 * Created by dux.fangl on 8/16/2016.
 */
Ext.define('td.store.TaskListStore', {
	extend:'Ext.data.TreeStore',
	xtype:'taskListStore',
	requires: [
        'td.model.TaskModel'
    ],
	load: function(options) {
        options = options || {};
        options.params = options.params || {};

        var me = this,
            node = options.node = options.node || me.getRoot();

        options.params[me.getNodeParam()] = node.getId();
		options.params['projectId'] = me.projectId;

        if (me.getClearOnLoad()) {
            node.removeAll(true);
        }
        node.set('loading', true);

        return me.callParent([options]);
    },
	config:{
		model:'td.model.GroupModel',
		autoSync:false,
		autoLoad:false,
		clearOnLoad:true,
		defaultRootId:'rootgroup',
	    sorters: [{
            property: 'name'
        }],
        pageSize:0,
		proxy: {
            type: 'ajax',
            url: '../view?command=listTasks',
            timeout:180000,
            reader: {
                type: 'json',
                rootProperty: 'children'
            }
        }
	}
});