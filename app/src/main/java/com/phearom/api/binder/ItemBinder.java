package com.phearom.api.binder;

public interface ItemBinder<T>
{
      //To get layoutId from Object(view model)
      int getLayoutRes(T model);
      //To get binding variable from Object(view model)
      int getBindingVariable(T model);
}