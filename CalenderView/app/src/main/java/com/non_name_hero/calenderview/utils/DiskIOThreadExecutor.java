package com.non_name_hero.calenderview.utils;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * バックグラウンドで処理するためのスレッドを管理するExecutorを実装するクラス
 *
 */
public class DiskIOThreadExecutor implements Executor {

    private final Executor mDiskIO;

    //コンストラクタ
    //一つのスレッドを管理するExecutorを返却
    public DiskIOThreadExecutor() {
        mDiskIO = Executors.newSingleThreadExecutor();
    }

    /**
     * 引数のRunnableをI/Oスレッドで実行する
     * @param command 実行したい処理内容
     */
    @Override
    public void execute(@NonNull Runnable command) {
        mDiskIO.execute(command);
    }
}
