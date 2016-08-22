##创建用户

- 管理用户

```shell
use admin
db.createUser({user:"root",pwd:"root20160324", roles:[{role:"root",db:"admin"}]})
db.createUser(
  {
    user: "admin",
    pwd: "admin123",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
)
```

- DB用户

```shell
use bblinklogs

db.createUser({user: "bblink",pwd: "bblink",roles: [ "userAdmin","readWrite", "dbAdmin" ]})

- - -
db.createUser(
  {
    user: "bblink",
    pwd: "bblink",
    roles: [
    	{ role: "userAdmin", db: "bblinklogs" },
    	{ role: "dbOwner", db: "bblinklogs" }
    ]
  }
)

```

##更新用户角色

```shell
---update user role---
db.updateUser("bblink", {
                   roles : [
                      { role: "userAdmin", db: "bblinklogs"},
                      { role: "readWrite", db: "bblinklogs"},
                      { role: "dbAdmin", db: "bblinklogs"},
                      { role : "clusterManager", db : "admin" }
                   ]
              })

```

## 查看用户角色

```shell
	use admin;
	db.system.users.find();
	use bblinklogs;
	db.getUser('bblink');
```
## 导入导出　
 [参考](http://www.jb51.net/article/40285.htm)
－数据库(未具体实验)

	mongodump -h dbhost -d dbname -o dbdirectory
    mongorestore -h dbhost -d dbname –directoryperdb dbdirectory

－collection

	./mongoexport -h *.*.*.* --port 30000 -d bblinklogs -c userAuthorizedLog -u aaaaaalog -p aaaaa#2015$ -o /opt/cback/userAuthorizedLog.json

	./mongoimport -h cluster1 --port 30000 -d bblinklogs -u bblink -p bblink -c userAuthorizedLog --file /opt/cback/userAuthorizedLog.json

	/opt/mongodb/bin/mongoexport -h *.*.*.* --port 30000 -d bblinklogs -c userAuthorizedLog -u aaaaaalog -p aaaaa#2015$ --csv -q '{createTime:{$gte:new Date(1433836800000),$lt:new Date(1433923200000)}}' -f _id,className,userId,gwId,userType,userMac,authorizeTime,createTime -o ~/result.csv
	/opt/mongodb/bin/mongoexport -h *.*.*.* --port 30000 -d bblinklogs -c userAuthorizedLog -u aaaaaalog -p aaaaa#2015$ --csv -q '{$and:[{createTime:{$gte:new Date(1440518400000),$lt:new Date(1440604800000)}},{gwId:{$eq:"61060300FF0CA36D"}}]}' -f _id,className,userId,gwId,userType,userMac,authorizeTime,createTime -o ~/20150827.csv

对于mongoexport -q ，这个查询语句有时不能直接从mongoshell里直接拿来用.比如这里的date参数需要为javascript里的Date() [参考](http://stackoverflow.com/questions/14758605/mongoexport-using-gt-and-lt-constraints-on-a-date-range).

这个long型日期可以这样来确定.在mongoshell里　输入

    new Date(2015,05,10,8,00,00)*1
	>1433894400000

验证：

	new Date(1433894400000)
	>ISODate("2015-06-10T00:00:00Z")

注意　上边的月份index是从0开始的．另外，ISODate要落后8小时。

## 查询

```shell
	#删除数据
	db.userAuthorizedLog.remove({userType:{$exists: false}})
	#清空表
	db.userAuthorizedLog.remove({})
	＃查询数据
	db.userAuthorizedLog.find({userType:{$exists: false}}).pretty()
	db.userAuthorizedLog.find({gwId:{$eq: null}}).pretty()
	db.userAuthorizedLog.find({createTime:{$gte:ISODate("2015-06-09T16:00:00.00Z"),$lt:ISODate("2015-06-10T16:00:00.00Z")}})
  	db.userAuthorizedLog.find({$and:[{createTime:{$gte:ISODate("2015-07-01T16:00:00.00Z"),$lt:ISODate("2015-07-02T16:00:00.00Z")}},{userType:{$eq:'WECHAT'}}]})
  	db.userAuthorizedLog.find({createTime:{$gte:ISODate("2015-06-06T16:00:00.00Z"),$lt:ISODate("2015-06-07T16:00:00.00Z")}})
	db.userAuthorizedLog.find({$and:[{createTime:{$gte:ISODate("2015-07-02T17:00:00.00Z"),$lt:ISODate("2015-07-02T18:00:00.00Z")}},{gwId:{$eq:'001631f1b54c'}}]}).count()
---
	db.getCollection('sitePVv3').find({createTime:{$gte:1441814400000,$lt:1441900800000}}).count()

### aggregate

```
db.bblink_wifilog_userlogin_count.aggregate([  
  { $match : { "day" : "2015-10-30"}},  
  { $group : { _id : "$day", num_tutorial : {$sum : "$count"},count:{$sum : 1} }}  
]);  

db.runCommand({"group":{  
    "ns":"bblink_wifilog_userlogin_count",  
    "key":"day",  
    "initial":{"total":0},  
    "$reduce" : function(doc,prev){  
        prev.total += doc.count;  
    },  
    "condition":{"day":"2015-10-30"}  
}}); 
```



```
## dump

	/opt/mongodb/bin/mongodump -h 112.65.205.87 --port 30000 -d bblinklogs -c sitePVv3 -u bblink_logs -p **** -q '{createTime:{$lt:1439568000000}}'

## 索引

建立索引：

	db.userAuthorizedLog.ensureIndex({filed1:1,field2:1})

查询集合的索引：

	db.userAuthorizedLog.getIndexes()

查询数据库已经建立的索引：

	db.system.indexes.find()

嵌套字段索引：

	db.things.ensureIndex({"address.city":1})

组合字段索引：

	db.things.ensureIndex({j:1,name:-1})

稀疏索引：

	db.people.ensureIndex({title:1},{sparse:true})
    #稀疏索引,未包含的字段的document 将不会被查询

删除索引：

    db.people.dropIndex({title:1})
    db.collection.dropIndexes();#删除所有索引

唯一索引：

	db.things.ensureIndex({firstname:1,lastname:1},{unique:true})

重复键：

	db.things.ensureIndex({firstname : 1}, {unique : true, dropDups : true})
    #可自动删除 后边重复的

重建索引：

	db.myCollection.reIndex()
    // same as:
    db.runCommand( { reIndex : 'myCollection' } )

sharding status check:

  db.collection.getShardDistribution()
