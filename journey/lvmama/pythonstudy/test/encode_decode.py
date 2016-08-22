# -*- coding:utf-8 -*-
import urllib

if __name__ == '__main__':
    u = u'汉'
    print repr(u),u # u'\u6c49'
    s = u.encode('UTF-8')
    print repr(s),s # '\xe6\xb1\x89'
    u2 = s.decode('UTF-8')
    print repr(u2),u2 # u'\u6c49'

    x1=u"米国"
    x2="中国".decode("utf-8")
    x3="中国"
    print repr(x1+x2),x1+x2
    #print repr(x1+x3),x1+x3


    y3="日本"
    y3_1=urllib.quote(y3)
    print repr(y3_1),y3_1
    y3_2=urllib.unquote(y3_1)
    print repr(y3_2),y3_2
    print str(y3_2)