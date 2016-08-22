sshpass下载地址：http://sourceforge.net/projects/sshpass/
安装
tar zxvf sshpass-1.05.tar.gz
cd sshpass-1.05
./configure --prefix=/opt/sshpass
make
make install
结合使用：
sshpass 和ssh,scp,sftp一起使用
实例：
[root@fs bin]# ./sshpass -p 123456  ssh -o StrictHostKeyChecking=no    root@192.168.1.15 "ls -t | head -n 1"
[root@fs bin]# ./sshpass -p 123456 scp -o StrictHostKeyChecking=no  /root/abc.sh  192.168.1.15:/root
-p:指定ssh的密码
-o StrictHostKeyChecking=no 避免第一次登录出现公钥检查。也就是避免出现
 
sshpass: 用于非交互的ssh 密码验证
 ssh登陆不能在命令行中指定密码，也不能以shell中随处可见的，sshpass 的出现，解决了这一问题。它允许你用 -p 参数指定明文密码，然后直接登录远程服务器。 它支持密码从命令行,文件,环境变量中读取
$> sshpass -h
 Usage: sshpass [-f|-d|-p|-e] [-hV] command parameters
   -f filename Take password to use from file
   -d number Use number as file descriptor for getting password
   -p password Provide password as argument (security unwise)
   -e Password is passed as env-var "SSHPASS"
   With no parameters - password will be taken from stdin
  -h Show help (this screen)
   -V Print version information
At most one of -f, -d, -p or -e should be used
 sshpass [-f|-d|-p|-e] [-hV] command parameters 中的 command parameters 和使用交互式密码验证的使用方法相同
 #从命令行方式传递密码
    $> sshpass -p user_password ssh user_name@192.168..1.2
    $> sshpass -p user_password scp -P22 192.168.1.2:/home/test/t .
 #从文件读取密码
     $>　echo "user_password" > user.passwd
    $> sshpass -f user.passwd ssh user_name@192.168..1.2
 #从环境变量获取密码
    $> export SSHPASS="user_password"
    $> sshpass -e ssh user_name@192.168..1.2
----------------------------------------------------------------------------------------------------
用sshpass实现ssh的自动登陆
要实现ssh自动登录，网上搜了一下，主要有两种方法：1、生成公钥。2、编写expect脚本。这两种方法，用起来都有点复杂。在新立得上安装ssh的时候，偶然发现一个sshpass，百度谷歌之，英文资料甚多，而中文资料寥寥。其实sshpass的用法很简单。
用法：
    sshpass 参数 SSH命令(ssh，sftp，scp等)。
    参数:
        -p password    //将参数password作为密码。
        -f passwordfile //提取文件passwordfile的第一行作为密码。
        -e        //将环境变量SSHPASS作为密码。
    比如说：
        scp abc@192.168.0.5:/home/xxx/test /root   这个命令的作用是将服务器端文件test传到本地文件夹/root下。
        利用sshpass，假设密码为efghi，则可写作：
        ssh -p efghi scp abc@192.168.0.5:/home/xxx/test /root
另外，对于ssh的第一次登陆，会提示：“Are you sure you want to continue connecting (yes/no)”，这时用sshpass会不好使，可以在ssh命令后面加上 -o StrictHostKeyChecking=no来解决。比如说上面的命令，就可以写作ssh -p efghi scp abc@192.168.0.5:/home/xxx/test /root -o StrictHostKeyChecking=no。
分享： 