package com.example.android.yumm.view;

import com.example.android.yumm.model.Recipe;

public interface IRecipeCardDetailListItemClickListener
{
  /**
   * Clicker handler for items clicked with in the master list. The first item i.e. index 0 is the
   * ingredients. All subsequent indexes are the items within the recipe step list of the specified
   * recipe.
   *
   * @param recipe the Recipe object associated with the master list.
   *
   * @param clickIndex the item within the master list that was clicked.
   */
  public void onRecipeCardDetailListItemClick(Recipe recipe, int clickIndex);
}
