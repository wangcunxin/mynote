#!/bin/bash
ym=2015-10
ym2=201510
for i in {27..29}
do
 ymd=$ym-$i
 ymd2=$ym2$i
 echo $ymd $ymd2
 bash main_v4_redo.sh $ymd $ymd2 &

done

