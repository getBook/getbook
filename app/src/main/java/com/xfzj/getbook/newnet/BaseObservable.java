package com.xfzj.getbook.newnet;

import rx.Observable;

/**
 * Created by zj on 2016/6/24.
 */
public class BaseObservable extends Observable {
    /**
     * 
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p/>
     * <em>Note:</em> Use {@link #create(OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     *
     * @param f {@link OnSubscribe} to be executed when {@link #subscribe(Subscriber)} is called
     */
    protected BaseObservable(OnSubscribe f) {
        super(f);
    }
    
}
