#!/bin/bash

## Update system

cd
yum update -y 
 
## Install base package 
 
yum install setuptool system-config-network-tui vim libtool make autoconf automake htop wget curl gcc gcc-c++ -y

## Install Dependencies Graphite

yum install pycairo Django14 python-ldap python-memcached bitmap bitmap-fonts-compat python-devel \
python-crypto pyOpenSSL gcc python-zope-filesystem git gcc-c++ zlib-static MySQL-python python-setuptools \
python-psycopg2 nginx

## Download and extract package (version 0.9.12) 
 
wget -O graphite-web-0.9.12.tar.gz https://github.com/graphite-project/carbon/archive/0.9.12.tar.gz
wget -O carbon-0.9.12.tar.gz https://github.com/graphite-project/graphite-web/archive/0.9.12.tar.gz
wget -O whisper-0.9.12.tar.gz https://github.com/graphite-project/whisper/archive/0.9.12.tar.gz
 
tar xvfz graphite-web-0.9.12.tar.gz
tar xvfz carbon-0.9.12.tar.gz
tar xvfz whisper-0.9.12.tar.gz
 
mv graphite-web-0.9.12 graphite
mv carbon-0.9.12 carbon
mv whisper-0.9.12 whisper
 
rm -f graphite-web-0.9.12.tar.gz
rm -f carbon-0.9.12.tar.gz
rm -f whisper-0.9.12.tar.gz
 
###
 
yum install python-pip
pip install django-tagging
pip install zope.interface
pip install 'Twisted<12.0'
pip install txamqp
 
####################################
# INSTALL WHISPER
####################################
 
cd ~/whisper
python setup.py install
 
####################################
# INSTALL CARBON
####################################
 
cd ~/carbon
python setup.py install
 
####################################
# CONFIGURE GRAPHITE (webapp)
####################################
 
cd ~/graphite
python check-dependencies.py
python setup.py install
 
####################################
# CONFIGURE 
####################################
 
cd /opt/graphite/conf
mkdir examples; mv *.example examples
 
cp examples/storage-schemas.conf.example storage-schemas.conf 
cp examples/storage-aggregation.conf.example storage-aggregation.conf 
cp examples/carbon.conf.example carbon.conf 
cp examples/graphite.wsgi.example wsgi.py
 
cp /opt/graphite/webapp/graphite/{local_settings.py.example,local_settings.py}

#### Nginx Config
 
#Create nginx configuration file: #/etc/nginx/conf.d/
cat > /etc/nginx/conf.d/graphite.conf <<EOF
server { 
 
	listen 8080; 
	charset utf-8; 
 
	access_log /var/log/nginx/graphite.access.log; 
	error_log /var/log/nginx/graphite.error.log; 
 
	location / { 
		include uwsgi_params; 
		uwsgi_pass 127.0.0.1:3031; 
	} 
}
EOF
 
#### uwsgi config
 
pip install uwsgi
mkdir -p /etc/uwsgi/

cat > /etc/uwsgi/graphite.ini <<EOF
 
[uwsgi] 
 
processes = 2 
socket = 127.0.0.1:3031 
gid = nginx
uid = nginx
chdir = /opt/graphite/conf 
module = wsgi:application
EOF

####################################
# INITIAL DATABASE CREATION
####################################
 
cd /opt/graphite/webapp/graphite/
python manage.py syncdb

# follow prompts to setup django admin user
chown -R nginx: /opt/graphite/storage/
 
####################################
# START CARBON
####################################
 
cd /opt/graphite/
./bin/carbon-cache.py start