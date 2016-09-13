/**
 * Created by dux.fangl on 8/16/2016.
 */
Ext.define('td.model.GroupModel', {
	extend:'Ext.data.Model',
	xtype:'groupModel',
	config:{
		fields: [
            {name: 'id',type: 'string'},
            {name: 'name',type: 'string'},
			{name: 'group',type: 'boolean'},
			{name: 'proccess',type: 'string'},
			{name: 'color',type: 'string'}
        ],
		idField:'id'
	}
});