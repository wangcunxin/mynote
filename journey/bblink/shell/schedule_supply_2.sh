#!/bin/bash

ym2=201602
for i in {15..24}
do

 ymd2=$ym2$i
 echo $ymd2
 bash user_visit_day_supply.sh $ymd2

done

