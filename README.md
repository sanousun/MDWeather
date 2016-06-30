MDWeather
========
## 前言
灵感来源于materialup上的一些天气应用设计，
刚好那时候想着试试retrofit+rxjava的组合，
也就开始准着手这个应用的开发。
## 介绍
一个基于material design的天气app<br/>
网络请求采用retrofit 2.0 + rxjava + eventbus，
也使用了三个自定义的view用于展示日出日落，
七天天气预报，空气质量。
##计划
最近一次上传添加了本地天气的获取，
1. 采用了高德地图的sdk。<br/>
2. 将主启动界面更换成了本地天气界面<br/>
3. 应用初步完成<br/>
4. 去除eventBus,如果需要可以使用rxBus<br/>
5. 界面更改<br/>
## 数据来源
[天气查询](http://apistore.baidu.com/apiworks/servicedetail/112.html "百度的api store")  百度的api store中的一个关于天气的api
## 截图
![](https://github.com/sanousun/MDWeather/blob/master/screenshot/screenshot.jpg)
### 使用方法
[高德开放平台](http://lbs.amap.com/)申请key填入manifests中
### 这是我的邮箱，欢迎联系交流
sanousun@163.com
