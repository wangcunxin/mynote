#!/bin/bash
for n in {01..05}
do
 node="bd${n}.gewara.cn"
 echo "$node `ssh $node /usr/sbin/ntpdate 192.168.3.222;hwclock -w > /dev/null 2>&1`"
done

for n in {01..05}
do
 node="bd${n}.gewara.cn"
 echo "$node"
 ssh $node "date -s \"`date`\""
done
