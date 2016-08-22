暴露linux端口:
netstat |grep 8080

/sbin/iptables -I INPUT -p tcp --dport 28017 -j ACCEPT
/etc/rc.d/init.d/iptables save
/etc/rc.d/init.d/iptables restart
/etc/init.d/iptables status


