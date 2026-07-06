package com.smartledger.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 智能极简记账本 Application 入口。
 * 使用 Hilt 进行依赖注入。
 */
@HiltAndroidApp
class SmartLedgerApp : Application()
