1.  使用add jar path/test.jar;方法加入

该方法的缺点是每次启动Hive的时候都要从新加入，退出hive就会失效。

2.  通过设置hive的配置文件hive-site.xml 加入

在配置文件中增加配置

<property>
<name>hive.aux.jars.path</name>
<value>file:///jarpath/all_new1.jar,file:///jarpath/all_new2.jar</value>
</property>

保存即可。

该方法比第一种方法方便很多。不需要每次启动Hive执行命令加入，只是配置稍微复杂一些。

3.  在${HIVE_HOME中创建文件夹auxlib  ，然后将自定义jar文件放入该文件夹中。