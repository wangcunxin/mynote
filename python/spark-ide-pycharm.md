#pycharm
1.wget -c https://download.jetbrains.com/python/pycharm-community-2016.2.tar.gz

2.settings��version control->github / git 
	editor->colors&fonts
	
3.����python��install py4j,pip,python-dev
	��interpret�����python virtualenv�����⻷��spark_env
	
	��python->import sys,println(sys.path)->�ҵ������ڣ�/home/kevin/spark_env/lib/python2.7/site-packages
	
	touch pyspark.pth:
		$SPARK_HOME/python
		$SPARK_HOME/python/lib/py4j-0.8.2.1-src.zip

	File->Invalidate cache/restart