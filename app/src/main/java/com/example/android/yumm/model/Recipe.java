package com.example.android.yumm.model;

import java.util.List;

public class Recipe
{
  private int id;

  private String name;

  private List<RecipeIngredient> recipeIngredientList;

  private List<RecipeStep> recipeStepList;

  private int servings;

  private String imagePath;

  public Recipe(int id,
                String name,
                List<RecipeIngredient> recipeIngredientList,
                List<RecipeStep> recipeStepList,
                int servings,
                String imagePath)
  {
    this.id = id;
    this.name = name;
    this.recipeIngredientList = recipeIngredientList;
    this.recipeStepList = recipeStepList;
    this.servings = servings;
    this.imagePath = imagePath;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public List<RecipeIngredient> getRecipeIngredients()
  {
    return recipeIngredientList;
  }

  public void setRecipeIngredients(List<RecipeIngredient> recipeIngredientList)
  {
    this.recipeIngredientList = recipeIngredientList;
  }

  public List<RecipeStep> getRecipeSteps()
  {
    return recipeStepList;
  }

  public void setRecipeSteps(List<RecipeStep> recipeStepList)
  {
    this.recipeStepList = recipeStepList;
  }

  public int getServings()
  {
    return servings;
  }

  public void setServings(int servings)
  {
    this.servings = servings;
  }

  public String getImagePath()
  {
    return imagePath;
  }

  public void setImagePath(String imagePath)
  {
    this.imagePath = imagePath;
  }
}
