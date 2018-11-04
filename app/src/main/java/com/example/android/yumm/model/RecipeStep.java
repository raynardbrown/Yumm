package com.example.android.yumm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeStep implements Parcelable
{
  private int id;

  private String shortDescription;

  private String description;

  private String videoUrl;

  private String thumbnailUrl;

  public RecipeStep(int id,
                    String shortDescription,
                    String description,
                    String videoUrl,
                    String thumbnailUrl)
  {
    this.id = id;
    this.shortDescription = shortDescription;
    this.description = description;
    this.videoUrl = videoUrl;
    this.thumbnailUrl = thumbnailUrl;
  }

  protected RecipeStep(Parcel in)
  {
    id = in.readInt();
    shortDescription = in.readString();
    description = in.readString();
    videoUrl = in.readString();
    thumbnailUrl = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeInt(id);
    dest.writeString(shortDescription);
    dest.writeString(description);
    dest.writeString(videoUrl);
    dest.writeString(thumbnailUrl);
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>()
  {
    @Override
    public RecipeStep createFromParcel(Parcel in)
    {
      return new RecipeStep(in);
    }

    @Override
    public RecipeStep[] newArray(int size)
    {
      return new RecipeStep[size];
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

  public String getShortDescription()
  {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription)
  {
    this.shortDescription = shortDescription;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl)
  {
    this.videoUrl = videoUrl;
  }

  public String getThumbnailUrl()
  {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl)
  {
    this.thumbnailUrl = thumbnailUrl;
  }

  @Override
  public boolean equals(Object obj)
  {
    if(obj == this)
    {
      return true;
    }

    if(obj instanceof RecipeStep)
    {
      RecipeStep otherRecipeStep = (RecipeStep)obj;

      return this.id == otherRecipeStep.id &&
              this.shortDescription.equals(otherRecipeStep.shortDescription) &&
              this.description.equals(otherRecipeStep.description) &&
              this.videoUrl.equals(otherRecipeStep.videoUrl) &&
              this.thumbnailUrl.equals(otherRecipeStep.thumbnailUrl);
    }
    else
    {
      return false;
    }
  }
}
