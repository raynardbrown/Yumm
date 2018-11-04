package com.example.android.yumm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeIngredient implements Parcelable
{
  private double quantity;

  private String measure;

  private String name;

  public RecipeIngredient(double quantity,
                          String measure,
                          String name)
  {
    this.quantity = quantity;
    this.measure = measure;
    this.name = name;
  }

  protected RecipeIngredient(Parcel in)
  {
    quantity = in.readDouble();
    measure = in.readString();
    name = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeDouble(quantity);
    dest.writeString(measure);
    dest.writeString(name);
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>()
  {
    @Override
    public RecipeIngredient createFromParcel(Parcel in)
    {
      return new RecipeIngredient(in);
    }

    @Override
    public RecipeIngredient[] newArray(int size)
    {
      return new RecipeIngredient[size];
    }
  };

  public double getQuantity()
  {
    return quantity;
  }

  public void setQuantity(double quantity)
  {
    this.quantity = quantity;
  }

  public String getMeasure()
  {
    return measure;
  }

  public void setMeasure(String measure)
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

  @Override
  public boolean equals(Object obj)
  {
    if(obj == this)
    {
      return true;
    }

    if(obj instanceof RecipeIngredient)
    {
      RecipeIngredient ingredient = (RecipeIngredient)obj;

      return this.name.equals(ingredient.name) &&
              this.measure.equals(ingredient.measure) &&
              (Double.compare(this.quantity, ingredient.quantity) == 0);
    }
    else
    {
      return false;
    }
  }
}
