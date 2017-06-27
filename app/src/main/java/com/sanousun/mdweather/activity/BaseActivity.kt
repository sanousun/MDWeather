package com.sanousun.mdweather.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import rx.subscriptions.CompositeSubscription

/**
 * Created by dashu on 2017/6/25.
 * 基类的Activity
 */

abstract class BaseActivity : AppCompatActivity() {

    val subscriptions: CompositeSubscription = CompositeSubscription()

    /**
     * 获取布局文件id
     */
    abstract fun setLayoutId(): Int

    /**
     * 初始化数据，比如说处理intent
     */
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 初始化view
     */
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())
        initData(savedInstanceState)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}