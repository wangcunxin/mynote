0.ide: install pip setuptools py4j


1.python 在提交spark-submit提交任务代码，有可能找不到module,
可以将project路径以pyspark.pth的形式配置到“/usr/local/lib/python2.7/site-packages”。
pyspark.pth
/opt/work/gwspark


2.ide virtual env:否则找不到spark lib
/home/kevin/galaxy/sparkenv/local/lib/python2.7/site-packages/pyspark.pth

/home/kevin/galaxy/spark-1.6.2-bin-hadoop2.6/python
/home/kevin/galaxy/spark-1.6.2-bin-hadoop2.6/python/lib/py4j-0.9-src.zip

