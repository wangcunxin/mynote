#!/bin/bash
#ssh-keygen -t rsa
for a in {1..3}
do
ssh hd0$a cat /home/galaxy/.ssh/id_rsa.pub>>/home/galaxy/.ssh/authorized_keys
done

for b in {2 3}
do
scp /home/galaxy/.ssh/authorized_keys hd0$b:/home/galaxy/.ssh
done