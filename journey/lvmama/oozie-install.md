cd /usr/local
sudo wget http://apache.fayea.com/oozie/4.1.0/oozie-4.1.0.tar.gz
sudo tar zxvf oozie-4.1.0.tar.gz
sudo vim /usr/local/oozie-4.1.0/pom.xml
    <targetJavaVersion>1.7</targetJavaVersion>

sudo vim /usr/local/oozie-4.1.0/docs/pom.xml
    <dependency>
        <!-- Customized Doxia Maven Plugin for twiki documentation -->
        <groupId>org.apache.maven.doxia</groupId>
        <artifactId>doxia-module-twiki</artifactId>
        <version>1.0</version>
    </dependency>
    <dependency>
        <!-- Customized Doxia Maven Plugin for twiki documentation -->
        <groupId>org.apache.maven.doxia</groupId>
        <artifactId>doxia-core</artifactId>
        <version>1.0</version>
    </dependency>

sudo vim /usr/local/oozie-4.1.0/bin/mkdistro.sh
    export JAVA_HOME=/usr/java/jdk1.7.0_71
    /usr/local/apache-maven-3.0.4/bin/mvn clean package assembly:single ${MVN_OPTS} "$@"

sudo /usr/local/oozie-4.1.0/bin/mkdistro.sh -DskipTests -Dhadoop.version=2.3.0

cd /usr/local/oozie-4.1.0/distro/target/oozie-4.1.0-distro/oozie-4.1.0
sudo mkdir libext

cd /usr/local/oozie-4.1.0/distro/target/oozie-4.1.0-distro/oozie-4.1.0/libext
sudo cp /tmp/ext-2.2.zip ./
sudo cp /tmp/mysql-connector-java-5.1.38.jar ./
sudo cp /usr/hadoop/share/hadoop/*/*.jar ./
sudo cp /usr/hadoop/share/hadoop/*/lib/*.jar ./
sudo rm jasper-compiler-5.5.23.jar
sudo rm jasper-runtime-5.5.23.jar 
sudo rm jsp-api-2.1.jar

cd /usr/local/oozie-4.1.0/distro/target/oozie-4.1.0-distro/oozie-4.1.0/conf
sudo vim oozie-site.xml
    <property>
        <name>oozie.service.JPAService.create.db.schema</name>
        <value>true</value>
    </property>
    <property>
        <name>oozie.service.JPAService.jdbc.driver</name>
        <value>com.mysql.jdbc.Driver</value>
    </property>
    <property>
        <name>oozie.service.JPAService.jdbc.url</name>
        <value>jdbc:mysql://192.168.0.64:3306/oozie?createDatabaseIfNotExist=true</value>
    </property>

    <property>
        <name>oozie.service.JPAService.jdbc.username</name>
        <value>oozie</value>
    </property>

    <property>
        <name>oozie.service.JPAService.jdbc.password</name>
        <value>oozie</value>
    </property>
    <property>
        <name>oozie.service.HadoopAccessorService.hadoop.configurations</name>
        <value>*=/opt/hadoop/etc/hadoop</value>
    </property>    

sudo /usr/local/oozie-4.1.0/distro/target/oozie-4.1.0-distro/oozie-4.1.0/bin/oozie-setup.sh prepare-war

sudo vim /usr/hadoop/etc/hadoop/core-site.xml
    <property>
       <name>hadoop.proxyuser.oozie.hosts</name>
       <value>*</value>
    </property>
    <property>
       <name>hadoop.proxyuser.oozie.groups</name>
       <value>*</value>
    </property>

sudo /usr/local/oozie-4.1.0/distro/target/oozie-4.1.0-distro/oozie-4.1.0/bin/oozie-setup.sh db create -run

sudo /usr/local/oozie-4.1.0/distro/target/oozie-4.1.0-distro/oozie-4.1.0/bin/oozied.sh start