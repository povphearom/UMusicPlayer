package com.phearom.api.binder;

public class ItemBinderBase<T> implements ItemBinder<T>
{
    protected final int bindingVariable;
    protected final int layoutId;

    public ItemBinderBase(int bindingVariable, int layoutId)
    {
        this.bindingVariable = bindingVariable;
        this.layoutId = layoutId;
    }

    //Override get layoutId from ItemBinder
    public int getLayoutRes(T model)
    {
        return layoutId;
    }

    //Override get binding variable from ItemBinder
    public int getBindingVariable(T model)
    {
        return bindingVariable;
    }
}