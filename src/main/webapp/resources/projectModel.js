/**
 * Created by dux.fangl on 8/31/2016.
 */
Ext.define('Project', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'projectid'
    },{
        name: 'id'
    }, {
        name: 'name'
    }, {
        name: 'group'
    }, {
        name: 'zijin'
    }, {
        name: 'creator'
    }, {
        name: 'deployTime'
    }, {
        name: 'nowTaskID'
    },{
    	name:'bugs'
    }, {
    	header:'1.1场景评估',
        name: 'bf65707ba1c2427ab6ea0bdf5e49a510'
    }, {
    	header:'1.2行业分析',
        name: '00911e029cbc4cd9b2f37aefc526d597'
    }, {
    	header:'2.1目标客群',
        name: 'c6ff516c1be54cc0a06aaffb3413e0b3'
    }, {
    	header:'2.2产品原型',
        name: 'baac2b314fd845928fed79d3f13cedea'
    }, {
    	header:'2.3合作模式',
        name: 'f850a7da85b9407fa25be0ccbc97d588'
    }, {
    	header:'2.4行业背景',
        name: '23de72cc2aee4e8b9f10f1173c3f3abe'
    }, {
    	header:'2.5收益预测',
        name: '7765693e632b4450bf59093ad1863925'
    }, {
    	header:'2.6风控要点',
        name: 'a0524de1ed5c4d91b1ff80571fa7b140'
    }, {
    	header:'2.7可行性',
        name: '2bcd69aaa7844fb7a535875a5f97e4b8'
    }, {
    	header:'2.8产品参数',
        name: '7db0b51870c8494dab19a7ae5874bf7d'
    }, {
    	header:'3.1数据获取',
        name: '2ac4685d58b147ffb64d617e230ef4bf'
    }, {
    	header:'4.1合同签署',
        name: '1081f651366c4a568f804866f81eae94'
    }, {
    	header:'5.1分析报告',
        name: '2f72a834cd9c4d89ab802d006c6faed1'
    }, {
    	header:'5.2数据款表',
        name: '67bf28112d374b3ba90395bca4f2baa6'
    }, {
    	header:'5.3风险政策',
        name: 'd8b13bd186104f60aea1181baf951edc'
    }, {
    	header:'5.4投放规则',
        name: '07aef9b10fc74ff799b0d768c15f53b7'
    }, {
    	header:'5.5资金安排',
        name: '18fac3b44bf44b4f8e6bb26e655dd63d'
    }, {
    	header:'5.6首批名单',
        name: 'ed548a7947e043aa8861558405f86c82'
    }, {
    	header:'6.1项目内审',
        name: '51a552b5545d4273b584929599a88d47'
    }, {
    	header:'6.2资金立项',
        name: 'adc077e5e50640d2863f7f502be33df4'
    }, {
    	header:'6.3资金过会',
        name: '6bbba7dd42354cc5bba21dad1e1df269'
    }, {
    	header:'6.4资方协议',
        name: 'd219f713965a406298071c694e2074d0'
    }, {
    	header:'6.5政策更新',
        name: '68caa347ff6b4cc4b9e74819844237e3'
    }, {
    	header:'7.1BRD文档',
        name: '3a2866eccd09401e9af594c0a1862354'
    }, {
    	header:'7.2开发排期',
        name: '3f40d9c789bb494791220ef57404a4a5'
    }, {
    	header:'7.3系统开发',
        name: 'be773cd1871549749d9d5f90a28646ba'
    }, {
    	header:'8.1操作手册',
        name: 'd6615b1be7ba487dbbb638695d03e6fe'
    }, {
    	header:'8.2客户申请',
        name: '7e260e880cd9403b984165ae10d8b204'
    }, {
    	header:'8.3客户支用',
        name: '07ba394fe0944703a19b96325aa0cf7e'
    }],
    idField: 'id'
});