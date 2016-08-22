#!/bin/bash
bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin/"; pwd`

source $bin/galaxy-env.sh
#配置同步的目标节点，如同步到x2hadoop04节点的galaxy模块。
SRC="${GALAXY_HOME}/works"
DST="${GALAXY_USER}@x2hadoop04::galaxy"
PASSWD="${GALAXY_HOME}/sbin/rsyncd.secrets"
EVENT="modify,attrib,move,close_write,create,delete,delete_self,moved_to"
/usr/local/bin/inotifywait  -mrq -e $EVENT  ${SRC} | while read DEF
do
    rsync  -avzp  --progress   --delete  --exclude=".*.swp"    $SRC $DST  --password-file=$PASSWD > ${GALAXY_HOME}/logs/rsync/rsync.log
done