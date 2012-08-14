package com.mini_colombia.servicios;

public interface AsyncTaskListener<T> 
{
	 public void onTaskComplete(T result);
}
