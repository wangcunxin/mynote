1.VirtualBox-5.0.12-104815-Win
2.win7 �ƽ���滻uxtheme.dll:f8���밲ȫģʽ���滻
3.debian:debian-8.2.0-i386-DVD-1.iso
4.������/=15g /boot=100m swap=2g /home=other
5.grub ��������/dev/sda1

6.vi /etc/apt/source.list

deb http://ftp.cn.debian.org/debian jessie main non-free contrib 
deb http://ftp.cn.debian.org/debian jessie-proposed-updates main contrib non-free 
deb http://ftp.cn.debian.org/debian-security jessie/updates main contrib non-free
deb-src http://ftp.cn.debian.org/debian/ jessie main contrib non-free

apt-get update

7#
Building the main Guest Additions module ...fail!
apt-get install linux-headers-$(uname -r) build-essential

8#sudo
apt-get install sudo
chmod +w /etc/sudoers
 vim /etc/sudoers
 ���һ��   username     ALL=(ALL) ALL
 ����username������û���������
 ����sudoers�ļ�Ȩ��  chmod 0440 /etc/sudoers

9#ll
vim .bashrc
alias ll='ls -l'
source .bashrc

10#
dpkg-reconfigure locales
ѡ��
en_US ISO-8859-1 
zh_CN GB2312 
zh_CN.GBK GBK 
zh_CN.UTF-8 UTF-8 
zh_TW BIG5 
zh_TW.UTF-8 UTF-8
��װ��������
apt-get install ttf-arphic-gbsn00lp ttf-arphic-gkai00mp ttf-arphic-bsmi00lp ttf-arphic-bkai00mp


����������뷨

apt-get install ibus-pinyin
ibus-setup
reboot

��input source��ѡ��chinese(pinyin)


11.install jdk pycharm
wget http://download.oracle.com/otn-pub/java/jdk/7u79-b15/jdk-7u79-linux-i586.tar.gz
wget http://download.jetbrains.com/python/pycharm-community-4.5.5.tar.gz


sudo tar zxvf ./jdk-7u79-linux-i586.tar.gz  -C /home/kevin/galaxy
sudo tar zxvf ./pycharm-community-4.5.5.tar.gz  -C /home/kevin/galaxy

��interpreter�����virtue env��install pip py4j,
��/home/kevin/spark_virtual_env/local/lib/python2.7/site-packages��
�½�pyspark.pth��/home/kevin/galaxy/spark-1.6.2-bin-hadoop2.6/python
��/usr/lib/python2.7/dist-packages�£�
�½�py-spark.pth��/home/kevin/workspace/pycharm/spark_py


12.git 
apt-get install git
git --version

git config --global user.name "wangcunxin"
git config --global user.email "wangcx1217@163.com"
git config --list

# checkout a project
git init git_repository
git clone https://github.com/wangcunxin/spark-py.git


#create a new repository on the command line

echo "# spark_py" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/wangcunxin/spark_py.git
git push -u origin master


13 apt-get install pip
 wget -c https://bootstrap.pypa.io/get-pip.py
 python get-pip.py 


