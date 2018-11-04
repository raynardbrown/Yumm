package com.example.android.yumm.tasks;

public interface IAsyncTaskCompleteListener<T>
{
  public void onTaskComplete(T result);
}
