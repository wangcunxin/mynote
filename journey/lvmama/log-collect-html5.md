
功能：获取H5页面相关参数,通过ajax,请求1*1的图片,将参数发送到后端

 URL示例 ：http://rh.lvmama.com/1.gif?rf=http%3A%2F%2Fm.lvmama.com%2F&ul=http%3A%2F%2Fzt1.lvmama.com%2Ftemplate3%2Findex3%2F1074&an=%E3%80%90%E9%A9&al=xxxx&udid=token&ts=1458712337861&pc=baidu
 Http请求方式：get
 是否需要登录：否
 是否支持Https：否
 
 页面js分为两个事件,一个是进入页面事件,一个是点击事件. 进入页面需要的参数:rf,ul,udid,lsd,pc(如果有). 点击事件所需参数:1 如果点击的专题,ul,udid,lsd,an,al

                2 如果点击的是产品,ul,udid,lsd,an
参数:

参数	必选	范围及类型	说明
rf	true	String	(reference)页面来源
ul	true	String	(url)当前页面
udid	true	String	设备唯一标识
lsd	true	String	lvsessionid
ts	true	Long	(timestamp)参数发送的时间戳毫秒数
pc	false	String	(promotionChannel)推广渠道
pi	true	String	(productId)产品id
an	true	String	(activityName)专题名称
al	true	String	(activityLocation)专题位置
 