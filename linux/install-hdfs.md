cat /etc/passwd 
0.add new user
sudo useradd -m hadoop -s /bin/bash
sudo passwd hadoop
sudo adduser hadoop sudo

sudo apt-get install openssh-server
cd ~/.ssh/                     # 若没有该目录，请先执行一次ssh localhost
ssh-keygen -t rsa              # 会有提示，都按回车就可以
cat ./id_rsa.pub >> ./authorized_keys  # 加入授权



1.install jdk7
sudo apt-get install openjdk-7-jre openjdk-7-jdk
dpkg -L openjdk-7-jdk | grep '/bin/javac'
找到path 
vim ~/.bashrc
export JAVA_HOME=
java -version
source ~/.bashrc

2.install hadoop2.6
sudo tar -zxf *.tar.gz -C /usr/local
sudo mv ./hadoop-2.6.0/ ./hadoop
sudo chown -R hadoop ./hadoop

export HADOOP_HOME=/usr/local/hadoop
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
source ~/.bashrc

Hadoop伪分布式配置
gedit ./etc/hadoop/core-site.xml
<configuration>
        <property>
             <name>hadoop.tmp.dir</name>
             <value>file:/usr/local/tmp/hadoop</value>
             <description>Abase for other temporary directories.</description>
        </property>
        <property>
             <name>fs.defaultFS</name>
             <value>hdfs://localhost:9000</value>
        </property>
</configuration>


 hdfs-site.xml

<configuration>
        <property>
             <name>dfs.replication</name>
             <value>1</value>
        </property>
        <property>
             <name>dfs.namenode.name.dir</name>
             <value>file:/usr/local/tmp/hadoop/dfs/name</value>
        </property>
        <property>
             <name>dfs.datanode.data.dir</name>
             <value>file:/usr/local/tmp/hadoop/dfs/data</value>
        </property>
</configuration>


./bin/hdfs namenode -format
./sbin/start-dfs.sh

test:
./bin/hdfs dfs -mkdir -p /user/hadoop
./bin/hdfs dfs -mkdir input
./bin/hdfs dfs -put ./etc/hadoop/*.xml input
./bin/hdfs dfs -ls input
./bin/hadoop jar ./share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar grep input output 'dfs[a-z.]+'
./bin/hdfs dfs -cat output/*

配置，但不启用，不能存在mapred-site.xml，必须重命名
yarn:
mapred-site.xml
<configuration>
        <property>
             <name>mapreduce.framework.name</name>
             <value>yarn</value>
        </property>
</configuration>

yarn-site.xml
<configuration>
        <property>
             <name>yarn.nodemanager.aux-services</name>
             <value>mapreduce_shuffle</value>
            </property>
</configuration>

./sbin/start-yarn.sh      # 启动YARN
./sbin/mr-jobhistory-daemon.sh start historyserver  # 开启历史服务器，才能在Web中查看任务运行


http://localhost:8088/cluster