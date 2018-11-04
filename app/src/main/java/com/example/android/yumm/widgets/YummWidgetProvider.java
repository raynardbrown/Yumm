package com.example.android.yumm.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.yumm.R;
import com.example.android.yumm.view.RecipeCardIngredientActivity;

/**
 * Implementation of App Widget functionality.
 */
public class YummWidgetProvider extends AppWidgetProvider
{
  static void updateYummWidget(Context context, AppWidgetManager appWidgetManager,
                               int appWidgetId)
  {
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yumm_widget_provider);

    Intent intent = new Intent(context, YummGridWidgetService.class);
    views.setRemoteAdapter(R.id.gv_recipe_widget, intent);

    Intent ingredientIntent = new Intent(context, RecipeCardIngredientActivity.class);
    PendingIntent ingredientPendingIntent = PendingIntent.getActivity(context, 0, ingredientIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    views.setPendingIntentTemplate(R.id.gv_recipe_widget, ingredientPendingIntent);

    views.setEmptyView(R.id.gv_recipe_widget, R.id.empty_widget);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
  {
    // There may be multiple widgets active, so update all of them

    // This function is called the first time the widget is added to the home screen.
    for(int appWidgetId : appWidgetIds)
    {
      YummWidgetProvider.updateYummWidget(context, appWidgetManager, appWidgetId);
    }
  }

  public static void updateYummWidgets(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetIds)
  {
    for(int appWidgetId : appWidgetIds)
    {
      YummWidgetProvider.updateYummWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context)
  {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context)
  {
    // Enter relevant functionality for when the last widget is disabled
  }
}

