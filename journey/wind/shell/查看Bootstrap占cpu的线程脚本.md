#!/bin/bash
PID=`ps aux | grep Bootstrap | head -2|tail -1 | awk '{print $2}'`

echo $PID

ps -mp $PID -o THREAD,tid,time | sort -k 2 -r | head -10

echo -n -e " input tid:"

read f

if [ -z $f ]

then

echo "no"

else

jstack "$PID" | grep `printf "%x\n"  $f` -A 50

fi