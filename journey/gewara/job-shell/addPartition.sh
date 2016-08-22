if [$# -lt 3] ; 
then
	echo "USERAGE:[TABLE:apilog,STARTDATE:2016-05-11,RANGE:30]"
	exit
fi
table=$1
exeDate=$2
range=$3
for ((i=0;i<$range;i++)) ; 
do
	partition=`date -d "$exeDate +$i days" +%F`
	`hive -e "alter table $table add partition (ds='$partition') location '/user/sqoop/trade/$partition'"`
	echo "success add partition: $partition to table : $table "
done

