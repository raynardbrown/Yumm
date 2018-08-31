package com.example.android.yumm.model;

public class RecipeIngredient
{
  private int quantity;

  private IngredientMeasure measure;

  private String name;

  public RecipeIngredient(int quantity,
                          IngredientMeasure measure,
                          String name)
  {
    this.quantity = quantity;
    this.measure = measure;
    this.name = name;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public void setQuantity(int quantity)
  {
    this.quantity = quantity;
  }

  public IngredientMeasure getMeasure()
  {
    return measure;
  }

  public void setMeasure(IngredientMeasure measure)
  {
    this.measure = measure;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
