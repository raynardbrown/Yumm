package com.example.android.yumm.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.yumm.db.RecipeContract;
import com.example.android.yumm.db.RecipeDbHelper;

public class RecipeContentProvider extends ContentProvider
{
  private RecipeDbHelper recipeDbHelper;

  public static final int RECIPE = 100;
  public static final int RECIPE_WITH_ID = 101;

  public static final int INGREDIENT = 200;
  public static final int INGREDIENT_WITH_ID = 201;

  public static final int STEP = 300;
  public static final int STEP_WITH_ID = 301;

  private static final UriMatcher uriMatcher = buildUriMatcher();

  private static UriMatcher buildUriMatcher()
  {
    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    uriMatcher.addURI(RecipeContract.URI_CONTENT_AUTHORITY, RecipeContract.RecipeColumns.TABLE_NAME, RecipeContentProvider.RECIPE);

    // Single row in the recipe database
    uriMatcher.addURI(RecipeContract.URI_CONTENT_AUTHORITY, RecipeContract.RecipeColumns.TABLE_NAME + "/#", RecipeContentProvider.RECIPE_WITH_ID);

    uriMatcher.addURI(RecipeContract.URI_CONTENT_AUTHORITY, RecipeContract.IngredientColumns.TABLE_NAME, RecipeContentProvider.INGREDIENT);

    // Single row in the ingredient database
    uriMatcher.addURI(RecipeContract.URI_CONTENT_AUTHORITY, RecipeContract.IngredientColumns.TABLE_NAME + "/#", RecipeContentProvider.INGREDIENT_WITH_ID);

    uriMatcher.addURI(RecipeContract.URI_CONTENT_AUTHORITY, RecipeContract.StepColumns.TABLE_NAME, RecipeContentProvider.STEP);

    // Single row in the step database
    uriMatcher.addURI(RecipeContract.URI_CONTENT_AUTHORITY, RecipeContract.StepColumns.TABLE_NAME + "/#", RecipeContentProvider.STEP_WITH_ID);

    return uriMatcher;
  }

  @Override
  public boolean onCreate()
  {
    Context context = getContext();

    recipeDbHelper = new RecipeDbHelper(context);

    return true;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                      @Nullable String[] selectionArgs, @Nullable String sortOrder)
  {
    final SQLiteDatabase db = recipeDbHelper.getReadableDatabase();

    int match = uriMatcher.match(uri);

    Cursor returnCursor = null;

    switch(match)
    {
      case RecipeContentProvider.RECIPE:
      {
        // return all the rows in the database.
        returnCursor = db.query(RecipeContract.RecipeColumns.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        break;
      }

      case RecipeContentProvider.RECIPE_WITH_ID:
      {
        String id = uri.getPathSegments().get(1);

        String tempSelection = RecipeContract.RecipeColumns._ID + "=?"; // column name
        String[] tempSelectionArgs = new String[]{id}; // column value

        returnCursor = db.query(RecipeContract.RecipeColumns.TABLE_NAME,
                projection,
                tempSelection,
                tempSelectionArgs,
                null,
                null,
                sortOrder);

        break;
      }

      case RecipeContentProvider.INGREDIENT:
      {
        // return all the rows in the database.
        returnCursor = db.query(RecipeContract.IngredientColumns.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        break;
      }

      case RecipeContentProvider.INGREDIENT_WITH_ID:
      {
        String id = uri.getPathSegments().get(1);

        // all the ingredients that match the recipe foreign key
        String tempSelection = RecipeContract.IngredientColumns.RECIPE_FOREIGN_KEY + "=?"; // column name
        String[] tempSelectionArgs = new String[]{id}; // column value

        returnCursor = db.query(RecipeContract.IngredientColumns.TABLE_NAME,
                projection,
                tempSelection,
                tempSelectionArgs,
                null,
                null,
                sortOrder);

        break;
      }

      case RecipeContentProvider.STEP:
      {
        // return all the rows in the database.
        returnCursor = db.query(RecipeContract.StepColumns.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        break;
      }

      case RecipeContentProvider.STEP_WITH_ID:
      {
        String id = uri.getPathSegments().get(1);

        // all the steps that match the recipe foreign key
        String tempSelection = RecipeContract.StepColumns.RECIPE_FOREIGN_KEY + "=?"; // column name
        String[] tempSelectionArgs = new String[]{id}; // column value

        returnCursor = db.query(RecipeContract.StepColumns.TABLE_NAME,
                projection,
                tempSelection,
                tempSelectionArgs,
                null,
                null,
                sortOrder);

        break;
      }

      default:
      {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }

    if(getContext() != null)
    {
      // tell the cursor what content uri the cursor was created for.
      returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
    }

    return returnCursor;
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri)
  {
    // We are not using getType in this app
    throw new UnsupportedOperationException("Unknown uri: " + uri);
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
  {
    final SQLiteDatabase db = recipeDbHelper.getWritableDatabase();

    int match = uriMatcher.match(uri);

    Uri returnUri = null;

    switch(match)
    {
      case RecipeContentProvider.RECIPE:
      {
        long newInsertedRowId = db.insert(RecipeContract.RecipeColumns.TABLE_NAME, null, values);

        if(newInsertedRowId != -1)
        {
          returnUri = ContentUris.withAppendedId(RecipeContract.RecipeColumns.CONTENT_URI, newInsertedRowId);
        }
        else
        {
          throw new SQLiteException("Failed to insert row into " + uri);
        }

        break;
      }

      case RecipeContentProvider.INGREDIENT:
      {
        long newInsertedRowId = db.insert(RecipeContract.IngredientColumns.TABLE_NAME, null, values);

        if(newInsertedRowId != -1)
        {
          returnUri = ContentUris.withAppendedId(RecipeContract.IngredientColumns.CONTENT_URI, newInsertedRowId);
        }
        else
        {
          throw new SQLiteException("Failed to insert row into " + uri);
        }

        break;
      }

      case RecipeContentProvider.STEP:
      {
        long newInsertedRowId = db.insert(RecipeContract.StepColumns.TABLE_NAME, null, values);

        if(newInsertedRowId != -1)
        {
          returnUri = ContentUris.withAppendedId(RecipeContract.StepColumns.CONTENT_URI, newInsertedRowId);
        }
        else
        {
          throw new SQLiteException("Failed to insert row into " + uri);
        }

        break;
      }

      default:
      {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }

    // notify the content resolver that the uri has changed
    if(getContext() != null)
    {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return returnUri;
  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
  {
    // We are not using delete in this app
    throw new UnsupportedOperationException("Unknown uri: " + uri);
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                    @Nullable String[] selectionArgs)
  {
    // We are not using update in this app
    throw new UnsupportedOperationException("Unknown uri: " + uri);
  }
}
