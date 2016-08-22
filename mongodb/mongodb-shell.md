
#mongodb
shell connection:
mongo -u gewampi -p gewampi@123 192.168.2.34:40001/gewasession
rs.slaveOk()

show dbs
use admin
db.createUser({user:"sa",pwd:"123456",roles:[{role:"userAdminAnyDatabase",db:"admin"}]})


use db_test
db.createCollection("col_test")
db.col_test.insert({"company":"gewara",addr:1000})
db.col_test.find()
db.col_test.remove({})