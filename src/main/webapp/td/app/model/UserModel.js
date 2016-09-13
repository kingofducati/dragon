/**
 * Created by dux.fangl on 8/16/2016.
 */
Ext.define('td.model.UserModel', {
	extend:'Ext.data.Model',
	xtype:'userModel',
	config:{
		fields:[
			{name:'userId'},
			{name:'email'},
			{name:'phone'},
			{name:'name'},
			{name:'title'}
		],
		idField:'userId'
	}
});