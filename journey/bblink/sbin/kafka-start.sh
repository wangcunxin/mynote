#!/bin/bash

dir="/opt/cloud/"
pre=cloud
user='hadoop'


for i in {1..3}
do
  hostname=$pre$i
  echo $hostname

 ssh "$user@$hostname" "${dir}/kafka/bin/kafka-server-start.sh" "${dir}/kafka/config/server.properties" >/dev/null 2>&1 & 

done

bash check_ps_kafka.sh
#exit
