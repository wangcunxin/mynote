主程序:main.c
# define STD_INPUT 0                                 // 定义标准输入设备描述符
# define STD_OUTPUT 1                             // 定义标准输出设备描述符
int fd[2];
main()
{
static char process1[]="c",process2[]="hd";
               pipe(fd);                                                // 定义管道
               pipeline(process1,process2);                // 调用自定义函数pipeline()
               exit(1);                                                   // 程序结束
}
 
pipeline(char* process1,char* process2)
{
          int i;
               while ((i=fork())==-1);                          // 创建进程，直到创建成功为止
          if (i)
               {
                         close(fd[0]);                                  // 关闭管道输入描述符
                         close(STD_OUTPUT);               // 关闭标准输出描述符1
                         dup(fd[1]);                                   // 指定标准输出描述符1为管道写指针
                         close(fd[1]);                                  // 关闭原始管道写指针
execl(process1, process1, 0);        // 用程序father覆盖当前程序
                         printf(" father failed.\n");             // execl()执行失败
               }
               else
               {
                         close(fd[1]);                                  // 关闭管道输出描述符
                         close(STD_INPUT);                    // 关闭标准输入描述符0
                         dup(fd[0]);                                   // 指定标准输入描述符0为管道读指针
                         close(fd[0]);                                  // 关闭原始管道读指针
                         execl(process2,process2,0);         // 用程序child覆盖当前程序
                         printf("child failed.\n");                // execl()执行失败
               }
               exit(2);                                                   // 程序结束
}
歌词：c.c
   main()
      {
          printf("碧草青青\n") ;
   sleep(4);
          printf("花盛开\n") ;
          sleep(4);
          printf("彩蝶双双\n") ;
   sleep(4);
          printf("久徘徊\n") ;
   sleep(4);
          return(0) ;
      }
歌曲:hd.c
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
