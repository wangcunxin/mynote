

--- ok

mysqlimport  --ignore-lines=1 --fields-terminated-by=, --lines-terminated-by='\r\n' --columns='user_type,uid,user_name,user_pwd,nick_name,real_name,mobile,telephone,email,gender,birthday,address,user_status,last_login_time,last_logout_time ,create_user_id, create_time ,modify_user_id ,modify_time, remark, mobile1 ,mobile2 ,mobile3 ,province ,city, area ,user_ico, mac, user_stages, gestation_time, batch_no, is_lock' --local -u root -p'Bblink@2015$idc#85' bblink_hos /root/tmp_bblink_user_net_info.csv 

LOAD DATA LOCAL INFILE '/home/lidl/tmp_user.csv' INTO TABLE bblink_user_net_info FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\r\n' (user_type,uid,user_name,user_pwd,nick_name,real_name,mobile,telephone,email,gender,birthday,address,user_status,last_login_time,last_logout_time ,create_user_id, create_time ,modify_user_id ,modify_time, remark, mobile1 ,mobile2 ,mobile3 ,province ,city, area ,user_ico, mac, user_stages, gestation_time, batch_no, is_lock);

insert into bblink_user_net_info(user_type,uid,user_name,user_pwd,nick_name,real_name,mobile,telephone,email,gender,birthday,address,user_status,last_login_time,last_logout_time ,create_user_id, create_time ,modify_user_id ,modify_time, remark, mobile1 ,mobile2 ,mobile3 ,province ,city, area ,user_ico, mac, user_stages, gestation_time, batch_no, is_lock) select user_type,uid,user_name,user_pwd,nick_name,real_name,mobile,telephone,email,gender,birthday,address,user_status,last_login_time,last_logout_time ,create_user_id, create_time ,modify_user_id ,modify_time, remark, mobile1 ,mobile2 ,mobile3 ,province ,city, area ,user_ico, mac, user_stages, gestation_time, batch_no, is_lock from tmp_bblink_user_net_info

update bblink_user_net_info set last_logout_time=null where last_logout_time<'2000-01-01' 
update bblink_user_net_info set create_time=null where create_time<'2000-01-01' 

---
--- ok
mysqlimport  --ignore-lines=1 --fields-terminated-by=, --lines-terminated-by='\r\n' --columns='gw_id, supp_id, user_id, user_type, user_name, login_time, logout_time, mac, ip, user_agent, download_flow, upload_flow, os, browser, ratio, batch_no' --local -u root -p'Bblink@2015$idc#85' bblink_hos /root/tmp_bblink_wxcity_user_info.csv

LOAD DATA LOCAL INFILE '/home/lidl/tmp_wx.csv' INTO TABLE bblink_wxcity_user_info FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\r\n' (gw_id, supp_id, user_id, user_type, user_name, login_time, logout_time, mac, ip, user_agent, download_flow, upload_flow, os, browser, ratio, batch_no);

insert into bblink_wxcity_user_info(gw_id, supp_id, user_id, user_type, user_name, login_time, logout_time, mac, ip, user_agent, download_flow, upload_flow, os, browser, ratio, batch_no)select gw_id, supp_id, user_id, user_type, user_name, login_time, logout_time, mac, ip, user_agent, download_flow, upload_flow, os, browser, ratio, batch_no from tmp_bblink_wxcity_user_info;
---



 create table if not exists tmp_new_mobile(id int(11) not null auto_increment,mobile varch(18) default null,primary key(id)) engine=InnoDB


user_id,user_type,uid user_name,user_pwd,nick_name,real_name,mobile,telephone,email,gender,birthday,address,user_status,last_login_time,last_logout_time ,create_user_id, create_time ,modify_user_id ,modify_time, remark, mobile1 ,mobile2 ,mobile3 ,province ,city, area ,user_ico, mac, user_stages, gestation_time, batch_no, is_lock

id, gw_id, supp_id, user_id, user_type, user_name, login_time, logout_time, mac, ip, user_agent, download_flow, upload_flow, os, browser, ratio, batch_no

