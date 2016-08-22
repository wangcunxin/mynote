
来自客户端

 功能：发送用户访问页面的日志到服务端
 URL ：http://10.200.2.103/rhino/log/app
 Http请求方式：POST
 是否需要登录：否
 是否支持Https：否

请求参数:

参数	必选	范围及类型	说明
av	true	String	(appVersion)客户端版本号
dt	true	String	(deviceToken)设备号 iPhone传token,安卓传imei
mm	true	String	(mobileModel)客户端机型
ll	true	List	(logList)访问日志列表
logList参数:

参数	必选	范围及类型	说明
nt	true	String	(networkType)设备使用的网络类型
ts	true	Long	(timestamp)日志记录的时间戳毫秒数
pc	true	String	(pageCode)页面代码
cm	false	Map	(customMap)自定义参数键值对
请求报文示例:

"data" : Gzip.compress(

{

   "av": "7.6.0",
   "dt": "450739940288853",    
   "mm": "Nexus 5",
   "ll": [{
       "nt": "46001",
       "pc": "INDEX",
       "ts": 1457938524771
   },
   {
       "cm": {
           "destId": "79"
       },
       "nt": "46001",
       "pc": "CHANNEL",
       "ts": 1457938528732
   },
   {
       "cm": {
           "productId": "120044"
       },
       "nt": "WIFI",
       "pc": "DETAIL",
       "ts": 1457938533251
   }
}

)

请求参数JSONbase64加密，使用Gzip压缩, 把Gzip压缩产生的String作为value传过来.

HEADERS

 Content-Type=application/x-www-form-urlencoded
BODY

 data=Gzip.compress(JSONString.getBytes("UTF-8"))
返回结果:

   成功调用后台接口后，会返回 200 状态码