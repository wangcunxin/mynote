修改mysql 5.7的root密码
service mysqld stop
vim /etc/my.cnf
添加
[mysqld]
skip-grant-tables

service mysqld start
mysql
update mysql.user set authentication_string=password('123456') where user='root';

测试：mysql -uroot -p123456
