package com.example.android.yumm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkBroadcastReceiver extends BroadcastReceiver
{
  private INetworkBroadcastReceiverListener networkBroadcastReceiverListener;

  public NetworkBroadcastReceiver(INetworkBroadcastReceiverListener networkBroadcastReceiverListener)
  {
    this.networkBroadcastReceiverListener = networkBroadcastReceiverListener;
  }

  @Override
  public void onReceive(Context context, Intent intent)
  {
    String action = intent.getAction();

    if(action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
    {
      if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false))
      {
        // We have no connectivity
        networkBroadcastReceiverListener.onNetworkDisconnected();
      }
      else
      {
        // We have connectivity
        networkBroadcastReceiverListener.onNetworkConnected();
      }
    }
  }
}
