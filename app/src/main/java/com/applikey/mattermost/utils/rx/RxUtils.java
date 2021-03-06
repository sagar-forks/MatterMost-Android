package com.applikey.mattermost.utils.rx;

import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxUtils {

    public static <T> Observable.Transformer<T, T> applySchedulersAndRetry() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay.getDefaultInstance())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Execute action on UI thread, then switch observable to another thread
     *
     * @param action Action, which is performed on UI thread
     * @param scheduler Switch scheduler
     * @param <T> Type of action return result
     *
     * @return
     */
    public static <T> Observable.Transformer<T, T> doOnUi(Action1<T> action, Scheduler scheduler) {
        return tObservable -> tObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(action)
                .observeOn(scheduler);
    }

    /**
     * Execute action on UI thread, then switch observable to IO thread
     *
     * @param action Action, which is performed on UI thread
     * @param <T> Type of action return result
     *
     * @return
     */
    public static <T> Observable.Transformer<T, T> doOnUi(Action1<T> action) {
        return tObservable -> tObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(action)
                .observeOn(Schedulers.io());
    }

    /**
     * Execute two actions on UI thread
     * Use STRICTLY before .subscribe
     */
    public static <T> Observable.Transformer<T, T> applyProgress(Action0 beforeRun, Action0 afterRun) {
        return tObservable -> tObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(beforeRun)
                .doOnTerminate(afterRun);
    }

    /**
     * Execute two actions on UI thread
     * Use STRICTLY before .subscribe
     */
    public static <T> Single.Transformer<T, T> applyProgressSingle(Action0 beforeRun, Action0 afterRun) {
        return tObservable -> tObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(beforeRun)
                .doAfterTerminate(afterRun);
    }

}
