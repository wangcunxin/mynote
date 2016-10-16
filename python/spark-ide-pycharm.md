#pycharm
1.wget -c https://download.jetbrains.com/python/pycharm-community-2016.2.tar.gz

2.settings：version control->github / git editor->colors&fonts

3.设置python：install py4j pip setuptools python-dev 在interpret中添加python virtualenv的虚拟环境spark_env

在python->import sys,println(sys.path)->

找到类似于：/home/kevin/spark_env/lib/python2.7/site-packages

sudo vim pyspark.pth:
    $SPARK_HOME/python
    $SPARK_HOME/python/lib/py4j-0.8.2.1-src.zip

File->Invalidate cache/restart