1.安装docker
环境vm centos vm6.5
先更新yum 源到aliyun
curl https://git.oschina.net/feedao/Docker_shell/raw/start/ali-centos.sh | sh
yum install docker-io
内核3.8以下
需要更新
 yum upgrade device-mapper-libs
 
安装完毕后 添加启动项目 docker私服地址
 
vim /etc/init.d/docker
在start() 函数里面 启动项 一行 添加 docker  启动项参数
 --insecure-registry 192.168.0.97:5000
启动 ：
service docker start
 
ps -ef|grep docker
看到如下 进程表示启动成功
/usr/bin/docker -d --insecure-registry 192.168.0.97:5000


2.docker 部署zookeeper 服务
docker pull 192.168.0.97:5000/centos-lvtu-java-zookeeper
docker images
启动
docker run -d -p 2209:22 -p 2181:2181 -m 256m  192.168.0.97:5000/centos-lvtu-java-zookeeper
ssh 端口 2209
zk本地断开 2181


3.如何push docker镜像到私服
docker images
docker tag imageid 192.168.0.97:5000/centos-lvtu-php-base
docker push 192.168.0.97:5000/centos-lvtu-php-base
最后提示 pushed 成功
 
查看私服路径
http://192.168.0.97:5000/v1/search


4.从私服pull可用镜像
centos-lvtu-php-base 已制作好的无线php镜像服务（centos6.6）
docker pull 192.168.0.97:5000/centos-lvtu-php-base
执行完毕后
 
docker run -d -p 33301:22 -p 81:80 -v /opt/:/opt/ -v /var/www/:/var/www/ -v /data/nfsroot/:/data/nfsroot/ 5f5151610f3e
参数说明
run ：启动
-p:vm端口映射docker端口 例如 81：80 表示vm81端口映射到 容器80端口
-v:表示目录挂载  例如 -v /opt/:/opt/ 表示 vm /opt/挂载到 容器 /opt/ 主要用于 文件共享存储
-d:表示后台运行
5f5151610f3e ：是镜像ID 即 IMAGE ID
 
启动后
 
通过 ssh端口即可连接  为  ssh root@127.0.0.1 -p 33301 


5.docker 安装memcached

 docker run -i -t centos-base yum install -y memcached yum clean all

docker commit contantId memcached

 docker run -d -p 11211:11211 memcached memcached -u root -m 256 -p 11211

 
 
 
6.Docker pull java容器到VM

docker push 192.168.0.97:5000/centos-lvtu-php-base
docker images
docker run -d -p 33301:22 -p 8091:8080 centos-lvtu-java-base
http://host:8091/


7.docker常用命令
构建镜像
docker build -t centos-lvtu-java-base-wap http://192.168.0.97/dockerfile/Dockerfile_wap

添加tag
docker tag -f 5f51 192.168.0.97:5000/centos-lvtu-java-base-wap

启动tcp端口
docker -H tcp://0.0.0.0:2375 --daemon=true --pidfile=/var/run/docker_2375.pid --graph="/var/lib/docker_2375" &

提交镜像
docker push 192.168.0.97:5000/centos-lvtu-java-base-wap

拉取镜像
docker pull 192.168.0.97:5000/centos-lvtu-java-base-wap

查看可用镜像
docker images

查看运行中的进程
docker ps

ssh登入
ssh -p 33302 root@127.0.0.1

移除运行中的镜像
docker rm $(docker ps -q -a)
停止运行中的镜像
docker stop faa44208f38f 或 docker stop faa4


驴途测试环境启动
zk:
docker run -d -p 33301:22 -p 8060:8080 -p 2181:2181 192.168.0.97:5000/centos-lvtu-java-zk-dubbo 

pet:
docker run -d -p 2207:22 -p 7081:8080 -v /var/www/:/var/www/ -v /opt/apache-tomcat-pet/logs/:/opt/apache-tomcat-7.0.62/logs/ -v /etc/hosts:/etc/hosts  192.168.0.97:5000/centos-lvtu-java-base-pet

push: 
docker run -d -p 2205:22 -p 5050:8080 -v /var/www/:/var/www/ -v /opt/apache-tomcat-client-push-job/logs/:/opt/apache-tomcat-7.0.62/logs/ -v /etc/hosts:/etc/hosts 192.168.0.97:5000/centos-lvtu-java-base-schedule

client: 
docker run -d -p 2201:22 -p 8080:8080 -v /etc/hosts:/etc/hosts -v /var/www/:/var/www/ -v /opt/apache-tomcat-client-service/logs/:/opt/apache-tomcat-7.0.62/logs/ 192.168.0.97:5000/centos-lvtu-java-base-client

allin: 
docker run -d -p 2206:22 -p 4040:8080 -v /var/www/:/var/www/ -v /opt/apache-tomcat-allin/logs/:/opt/apache-tomcat-7.0.62/logs/ -v /etc/hosts:/etc/hosts 192.168.0.97:5000/centos-lvtu-java-base-ai

clutter:
docker run -d -p 2202:22 -p 6060:8080 -v /var/www/:/var/www/ -v /opt/apache-tomcat-client-clutter/logs/:/opt/apache-tomcat-7.0.62/logs/ -v /etc/hosts:/etc/hosts 192.168.0.97:5000/centos-lvtu-java-base-wap

bridge:
docker run -d -p 2203:22 -p 7070:8080 -v /var/www/:/var/www/ -v /opt/apache-tomcat-sso-bridge/logs/:/opt/apache-tomcat-7.0.62/logs/ -v /etc/hosts:/etc/hosts 192.168.0.97:5000/centos-lvtu-java-base-sso-bridge

train:
docker run -d -p 2208:22 -p 9090:8080 -v /var/www/:/var/www/ -v /opt/apache-tomcat-train/logs/:/opt/apache-tomcat-7.0.62/logs/ -v /etc/hosts:/etc/hosts 192.168.0.97:5000/centos-lvtu-java-base-train

php:
docker run -d -m 512m -p 2204:22 -p 81:80 -v /data/nfsroot/:/data/nfsroot/ -v /etc/hosts:/etc/hosts 192.168.0.97:5000/centos-lvtu-php-base

shipyard:
docker run -d -m 512m -p 3201:22 -p 12345:80 192.168.0.97:5000/shipyard

