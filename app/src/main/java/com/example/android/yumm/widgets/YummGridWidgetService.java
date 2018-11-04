package com.example.android.yumm.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class YummGridWidgetService extends RemoteViewsService
{
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent)
  {
    return new YummWidgetGridRemoteViewsFactory(getApplicationContext());
  }
}
