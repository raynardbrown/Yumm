package com.example.android.yumm.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable
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

  private Recipe(Parcel in)
  {
    id = in.readInt();
    name = in.readString();
    recipeIngredientList = in.createTypedArrayList(RecipeIngredient.CREATOR);
    recipeStepList = in.createTypedArrayList(RecipeStep.CREATOR);
    servings = in.readInt();
    imagePath = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeTypedList(recipeIngredientList);
    dest.writeTypedList(recipeStepList);
    dest.writeInt(servings);
    dest.writeString(imagePath);
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  public static final Creator<Recipe> CREATOR = new Creator<Recipe>()
  {
    @Override
    public Recipe createFromParcel(Parcel in)
    {
      return new Recipe(in);
    }

    @Override
    public Recipe[] newArray(int size)
    {
      return new Recipe[size];
    }
  };

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

  @Override
  public boolean equals(Object obj)
  {
    if(obj == this)
    {
      return true;
    }

    if(obj instanceof Recipe)
    {
      Recipe r = (Recipe)obj;

      if(this.recipeIngredientList.size() != r.recipeIngredientList.size())
      {
        return false;
      }

      if(this.recipeStepList.size() != r.recipeStepList.size())
      {
        return false;
      }

      for(int i = 0; i < this.recipeIngredientList.size(); ++i)
      {
        RecipeIngredient thisRecipeIngredient = this.recipeIngredientList.get(i);
        RecipeIngredient otherRecipeIngredient = r.recipeIngredientList.get(i);

        if(!thisRecipeIngredient.equals(otherRecipeIngredient))
        {
          return false;
        }
      }

      for(int i = 0; i < this.recipeStepList.size(); ++i)
      {
        RecipeStep thisRecipeStep = this.recipeStepList.get(i);
        RecipeStep otherRecipeStep = r.recipeStepList.get(i);

        if(!thisRecipeStep.equals(otherRecipeStep))
        {
          return false;
        }
      }

      return this.id == r.id &&
              this.name.equals(r.name) &&
              this.servings == r.servings &&
              this.imagePath.equals(r.imagePath);
    }
    else
    {
      // not a recipe object
      return false;
    }
  }
}
