cat /etc/passwd 
0.add new user
sudo useradd -m hadoop -s /bin/bash
sudo passwd hadoop
sudo adduser hadoop sudo

sudo apt-get install openssh-server
cd ~/.ssh/                     # ��û�и�Ŀ¼������ִ��һ��ssh localhost
ssh-keygen -t rsa              # ������ʾ�������س��Ϳ���
cat ./id_rsa.pub >> ./authorized_keys  # ������Ȩ



1.install jdk7
sudo apt-get install openjdk-7-jre openjdk-7-jdk
dpkg -L openjdk-7-jdk | grep '/bin/javac'
�ҵ�path 
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

Hadoopα�ֲ�ʽ����
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

���ã��������ã����ܴ���mapred-site.xml������������
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

./sbin/start-yarn.sh      # ����YARN
./sbin/mr-jobhistory-daemon.sh start historyserver  # ������ʷ��������������Web�в鿴��������


http://localhost:8088/cluster