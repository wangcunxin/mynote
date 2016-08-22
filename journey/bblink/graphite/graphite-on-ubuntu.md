#Graphite on ubuntu

ref:[guide on digitalocean](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-graphite-on-an-ubuntu-14-04-server)

## install commands list

	sudo apt-get update
	sudo apt-get install graphite-web graphite-carbon
	sudo apt-get install nginx uwsgi uwsgi-plugin-python

## config

- graphite - local_settings.py

	sudo vim /etc/graphite/local_settings.py

修改：

	SECRET_KEY = 'a_salty_string' # 修改字符
	TIME_ZONE = 'Asia/Shanghai'# 修改为指定的时区
	USE_REMOTE_USER_AUTHENTICATION = True # 修改为True

database modify:

```
DATABASES = {
    'default': {
        'NAME': '/var/lib/graphite/graphite.db',
        'ENGINE': 'django.db.backends.sqlite3',
        'USER': '',
        'PASSWORD': '',
        'HOST': '',
        'PORT': ''
    }
}

```

- graphite - sync the database

	sudo graphite-manage syncdb

- carbon - graphite-carbon 修改

	sudo vim /etc/default/graphite-carbon

修改：

	CARBON_CACHE_ENABLED=true
	
- carbon - carbon.conf 修改

	ENABLE_LOGROTATION = True

## uwsgi 配置

	sudo vim /etc/uwsgi/apps-available/graphite.ini 

添加内容：

```
[uwsgi]
plugins-dir = /usr/lib/uwsgi
plugins = python
uid = _graphite
gid = _graphite
wsgi-file = /usr/share/graphite-web/graphite.wsgi
http-socket = :8080
```

	sudo ln -s /etc/uwsgi/apps-available/graphite.ini /etc/uwsgi/apps-enabled/

## 操作

- 启动carbon-cache

	sudo service carbon-cache start

-　启动uwsgi

	sudo service uwsgi start

- 访问graphite

	http://host:8080
	＃如果界面显示不正常　请检查日志

## 日志检查　

	sudo tail -f -n 100 /var/log/uwsgi/app/graphite.log 
	＃详细查看　uwsgi开始启动时的日志　可以发现一些问题
    
## 自定义数据发送到graphite

以下示例将发送数据到test.data.data　目录下．

需要修改carbon配置

	sudo vim /etc/carbon/storage-schemas.conf

添加

	[testdata]
    pattern = ^testdata.*
    retentions = 10s:1d,1m:7d,10m:1y

发送数据的python代码
    
```python
import socket
import time
import random


CARBON_SERVER = '192.168.0.97'
CARBON_PORT = 2003

message = 'testdata.data %d %d\n' % (random.randint(1,50),int(time.time()))

print 'sending message:\n%s' % message
sock = socket.socket()
sock.connect((CARBON_SERVER, CARBON_PORT))
sock.sendall(message)
sock.close()
```

##　常见问题

#### 启动失败
访问时的日志：

	--- no python application found, check your startup logs for errors ---

启动日志：

	django.db.utils.OperationalError: attempt to write a readonly database
	Wed Jun 10 12:04:41 2015 - unable to load app 0 (mountpoint='') (callable not found or import error)
	Wed Jun 10 12:04:41 2015 - *** no app loaded. going in full dynamic mode ***

这个错误说明graphite没有权限向db写入数据．

进行权限修改

	cd /var/lib/graphite/  #根据　/etc/graphite/local_setting.py database-> /var/lib/graphite/graphite.db	

查看权限，可能会发现graphite.db的权限是root 的，进行修改:

	sudo chown _graphite:_graphite graphite.db

重新启动uwsgi即可.

#### 从左侧树中删除监控列表

	cd /var/lib/graphite/whisper
    
删除相应的文件夹即可