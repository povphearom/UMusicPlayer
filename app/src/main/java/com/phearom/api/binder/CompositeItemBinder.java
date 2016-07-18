package com.phearom.api.binder;

public class CompositeItemBinder<T> implements ItemBinder<T> {
    //Store multiple itemBinderBase
    private final ConditionalDataBinder<T>[] conditionalDataBinders;

    public CompositeItemBinder(ConditionalDataBinder<T>... conditionalDataBinders) {
        this.conditionalDataBinders = conditionalDataBinders;
    }

    @Override
    public int getLayoutRes(T model) {
        for (ConditionalDataBinder<T> binder : conditionalDataBinders) {
            ConditionalDataBinder<T> dataBinder = binder;
            if (dataBinder.canHandle(model)) {
                return dataBinder.getLayoutRes(model);
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public int getBindingVariable(T model) {
        for (ConditionalDataBinder<T> binder : conditionalDataBinders) {
            ConditionalDataBinder<T> dataBinder = binder;
            if (dataBinder.canHandle(model)) {
                return dataBinder.getBindingVariable(model);
            }
        }
        throw new IllegalStateException();
    }
}