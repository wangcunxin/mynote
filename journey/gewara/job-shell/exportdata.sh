if [ $# -lt 3] ; then
	echo "USERAGE:[TABLE:apilog,STARTDATE:2016-05-11,RANGE:30]"
	exit
fi

table=$1
exeDate=$2
range=$3
for ((i=0;i<$range;i++));
do
	partition=`date -d "$exeDate +$i days" +%F`
	`hive -e "insert into table apilog partition(ds) select calldate,calltime,method,mobiletype,appsource,appversion,appkey,apptype,callsuccess,citycode,datatype,iosdeviceid,deviceid,elapsed,format,\`from\`,idfa,maxnum,case when(api_params['userTrace'] is null) then '' else api_params['userTrace'] end usertrace,memberencode,mnet,mprovider,imei,ostype,osversion,pointx,pointy,remoteip,signmethod,systemid,uri,v,version,api_params,ds from apilog2 where ds = '$partition'"`
	echo "success export data : $partition to table : $table "
done

