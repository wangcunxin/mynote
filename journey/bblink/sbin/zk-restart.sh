#!/bin/sh
source ~/.bashrc
echo $KAFKA_HOME

dir="/opt/cloud/"
pre=cloud
user='hadoop'

cmd=$1
echo "input cmd : $cmd"

for i in {1..3}
do
  hostname=$pre$i
  echo $hostname

 ssh "$user@$hostname" "${dir}/zookeeper/bin/zkServer.sh" "$cmd" &

# ssh "$user@$hostname" "${dir}/kafka/bin/kafka-server-stop.sh" "${dir}/kafka/config/server.properties" &

# ssh "$user@$hostname" "${dir}/kafka/bin/kafka-server-start.sh" "${dir}/kafka/config/server.properties" &

done

exit
