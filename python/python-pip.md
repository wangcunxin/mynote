
 #yum install pip-python
 wget https://bootstrap.pypa.io/get-pip.py
 python get-pip.py 

pip install kafka-python pymongo pymysql

#install pip-python
wget --no-check-certificate https://pypi.python.org/packages/source/s/setuptools/setuptools-18.2.tar.gz
tar xzvf setuptools-18.2.tar.gz
cd setuptools-18.2
python setup.py install

wget --no-check-certificate https://pypi.python.org/packages/source/p/pip/pip-7.1.2.tar.gz
tar xzvf pip-7.1.2.tar.gz
cd pip-7.1.2
python setup.py install
ln -s /usr/local/python2.7.11/bin/pip /usr/bin/pip
pip install --upgrade pip


yum install postgresql-devel
pip install psycopg2 pymongo hbase-thrift pymysql kafka-python
