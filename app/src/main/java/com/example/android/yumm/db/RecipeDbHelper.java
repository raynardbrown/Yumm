package com.example.android.yumm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper
{
  private static final String DATABASE_NAME = "recipe.db";

  private static final int DATABASE_VERSION = 1;

  private static final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " +
          RecipeContract.RecipeColumns.TABLE_NAME + " (" +
          RecipeContract.RecipeColumns._ID + " INTEGER PRIMARY KEY, " +
          RecipeContract.RecipeColumns.RECIPE_NAME + " TEXT NOT NULL, " +
          RecipeContract.RecipeColumns.RECIPE_SERVINGS + " INTEGER, " +
          RecipeContract.RecipeColumns.RECIPE_IMAGE + " TEXT NOT NULL" +
          ")";

  private static final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " +
          RecipeContract.IngredientColumns.TABLE_NAME + " (" +
          RecipeContract.IngredientColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
          RecipeContract.IngredientColumns.INGREDIENT_NAME + " TEXT NOT NULL, " +
          RecipeContract.IngredientColumns.INGREDIENT_QUANTITY + " REAL, " +
          RecipeContract.IngredientColumns.INGREDIENT_MEASURE + " TEXT NOT NULL, " +
          RecipeContract.IngredientColumns.RECIPE_FOREIGN_KEY  + " INTEGER, " +
          "FOREIGN KEY(" + RecipeContract.IngredientColumns.RECIPE_FOREIGN_KEY   + ")" +
          " REFERENCES " + RecipeContract.RecipeColumns.TABLE_NAME + "(" + RecipeContract.RecipeColumns._ID  + ")" +
          " ON DELETE CASCADE" +
          ")";

  private static final String SQL_CREATE_STEP_TABLE = "CREATE TABLE " +
          RecipeContract.StepColumns.TABLE_NAME  + " (" +
          RecipeContract.StepColumns._ID + " INTEGER, " +
          RecipeContract.StepColumns.STEP_INDEX + " INTEGER, " +
          RecipeContract.StepColumns.STEP_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
          RecipeContract.StepColumns.STEP_DESCRIPTION + " TEXT NOT NULL, " +
          RecipeContract.StepColumns.STEP_VIDEO_URL + " TEXT NOT NULL, " +
          RecipeContract.StepColumns.STEP_THUMBNAIL_URL + " TEXT NOT NULL, " +
          RecipeContract.StepColumns.RECIPE_FOREIGN_KEY + " INTEGER, " +
          "FOREIGN KEY(" + RecipeContract.StepColumns.RECIPE_FOREIGN_KEY   + ")" +
          " REFERENCES " + RecipeContract.RecipeColumns.TABLE_NAME + "(" + RecipeContract.RecipeColumns._ID  + ")" +
          " ON DELETE CASCADE" +
          ")";

  public RecipeDbHelper(Context context)
  {
    super(context, RecipeDbHelper.DATABASE_NAME, null, RecipeDbHelper.DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db)
  {
    // Create all of the tables
    db.execSQL(RecipeDbHelper.SQL_CREATE_RECIPE_TABLE);
    db.execSQL(RecipeDbHelper.SQL_CREATE_INGREDIENT_TABLE);
    db.execSQL(RecipeDbHelper.SQL_CREATE_STEP_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeColumns.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.IngredientColumns.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.StepColumns.TABLE_NAME);
    onCreate(db);
  }
}
