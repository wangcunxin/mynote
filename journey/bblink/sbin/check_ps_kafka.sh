#!/bin/bash

pslist="Kafka QuorumPeerMain"

pre=cloud
user=hadoop
passwd=hadoop

for i in {1..3}
do
	node=$pre$i
	echo $node
 
 	jps_proc=`ssh $user@$node ${JAVA_HOME}/bin/jps`

 	for ps in $pslist
	do
		ps_value=`echo $jps_proc|grep -e $ps`
	
		if [ ! "$ps_value" ]
			then echo "$node is not running about $ps"
		else
			echo "$node is running about $ps"
		fi

	done
	
done

