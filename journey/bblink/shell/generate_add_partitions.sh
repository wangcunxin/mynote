#!/bin/bash
sql="alter table back_portal_loginlog add if not exists partition(dat ='"
ym2=201511
for i in {04..30}
do

 ymd2=$ym2$i
 echo "$sql$ymd2');"
 

done

