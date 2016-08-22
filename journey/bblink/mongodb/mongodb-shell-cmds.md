
export:
new Date('2015,07,05').getTime()

/opt/mongodb/bin/mongoexport -h 112.65.205.87 --port 30000 -d bblinklogs -c userAuthorizedLog -u bblink_logs -p Bblink#2015$ --csv -q '{createTime:{$gte:new Date(1438704000000),$lt:new Date(1438790400000)}}' -f _id,className,userId,gwId,userType,userMac,authorizeTime,createTime -o ~/0805.csv

#find

db.bblink_wifilog_userlogin_uv_day.find({"day":{$gte:"2015-08-01",$lte:"2015-08-10"}}).sort({day:-1})
db.bblink_wifilog_userauthorize_uv_day.find({$and:[{day:{$gte:"2015-06-01",$lte:"2015-06-30"}}]}).sort({day:1})
db.userAuthorizedLog.find({$and:[{authorizeTime:{$gte:ISODate("2015-04-08T16:00:00.00Z"),$lt:ISODate("2015-04-09T16:00:00.00Z")}}]}).count()
db.bblink_wifilog_userlogin_count.find({$and:[{day:"2015-10-31"},{sup_id:{$eq:'3'}}]})

db.getCollection('bblink_wifilog_userauthorize_count').find({$and:[{user_type:'MOBILE'},{sup_id:'3'},{day:'2015-10-03'}]}).sort({day:-1})

db.bblink_wifilog_userlogin_count.aggregate([{$match:{"day":"2015-10-31"}},{$group:{_id:"$_id",sum:{'$sum':"$count"}}}]);
db.userAuthorizedLog.find({$and:[{createTime:{$gte:ISODate("2015-06-06T16:00:00.00Z"),$lt:ISODate("2015-06-07T16:00:00.00Z")}},{gwId:{$eq:'000babf02918'}}]}).count()
db.bblink_wifilog_userauthorize_count.find({$and:[{day:"2015-08-27"},{gwid:{$eq:'29ADF0CFF9'}}]})


#sort
db.bblink_wifilog_userauthorize_count.find({"day":"2015-06-01"}).sort({gwid:1,day:1,hour:1})

#remove
db.bblink_wifilog_userauthorize_count.remove({"day":"2015-04-09"})
db.bblink_wifilog_userauthorize_uv_day.remove({$and:[{day:{$gte:"2015-06-01",$lte:"2015-06-30"}}]});

#drop
db.bblink_wifilog_userlogin_count.drop();

