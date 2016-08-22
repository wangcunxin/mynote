# -*- coding:utf-8 -*-
import urllib2

if __name__ == '__main__':

    url = "http://sports.sina.com.cn/g/premierleague/index.shtml"
    response = urllib2.urlopen(url)
    html = response.read()
    print html
    '''
    url = "http://sports.sina.com.cn/g/premierleague/index.shtml"
    request = urllib2.Request(url)
    request.add_header('Accept-encoding', 'gzip')
    opener = urllib2.build_opener()
    response = opener.open(request)html = response.read()
    gzipped = response.headers.get('Content-Encoding')
    if gzipped:
        html = zlib.decompress(html, 16+zlib.MAX_WBITS)
    print html
    '''