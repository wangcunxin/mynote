Redis��һ�ָ߼�key-value���ݿ⡣����memcached���ƣ��������ݿ��Գ־û�������֧�ֵ��������ͺܷḻ�����ַ����������� �Ϻ����򼯺ϡ�֧���ڷ������˼��㼯�ϵĲ������Ͳ���(difference)�ȣ���֧�ֶ��������ܡ�����RedisҲ���Ա�������һ�����ݽṹ���� ����
Redis���������ݶ��Ǳ������ڴ��У�Ȼ�󲻶��ڵ�ͨ���첽��ʽ���浽������(���Ϊ����־û�ģʽ��)��Ҳ���԰�ÿһ�����ݱ仯��д�뵽һ��append only file(aof)����(���Ϊ��ȫ�־û�ģʽ��)��
1�� ���ص�ַ��
$ wget http://redis.googlecode.com/files/redis-2.6.13.tar.gz
2�� ��ѹ��
$ tar xzf redis-2.6.13.tar.gz
3�� ����
$ cd redis-2.6.13
$ make
$make install
$cp redis.conf /etc/
�������ܣ�
make install����ִ����ɺ󣬻���/usr/local/binĿ¼�����ɱ�����ִ���ļ����ֱ���redis-server��redis-cli��redis-benchmark��redis-check-aof ��redis-check-dump�����ǵ��������£�
redis-server��Redis��������daemon��������
redis-cli��Redis�����в������ߡ�Ҳ������telnet�����䴿�ı�Э��������
redis-benchmark��Redis���ܲ��Թ��ߣ�����Redis�ڵ�ǰϵͳ�µĶ�д����
redis-check-aof�������޸�
redis-check-dump����鵼������
4�� �޸�ϵͳ�����ļ���ִ������
a) echo vm.overcommit_memory=1 >> /etc/sysctl.conf
b) sysctl vm.overcommit_memory=1 ��ִ��echo vm.overcommit_memory=1 >>/proc/sys/vm/overcommit_memory
ʹ�����ֺ��壺
0����ʾ�ں˽�����Ƿ����㹻�Ŀ����ڴ湩Ӧ�ý���ʹ�ã�������㹻�Ŀ����ڴ棬�ڴ��������������ڴ�����ʧ�ܣ����Ѵ��󷵻ظ�Ӧ�ý��̡�
1����ʾ�ں�����������е������ڴ棬�����ܵ�ǰ���ڴ�״̬��Ρ�
2����ʾ�ں�������䳬�����������ڴ�ͽ����ռ��ܺ͵��ڴ�
5�� �޸�redis�����ļ�
a) $ cd redis-2.6.13
b) vi redis.conf
c) �޸�daemonize yes---Ŀ��ʹ�����ں�̨����
�������ܣ�
daemonize���Ƿ��Ժ�̨daemon��ʽ����
pidfile��pid�ļ�λ��
port�������Ķ˿ں�
timeout������ʱʱ��
loglevel��log��Ϣ����
logfile��log�ļ�λ��
databases���������ݿ������
save * *��������յ�Ƶ�ʣ���һ��*��ʾ�೤ʱ�䣬������*��ʾִ�ж��ٴ�д��������һ��ʱ����ִ��һ��������д����ʱ���Զ�������ա������ö��������
rdbcompression���Ƿ�ʹ��ѹ��
dbfilename�����ݿ����ļ�����ֻ���ļ�����������Ŀ¼��
dir�����ݿ��յı���Ŀ¼�������Ŀ¼��
appendonly���Ƿ���appendonlylog�������Ļ�ÿ��д�������һ��log�����������ݿ�������������Ӱ��Ч�ʡ�
appendfsync��appendonlylog���ͬ�������̣�����ѡ��ֱ���ÿ��д��ǿ�Ƶ���fsync��ÿ������һ��fsync��������fsync�ȴ�ϵͳ�Լ�ͬ����
6�� ����redis
a) $ cd /usr/local/bin
b) ./redis-server /etc/redis.conf
7�� ����Ƿ������ɹ�
a) $ ps -ef | grep redis
���� 
0
ϲ��
0
����������
