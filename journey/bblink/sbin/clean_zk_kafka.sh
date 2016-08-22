#!/bin/bash
pre=cloud
user='hadoop'

for i in {1..3}
do
  hostname=$pre$i
  echo $hostname

 ssh "$user@$hostname" rm -rf /home/hadoop/tmp/kafka-logs/* &
 ssh "$user@$hostname" rm -rf /home/hadoop/tmp/zk/ver* &

done
