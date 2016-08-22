#!/usr/bin/python
# -*- coding: UTF-8 -*-

class Parent:        # 定义父类
   parentAttr = 100
   def __init__(self):
      print "调用父类构造函数1"

   def parentMethod1(self):
      print '调用父类方法1'

   def setAttr(self, attr):
      Parent.parentAttr = attr

   def getAttr(self):
      print "父类属性 :", Parent.parentAttr

class Parent2:        # 定义父类
   parentAttr = 100
   def __init__(self):
      print "调用父类构造函数2"

   def parentMethod2(self):
      print '调用父类方法2'

   def setAttr(self, attr):
      Parent.parentAttr = attr

   def getAttr(self):
      print "父类属性 :", Parent.parentAttr

class Child(Parent): # 定义子类
   def __init__(self):
      print "调用子类构造方法"

   def childMethod1(self):
      print '调用子类方法 child method'

class Child2(Parent,Parent2): # 定义子类

   def __init__(self):
      Parent.__init__(self)
      Parent2.__init__(self)
      #super(Child2,self).__init__()
      print "调用子类构造方法"


   def childMethod2(self):
      print '调用子类方法 child method'


if __name__ == '__main__':

    c = Child()          # 实例化子类
    c.childMethod1()      # 调用子类的方法
    c.parentMethod1()     # 调用父类方法
    c.setAttr(200)       # 再次调用父类的方法
    c.getAttr()          # 再次调用父类的方法
    ret = isinstance(c,Parent2)
    print(ret)

    print("-------------")

    c2 = Child2()          # 实例化子类
    c2.childMethod2()      # 调用子类的方法
    c2.parentMethod1()     # 调用父类方法
    c2.parentMethod2()     # 调用父类方法
    c2.setAttr(200)       # 再次调用父类的方法
    c2.getAttr()          # 再次调用父类的方法

    ret = isinstance(c2,Parent2)
    print(ret)

    ret = cmp(c2,c)
    print(ret)

    print(str(c))

