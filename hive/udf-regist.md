1.  ʹ��add jar path/test.jar;��������

�÷�����ȱ����ÿ������Hive��ʱ��Ҫ���¼��룬�˳�hive�ͻ�ʧЧ��

2.  ͨ������hive�������ļ�hive-site.xml ����

�������ļ�����������

<property>
<name>hive.aux.jars.path</name>
<value>file:///jarpath/all_new1.jar,file:///jarpath/all_new2.jar</value>
</property>

���漴�ɡ�

�÷����ȵ�һ�ַ�������ܶࡣ����Ҫÿ������Hiveִ��������룬ֻ��������΢����һЩ��

3.  ��${HIVE_HOME�д����ļ���auxlib  ��Ȼ���Զ���jar�ļ�������ļ����С�