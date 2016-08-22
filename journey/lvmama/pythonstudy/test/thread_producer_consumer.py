# -*- coding: UTF-8 -*-
import Queue
import random
import time
import threading


def produce(queue):
    global exitFlag
    while 1:
        ran = random.randint(0, 100)
        # queueLock.acquire()
        queue.put(ran)
        # queueLock.release()
        print "produce:%d" % ran
        if ran % 9 == 8:
            exitFlag = True
            print "mod:%d,%s" % (ran % 9, str(exitFlag))
            break
        time.sleep(2)


class Producer(threading.Thread):
    def __init__(self, thread_id, thread_name, queue):
        threading.Thread.__init__(self)
        self.thread_id = thread_id
        self.thread_name = thread_name
        self.queue = queue

    def run(self):
        produce(self.queue)


def process(queue):
    global exitFlag
    exitFlag = False
    while 1:
        queueLock.acquire()
        if not queue.empty():
            ele = queue.get()
            print "%d:%d" % (ele, queue.qsize())
            queueLock.release()
        else:
            time.sleep(5)
            print(queue.qsize())
            # print exitFlag
            queueLock.release()
            if exitFlag:
                print("break")
                break


class Consumer(threading.Thread):
    def __init__(self, thread_id, thread_name, queue):
        threading.Thread.__init__(self)
        self.thread_id = thread_id
        self.thread_name = thread_name
        self.queue = queue

    def run(self):
        process(self.queue)


if __name__ == '__main__':
    '''
    1.producer:add a num to queue
    2.consumer:remove a element from queue
    3.wait to finish all threads
    '''
    queueLock = threading.Lock()
    workQueue = Queue.Queue(100)
    threads = []
    threadID = 1

    # 创建新线程
    for i in range(0, 5, 1):
        thread_id = "thread_id" + str(threadID)
        thread_name = "thread_name" + str(threadID)
        thread = Consumer(thread_id, thread_name, workQueue)
        thread.setDaemon(True)
        thread.start()
        threads.append(thread)
        threadID += 1

    # produce
    thread = Producer("thread_id0", "thread_name0", workQueue)
    thread.setDaemon(True)
    thread.start()
    threads.append(thread)

    '''
    # 等待队列清空
    while not (workQueue.empty() and exitFlag == True):
        #print("--waiting--")
        time.sleep(2)
    '''
    # 等待所有线程完成
    for t in threads:
        t.join()
    print "Exiting Main Thread"