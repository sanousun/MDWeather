MDWeather
========
# 注意
需要将 gradle.properties 中加上自己申请的高德地图的key和和风天气的key
## UI方面
前段时间找到了一个非常棒的开源天气app
[几何天气](https://github.com/WangDaYeeeeee/GeometricWeather)
所以UI方面打算用他的方案了，类似于一加天气，我把他的view实现放在单独的module-weather_bg里，再看情况实现以下自己的动画效果

## 前言
最初的灵感来源于 materialup 上的一些天气应用设计，刚好那时候想着试试 retrofit + rxjava 的组合，于是这个应用的开发
一年后，百度的 API 已经停用了，所以换成了和风天气的API，但是数据量却减少很多，本人也想试试 Kotlin 项目，于是开始动手改造项目

## 介绍
一个基于 material design 的天气 app<br/>
网络请求采用retrofit 2.0 + rxjava

## 计划
1. 应用初步完成<br/>
2. 高德定位接入<br/>
3. 界面UI待美化<br/>

## 数据来源
和风天气