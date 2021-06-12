package com.non_name_hero.calenderview.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * バックグラウンドで処理するためのスレッドを管理するExecutorを実装するクラス
 *
 */
class DiskIOThreadExecutor : Executor {
    private val mDiskIO: Executor

    /**
     * 引数のRunnableをI/Oスレッドで実行する
     * @param command 実行したい処理内容
     */
    override fun execute(command: Runnable) {
        mDiskIO.execute(command)
    }

    //コンストラクタ
    //一つのスレッドを管理するExecutorを返却
    init {
        mDiskIO = Executors.newSingleThreadExecutor()
    }
}