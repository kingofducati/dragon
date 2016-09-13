/**
 * Created by dux.fangl on 8/16/2016.
 */
Ext.define('td.store.UserListStore', {
	extend:'Ext.data.Store',
	xtype:'userListStore',
	requires: [
        'td.model.UserModel'
    ],
	config:{
		model:'td.model.UserModel',
		autoSync:false,
	    sorters: [{
            property: 'name',
            direction: 'DESC'
        }],
        pageSize:0,
		proxy: {
            type: 'ajax',
            url: '../view?command=listUsers',
            timeout:180000,
            reader: {
                type: 'json',
                rootProperty: 'users'
            }
        }
	}
});