function isArray(object) {
	if(object instanceof Array) {
		isArrayVar = true;
	}
	else 
	{
		isArrayVar = false;
	}
	return isArrayVar;
}

function isObject(object) {
	if(object instanceof Object) {
		isObjectVar = true;
	}
	else 
	{
		isObjectVar = false;
	}
	return isObjectVar;
}

//{table:'TB_TABLE',rowkey:'helloWorld',/*ts=option*/,{cf:{key:'val1',key2:'val2'},info:{rslt:'test'}}};
//put('TEST',i,{cf:{keyQualifier:'val'}});
function put(table , rowKey , ts , data) {
	if(!(arguments.length == 3 || arguments.length == 4)) {
		println("Put Usage : put('tableName','rowKey',{columnfamily:{qualifier:'val1',qualifier2:'val2'}}}");
		throw Exception("Put Usage : put('tableName','rowKey',{columnfamily:{qualifier:'val1',qualifier2:'val2'}}}")
	}
	if(arguments.length == 3 && ts instanceof Object) {
		data = ts;
		ts = new Date().getTime();
	}
	putVar = NEWDATA.putData();
	putVar.setTable(table);
	putVar.setRowKey(rowKey);
	putVar.setTimeStamp(ts);
	for(var cf in data) {
		for(var colObj in data) {
			println("colObj : " + colObj);
			for(var colKey in data[colObj]) {
				println("colKey : " + colKey  + " , data[colObj][colKey] :" + data[colObj][colKey]);
				putVar.addColumnInfo(colObj, colKey , data[colObj][colKey]);	
			}
		}
	}
	HBASE.put(putVar);
}
function count(table , options) {
	if(!(arguments.length == 1 || arguments.length == 2)) {
		println("Put Usage : count('tableName',{filter:[{}],startrow:'',endrow:'',startts:1,endts:9999}");
		throw Exception("Put Usage : count('tableName',{filter:[{}],startrow:'',endrow:'',startts:1,endts:9999}");
	}	
	processScanVar(table,options);
	HBASE.count(scanVar);
	
}
function scan(table , options) {
	if(!(arguments.length == 1 || arguments.length == 2)) {
		println("Put Usage : scan('tableName',{filter:[{}],startrow:'',endrow:'',startts:1,endts:9999}");
		throw Exception("Put Usage : scan('tableName',{filter:[{}],startrow:'',endrow:'',startts:1,endts:9999}");
	}
	processScanVar(table,options);
	HBASE.scan(scanVar);
}

function processScanVar(table, options) {
	scanVar = NEWDATA.scanData();
	scanVar.setTable(table);
	
	if(options != null) {
		if(options['filter'])
			var list = FILTER.FilterList();
			if(options['filter'] instanceof Array) {
				for(var filter in options['filter']) {
					list.addFilter(options['filter'][filter]);
				}
			}
			else {
				list.addFilter(options['filter']);
			}
			scanVar.setFilter(list);
		if(options['startrow'])
			scanVar.setStartRow(options['startrow']);
		
		if(options['endrow'])
			scanVar.setEndRow(options['endrow']);
		
		if(options['startts'])
			scanVar.setStartTimeStamp(options['startts']);
		
		if(options['endts'])
			scanVar.setEndTimeStamp(options['endts']);
	}
	
	return scanVar;
}