������:main.c
# define STD_INPUT 0                                 // �����׼�����豸������
# define STD_OUTPUT 1                             // �����׼����豸������
int fd[2];
main()
{
static char process1[]="c",process2[]="hd";
               pipe(fd);                                                // ����ܵ�
               pipeline(process1,process2);                // �����Զ��庯��pipeline()
               exit(1);                                                   // �������
}
 
pipeline(char* process1,char* process2)
{
          int i;
               while ((i=fork())==-1);                          // �������̣�ֱ�������ɹ�Ϊֹ
          if (i)
               {
                         close(fd[0]);                                  // �رչܵ�����������
                         close(STD_OUTPUT);               // �رձ�׼���������1
                         dup(fd[1]);                                   // ָ����׼���������1Ϊ�ܵ�дָ��
                         close(fd[1]);                                  // �ر�ԭʼ�ܵ�дָ��
execl(process1, process1, 0);        // �ó���father���ǵ�ǰ����
                         printf(" father failed.\n");             // execl()ִ��ʧ��
               }
               else
               {
                         close(fd[1]);                                  // �رչܵ����������
                         close(STD_INPUT);                    // �رձ�׼����������0
                         dup(fd[0]);                                   // ָ����׼����������0Ϊ�ܵ���ָ��
                         close(fd[0]);                                  // �ر�ԭʼ�ܵ���ָ��
                         execl(process2,process2,0);         // �ó���child���ǵ�ǰ����
                         printf("child failed.\n");                // execl()ִ��ʧ��
               }
               exit(2);                                                   // �������
}
��ʣ�c.c
   main()
      {
          printf("�̲�����\n") ;
   sleep(4);
          printf("��ʢ��\n") ;
          sleep(4);
          printf("�ʵ�˫˫\n") ;
   sleep(4);
          printf("���ǻ�\n") ;
   sleep(4);
          return(0) ;
      }
����:hd.c
# include <fcntl.h>
# include <stdio.h>
# include <stdlib.h>
# include <string.h>
# include <unistd.h>
# include <sys/ioctl.h>
# include <sys/types.h>
# include <linux/kd.h>
int main(int argc,char**argv)
{
int console_fd;
int i;
int s[]={330,392,440,523,311,440,523,784,784,1047,880,784,659,784,587,587,659,497,440,523,587,330,523,440,492,440,392,440,523,392};
int len[]={1000,725,250,725,250,250,250,500,725,250,250,250,250,250,2000,725,250,500,500,725,250,500,500,500,500,250,250,250,250,2000};
if((console_fd=open("/dev/console",O_WRONLY))==-1)
{
fprintf(stderr,"Failed to open console.\n");
perror("open");
exit(1);
}
for(i=0;i<30;i++)
{
int magical_fairy_number=1190000/s[i];
ioctl(console_fd,KIOCSOUND,magical_fairy_number);
usleep(1000*len[i]);
ioctl(console_fd,KIOCSOUND,0);
usleep(1000*50);
}
}
