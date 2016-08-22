#!/bin/sh
source ~/.bashrc
echo $KAFKA_HOME

dir="/opt/cloud/"
pre=cloud
user='hadoop'

for i in {1..3}
do
  hostname=$pre$i
  echo $hostname

 ssh "$user@$hostname" bash "${dir}/kafka/bin/kafka-server-stop.sh"

done

#exit
