#!/bin/bash

## 接收指定运行日期参数，不合法则直接退出，不传刚取昨天的日期
runday=$1
t=`date  '+%Y-%m-%d %H:%M:%S'`
if [ "$runday" = "" ]; then
        runday=`date -d "today -1 days" +%Y-%m-%d`
else
        if echo $runday | grep -q "-" && date -d $runday +%Y-%m-%d 2>/dev/null; then
            echo ""
        else 
            echo $t" error: please input a valid date format,YYYY-MM-dd ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
            exit 0  
        fi
fi

## 进入hdfs的home目录，执行后续脚本
cd ~

t=`date  '+%Y-%m-%d %H:%M:%S'`
echo $t" info: import $runday order data ........................................."
## 如果hdfs存在运行日期目录，则删除当天目录
hadoop fs -test -e /user/sqoop/orders/$runday
if [ $? -ne 0 ]; then
    echo ""
else
    t=`date  '+%Y-%m-%d %H:%M:%S'`
    echo $t" info: target directory is exists,rm and reload data"
    hadoop fs -rm -r /user/sqoop/orders/$runday
    #echo $t" info: target directory is exists."
    #exit 1
fi

## 开始导入数据

startday=$runday
endday=`date -d "$runday 1 days" +%Y-%m-%d`

sqoop import \
--driver org.postgresql.Driver \
--connect "jdbc:postgresql://172.28.3.51/report" \
--username user_select  \
--password Dxuser323X \
--table report.ticketorder \
--where "  paidtime >= '$runday 00:00:00' and paidtime < '$endday 00:00:00'" \
--columns "recordid, ordertype, tradeno, citycode, status, createtime, addtime,paidtime, memberid, partnerid, paymethod, paybank, payseqno,totalcost, amount, alipaid, gewapaid, quantity, itemfee, otherfee,discount, disreason, description, regtime, regfrom,spid, acount, bcount, ccount, dcount, aamount, bamount, camount,damount, point, specdis, accountpay, wabipay, relatedid, placeid,itemid, costprice, playtime, beforemin, primetime, opentype,edition, buytype, tag, ptime, synchother, settle, updatetime,addpoint, buytimes, downtime, synchtime, itemcost, startdate,enddate, synstatus, fromcity, firstpay, paytimes, category, pricategory,maingewapay, mobilemd5, ordertypesh, filmfest, expressfee, unitprice,deposit, gewadiscount, membercardvalue, membercardgain, membercarddisrate,areaid, totalcostsh, salecycle, adddateid, paiddateid, merchantcode,gatewaycode, newpaymethod, tmpbuytimes, inneramount, partneridsh" \
--target-dir /user/sqoop/orders/$runday  \
-fields-terminated-by  '|' \
--append \
-m 1;




t=`date  '+%Y-%m-%d %H:%M:%S'`
if [ $? -eq 0 ];  then
        echo $t" info: import $runday orders data success!"
        hive -e "alter table orders add partition (ds='$runday') location '/user/sqoop/orders/$runday';"
        echo " "
        echo " "
else
    echo $t" Error info:" 1>&2
    exit 1
fi
exit 0

