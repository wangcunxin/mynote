sshpass���ص�ַ��http://sourceforge.net/projects/sshpass/
��װ
tar zxvf sshpass-1.05.tar.gz
cd sshpass-1.05
./configure --prefix=/opt/sshpass
make
make install
���ʹ�ã�
sshpass ��ssh,scp,sftpһ��ʹ��
ʵ����
[root@fs bin]# ./sshpass -p 123456  ssh -o StrictHostKeyChecking=no    root@192.168.1.15 "ls -t | head -n 1"
[root@fs bin]# ./sshpass -p 123456 scp -o StrictHostKeyChecking=no  /root/abc.sh  192.168.1.15:/root
-p:ָ��ssh������
-o StrictHostKeyChecking=no �����һ�ε�¼���ֹ�Կ��顣Ҳ���Ǳ������
 
sshpass: ���ڷǽ�����ssh ������֤
 ssh��½��������������ָ�����룬Ҳ������shell���洦�ɼ��ģ�sshpass �ĳ��֣��������һ���⡣���������� -p ����ָ���������룬Ȼ��ֱ�ӵ�¼Զ�̷������� ��֧�������������,�ļ�,���������ж�ȡ
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
 sshpass [-f|-d|-p|-e] [-hV] command parameters �е� command parameters ��ʹ�ý���ʽ������֤��ʹ�÷�����ͬ
 #�������з�ʽ��������
    $> sshpass -p user_password ssh user_name@192.168..1.2
    $> sshpass -p user_password scp -P22 192.168.1.2:/home/test/t .
 #���ļ���ȡ����
     $>��echo "user_password" > user.passwd
    $> sshpass -f user.passwd ssh user_name@192.168..1.2
 #�ӻ���������ȡ����
    $> export SSHPASS="user_password"
    $> sshpass -e ssh user_name@192.168..1.2
----------------------------------------------------------------------------------------------------
��sshpassʵ��ssh���Զ���½
Ҫʵ��ssh�Զ���¼����������һ�£���Ҫ�����ַ�����1�����ɹ�Կ��2����дexpect�ű��������ַ��������������е㸴�ӡ����������ϰ�װssh��ʱ��żȻ����һ��sshpass���ٶȹȸ�֮��Ӣ���������࣬�������������ȡ���ʵsshpass���÷��ܼ򵥡�
�÷���
    sshpass ���� SSH����(ssh��sftp��scp��)��
    ����:
        -p password    //������password��Ϊ���롣
        -f passwordfile //��ȡ�ļ�passwordfile�ĵ�һ����Ϊ���롣
        -e        //����������SSHPASS��Ϊ���롣
    ����˵��
        scp abc@192.168.0.5:/home/xxx/test /root   �������������ǽ����������ļ�test���������ļ���/root�¡�
        ����sshpass����������Ϊefghi�����д����
        ssh -p efghi scp abc@192.168.0.5:/home/xxx/test /root
���⣬����ssh�ĵ�һ�ε�½������ʾ����Are you sure you want to continue connecting (yes/no)������ʱ��sshpass�᲻��ʹ��������ssh���������� -o StrictHostKeyChecking=no�����������˵���������Ϳ���д��ssh -p efghi scp abc@192.168.0.5:/home/xxx/test /root -o StrictHostKeyChecking=no��
���� 