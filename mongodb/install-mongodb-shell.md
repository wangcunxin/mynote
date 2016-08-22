os:linux centos

vim /etc/yum.repos.d/mongodb3.repo 

name=MongoDB Repository
baseurl=http://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/3.2/x86_64/
gpgcheck=0
enabled=1


yum search mongod
yum install mongodb-org-shell