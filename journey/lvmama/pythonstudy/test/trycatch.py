# -*- coding: UTF-8 -*-

if __name__ == '__main__':

    try:
        print("xxx")
        1/0
    except TabError,e1:
        print(e1)
    except Exception,e2:
        print(e2)
    except:
        print("other exception")
    else:
        print("...")
    finally:
        print "finally"


    try:
        fh = open("testfile", "w")
        try:
            fh.write("这是一个测试文件，用于测试异常!!")
        finally:
            print "关闭文件"
            fh.close()
    except IOError:
        print "Error: 没有找到文件或读取文件失败"
        raise Exception("eeeeeeeeeeeee")











