package com.example.android.yumm.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract between the recipe provider and applications.
 */
public final class RecipeContract
{
  private static final String URI_SCHEME = "content://";

  /**
   * The authority for the recipe provider.
   */
  public static final String URI_CONTENT_AUTHORITY = "com.example.android.yumm";

  private static final Uri BASE_URL = Uri.parse(RecipeContract.URI_SCHEME + RecipeContract.URI_CONTENT_AUTHORITY);

  private RecipeContract()
  {

  }

  /**
   *
   * <pre>
   * +----+-----------------+--------+
   * |    |     recipe      |        |
   * +----+-----------------+--------+
   * | PK | id              | int    |
   * |    | recipe_name     | string |
   * |    | recipe_servings | int    |
   * |    | recipe_image    | string |
   * +----+-----------------+--------+
   * </pre>
   *
   */
  public static final class RecipeColumns implements BaseColumns
  {
    private RecipeColumns()
    {

    }

    public static final String TABLE_NAME = "recipe";

    /**
     * The name of the recipe.
     *
     * <p>Type: TEXT</p>
     */
    public static final String RECIPE_NAME = "recipe_name";

    /**
     * The number of servings in this recipe.
     *
     * <p>Type: INTEGER</p>
     */
    public static final String RECIPE_SERVINGS = "recipe_servings";

    /**
     * A picture of the recipe.
     *
     * <p>Type: TEXT</p>
     */
    public static final String RECIPE_IMAGE = "recipe_image";

    /**
     * The content:// style URI for this table.
     */
    public static final Uri CONTENT_URI = RecipeContract.BASE_URL.buildUpon().appendPath(RecipeColumns.TABLE_NAME).build();
  }

  /**
   *
   * <pre>
   * +----+---------------------+----------+
   * |    |     ingredient      |          |
   * +----+---------------------+----------+
   * | PK | id                  | int auto |
   * |    | ingredient_name     | string   |
   * |    | ingredient_quantity | real     |
   * |    | ingredient_measure  | string   |
   * | FK | recipe_id           | int      |
   * +----+---------------------+----------+
   * </pre>
   *
   */
  public static final class IngredientColumns implements BaseColumns
  {
    private IngredientColumns()
    {

    }

    public static final String TABLE_NAME = "ingredient";

    /**
     * The name of the ingredient.
     *
     * <p>Type: TEXT</p>
     */
    public static final String INGREDIENT_NAME = "ingredient_name";

    /**
     * The amount of the ingredient in the recipe.
     *
     * <p>Type: REAL</p>
     */
    public static final String INGREDIENT_QUANTITY = "ingredient_quantity";

    /**
     * The measure of the ingredient.
     *
     * <p>Type: TEXT</p>
     */
    public static final String INGREDIENT_MEASURE = "ingredient_measure";

    /**
     * The recipe id that links this row to a row in the recipe table.
     *
     * <p>Type: INTEGER</p>
     */
    public static final String RECIPE_FOREIGN_KEY = "recipe_id";

    /**
     * The content:// style URI for this table.
     */
    public static final Uri CONTENT_URI = RecipeContract.BASE_URL.buildUpon().appendPath(IngredientColumns.TABLE_NAME).build();
  }

  /**
   *
   * <pre>
   * +----+------------------------+----------+
   * |    |          step          |          |
   * +----+------------------------+----------+
   * |    | id                     | int      |
   * |    | step_index             | int      |
   * |    | step_short_description | string   |
   * |    | step_description       | string   |
   * |    | step_video_url         | string   |
   * |    | step_thumbnail_url     | string   |
   * | FK | recipe_id              | int      |
   * +----+------------------------+----------+
   * </pre>
   *
   */
  public static final class StepColumns implements BaseColumns
  {
    private StepColumns()
    {

    }

    public static final String TABLE_NAME = "step";

    /**
     * The sequence of this step in the collection of steps in a recipe.
     *
     * <p>Type: INTEGER</p>
     */
    public static final String STEP_INDEX = "step_index";

    /**
     * Short description of the recipe step.
     *
     * <p>Type: TEXT</p>
     */
    public static final String STEP_SHORT_DESCRIPTION = "step_short_description";

    /**
     * Detailed description of the recipe step.
     *
     * <p>Type: TEXT</p>
     */
    public static final String STEP_DESCRIPTION = "step_description";

    /**
     * Path to a video of a this recipe step.
     *
     * <p>Type: TEXT</p>
     */
    public static final String STEP_VIDEO_URL = "step_video_url";

    /**
     * Path to a thumbnail of this recipe step.
     */
    public static final String STEP_THUMBNAIL_URL = "step_thumbnail_url";

    /**
     * The recipe id that links this row to a row in the recipe table.
     *
     * <p>Type: INTEGER</p>
     */
    public static final String RECIPE_FOREIGN_KEY = "recipe_id";

    /**
     * The content:// style URI for this table.
     */
    public static final Uri CONTENT_URI = RecipeContract.BASE_URL.buildUpon().appendPath(StepColumns.TABLE_NAME).build();
  }
}
