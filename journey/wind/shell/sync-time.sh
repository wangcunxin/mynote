#!/bin/bash
#集群时间同步
LOCAL_TIME=`date`
echo $LOCAL_TIME
for node in $MASTER_NODES $SLAVE_NODES
do
   $SSH_TO_ROOT ssh root@$node "date -s \"`date`\""
done

#时间校验
for node in  $MASTER_NODES $SLAVE_NODES
do
  SERVER_TIME=`$SSH_TO_ROOT ssh root@$node  date`
  echo "$node 服务器时间为： $SERVER_TIME "
  echo "本地时间： `date` "
  SERVER_TIME=`$SSH_TO_ROOT ssh root@$node  date +%s `
  LOCAL_TIME=`date +%s`
  let DIFF=SERVER_TIME-LOCAL_TIME
 echo "时间误差为 $DIFF 秒"
done