package com.non_name_hero.calenderview.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final int THREAD_COUNT = 3;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    /**
     * コンストラクタ(オーバーロード)
     *
     * @param diskIO ローカルで使用するスレッドを管理するExecutor
     * @param networkIO ローカルで使用するスレッドを管理するExecutor
     * @param mainThread 主にUI処理を行うメインスレッドを管理するExecutor
     */
    @VisibleForTesting
    AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(
                new DiskIOThreadExecutor(), //ローカル用のスレッドを管理するExecutorをインスタンス化
                Executors.newFixedThreadPool(THREAD_COUNT), //ここでは3つのスレッドを作成して使い回す
                new MainThreadExecutor() //UI用のスレッドを管理するExecutorをインスタンス化
        );
    }

    /**
     * 処理を実行するスレッドのExecutorを返却
     * 使用するときは 任意のExecutor().execute(new Runnable {run(){行いたい処理}})
     * @return Executor
     */
    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    /**
     * Executorsからスレッドを作成するのではなく、現在のメインスレッドを利用するため、Handlerを作成してlooperを登録する
     * 1.スレッドを作成する必要がないため
     * 2.他の全てのRunnableタスクと同期的に実行されるので管理しやすい
     * 3.メインハンドラでは
     * https://academy.realm.io/jp/posts/android-thread-looper-handler/
     * */
    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            //処理をハンドラ(looper)に投稿する
            mainThreadHandler.post(command);
        }
    }
}
