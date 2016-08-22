from apscheduler.schedulers.blocking import BlockingScheduler
import datetime

def dateStr(date=None,format=''):
    return date.strftime(format)

sched=BlockingScheduler()
# every 3 second in every minute
@sched.scheduled_job('cron', second='20-40/3')
def schedulerTest():
    print("scheduler test before")
    print dateStr(datetime.datetime.now(),'%Y-%m-%d %H:%M:%S')
    time.sleep(10)
    print("scheduler test after")

if __name__ == '__main__':
    sched.start()
    sched.shutdown(wait=True)