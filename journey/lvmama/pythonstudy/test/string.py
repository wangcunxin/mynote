# -*- coding:utf-8 -*-
import urllib

if __name__ == '__main__':

    var1 = 'Hello World!'
    var2 = "Python Runoob"

    print "var1[0]: ", var1[0]
    print "var2[1:5]: ", var2[1:5]

    a = "Hello"
    b = "Python"

    print "a + b 输出结果：", a + b
    print "a * 2 输出结果：", a * 2
    print "a[1] 输出结果：", a[1]
    print "a[1:4] 输出结果：", a[1:4]

    if( "H" in a) :
        print "H 在变量 a 中"
    else :
        print "H 不在变量 a 中"

    if( "M" not in a) :
        print "M 不在变量 a 中"
    else :
        print "M 在变量 a 中"

    print r'\n'
    print R'\n'


    u = u"unicode"
    print u

    s = "string"
    print s


    u = u'汉'
    print repr(u) # u'\u6c49'
    print u
    s = u.encode('UTF-8')
    print repr(s) # '\xe6\xb1\x89'
    print s
    u2 = s.decode('UTF-8')
    print repr(u2) # u'\u6c49'
    print u2

    a = "[[1,2], [3,4], [5,6], [7,8], [9,0]]"
    b = eval(a)
    print b

    x1=u"米国"
    x2="中国".decode("utf-8")
    print repr(x1+x2)
    print x1+x2

    print urllib.unquote(str(x1)).decode("utf-8")