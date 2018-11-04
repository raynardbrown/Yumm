package com.example.android.yumm.view;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.yumm.R;
import com.example.android.yumm.databinding.FragmentRecipeCardDetailStepBinding;
import com.example.android.yumm.model.RecipeStep;
import com.example.android.yumm.utils.YummConstants;
import com.example.android.yumm.utils.YummUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RecipeCardDetailStepFragment extends Fragment
{
  private RecipeStep recipeStep;
  private SimpleExoPlayer simpleExoPlayer;

  private FragmentRecipeCardDetailStepBinding fragmentRecipeCardDetailStepBinding;

  private IRecipeCardDetailStepPreviousClickListener recipeCardDetailStepPreviousClickListener;
  private IRecipeCardDetailStepNextClickListener recipeCardDetailStepNextClickListener;

  private boolean previousButtonEnabled;
  private boolean nextButtonEnabled;

  private int orientation;

  private int layoutSizeHint;

  private int playerWindow;
  private long playerPosition;

  private boolean videoAvailable;
  private boolean imageAvailable;

  public RecipeCardDetailStepFragment()
  {

  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);

    if(!YummUtils.isTabletSizedDevice(context))
    {
      // previous and next buttons are only on phone sized devices
      try
      {
        recipeCardDetailStepPreviousClickListener = (IRecipeCardDetailStepPreviousClickListener) context;
        recipeCardDetailStepNextClickListener = (IRecipeCardDetailStepNextClickListener) context;
      } catch (ClassCastException e)
      {
        throw new ClassCastException(context.toString() + " must implement IRecipeCardDetailStepPreviousClickListener and IRecipeCardDetailStepNextClickListener interface");
      }
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    fragmentRecipeCardDetailStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_card_detail_step, container, false);

    if(savedInstanceState != null)
    {
      recipeStep = savedInstanceState.getParcelable(getString(R.string.recipe_step_type_key));

      if(YummUtils.isTabletSizedDevice(getContext()))
      {
        layoutSizeHint = YummConstants.TABLET_SIZED_DEVICE;
      }
      else
      {
        layoutSizeHint = YummConstants.PHONE_SIZED_DEVICE;
      }

      orientation = getResources().getConfiguration().orientation;

      stopVideoIfRunning();

      playerWindow = savedInstanceState.getInt(getString(R.string.recipe_step_video_playback_window_key), C.INDEX_UNSET);
      playerPosition = savedInstanceState.getLong(getString(R.string.recipe_step_video_playback_position_key), C.TIME_UNSET);
    }
    else
    {
      // The default playback position and window
      setDefaultPlaybackPosition();
    }

    setupUi();

    return fragmentRecipeCardDetailStepBinding.getRoot();
  }

  @Override
  public void onResume()
  {
    super.onResume();

    resumeVideo();
  }

  @Override
  public void onPause()
  {
    super.onPause();

    stopVideoIfRunning();
  }

  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);

    outState.putParcelable(getString(R.string.recipe_step_type_key), recipeStep);

    stopVideoIfRunning();

    outState.putInt(getString(R.string.recipe_step_video_playback_window_key), playerWindow);
    outState.putLong(getString(R.string.recipe_step_video_playback_position_key), playerPosition);
  }

  public void setOrientation(int orientation)
  {
    this.orientation = orientation;
  }

  public void setLayoutSizeHint(int layoutSizeHint)
  {
    this.layoutSizeHint = layoutSizeHint;
  }

  private void setDefaultPlaybackPosition()
  {
    playerWindow = C.INDEX_UNSET;
    playerPosition = C.TIME_UNSET;
  }

  public void setRecipeStep(RecipeStep recipeStep)
  {
    this.recipeStep = recipeStep;

    if(fragmentRecipeCardDetailStepBinding != null)
    {
      stopVideoIfRunning();
      // also reset player position
      setDefaultPlaybackPosition();
      setupUi();
    }
  }

  public void setPreviousButtonEnabled(boolean flag)
  {
    previousButtonEnabled = flag;

    if(fragmentRecipeCardDetailStepBinding != null)
    {
      fragmentRecipeCardDetailStepBinding.buttonPreviousStep.setEnabled(flag);
    }
  }

  public void setNextButtonEnabled(boolean flag)
  {
    nextButtonEnabled = flag;

    if(fragmentRecipeCardDetailStepBinding != null)
    {
      fragmentRecipeCardDetailStepBinding.buttonNextStep.setEnabled(flag);
    }
  }

  private void stopVideoIfRunning()
  {
    if(simpleExoPlayer != null)
    {
      playerWindow = simpleExoPlayer.getCurrentWindowIndex();
      playerPosition = simpleExoPlayer.getContentPosition();
      simpleExoPlayer.stop();
      simpleExoPlayer.release();
      simpleExoPlayer = null;
    }
  }

  private void resumeVideo()
  {
    String videoUrl = recipeStep.getVideoUrl();

    videoAvailable = videoUrl != null && videoUrl.length() > 0;

    if(videoAvailable)
    {
      // set up player
      setupPlayer(fragmentRecipeCardDetailStepBinding);
      setupSample(videoUrl);
    }
  }

  private void setupUi()
  {
    String videoUrl = recipeStep.getVideoUrl();
    String imageUrl = recipeStep.getThumbnailUrl();

    videoAvailable = videoUrl != null && videoUrl.length() > 0;
    imageAvailable = imageUrl != null && imageUrl.length() > 0;

    if(videoAvailable)
    {
      // make sure the player is visible since we could have disabled it
      fragmentRecipeCardDetailStepBinding.playerView.setVisibility(View.VISIBLE);

      // make sure the image view is not visible
      fragmentRecipeCardDetailStepBinding.ivRecipeDetailStep.setVisibility(View.INVISIBLE);

      // set up player
      setupPlayer(fragmentRecipeCardDetailStepBinding);
      setupSample(videoUrl);
    }
    else if(imageAvailable)
    {
      // show the image
      ImageView imageView = fragmentRecipeCardDetailStepBinding.ivRecipeDetailStep;

      // temporarily hide the media until picasso returns
      imageAvailable = false;
      fragmentRecipeCardDetailStepBinding.ivRecipeDetailStep.setVisibility(View.GONE);
      fragmentRecipeCardDetailStepBinding.playerView.setVisibility(View.GONE);
      fragmentRecipeCardDetailStepBinding.horizontalDivider.setVisibility(View.GONE);

      Picasso.with(getContext())
              .load(imageUrl)
              .into(imageView, new Callback()
              {
                private final int stepAtExecution = recipeStep.getId();

                @Override
                public void onSuccess()
                {
                  // throw away picasso results from another execution, otherwise run as normal
                  if(stepAtExecution == recipeStep.getId())
                  {
                    imageAvailable = false;

                    // hide player, because there is no video
                    fragmentRecipeCardDetailStepBinding.playerView.setVisibility(View.INVISIBLE);

                    fragmentRecipeCardDetailStepBinding.ivRecipeDetailStep.setVisibility(View.VISIBLE);
                    fragmentRecipeCardDetailStepBinding.horizontalDivider.setVisibility(View.VISIBLE);
                  }
                }

                @Override
                public void onError()
                {
                  // throw away picasso results from another execution, otherwise run as normal
                  if(stepAtExecution == recipeStep.getId())
                  {
                    // error loading the image, hide the image view, the player and the divider
                    imageAvailable = false;

                    fragmentRecipeCardDetailStepBinding.ivRecipeDetailStep.setVisibility(View.GONE);
                    fragmentRecipeCardDetailStepBinding.playerView.setVisibility(View.GONE);
                    fragmentRecipeCardDetailStepBinding.horizontalDivider.setVisibility(View.GONE);
                  }
                }
              });
    }
    else
    {
      // remove both the image and the video
      fragmentRecipeCardDetailStepBinding.playerView.setVisibility(View.GONE);
      fragmentRecipeCardDetailStepBinding.ivRecipeDetailStep.setVisibility(View.GONE);
    }

    if(layoutSizeHint == YummConstants.TABLET_SIZED_DEVICE)
    {
      // tablet
      fragmentRecipeCardDetailStepBinding.tvRecipeCardDetailStep.setText(recipeStep.getDescription());

      // Set up divider
      if((videoAvailable || imageAvailable) && recipeStep.getDescription().length() > 0)
      {
        fragmentRecipeCardDetailStepBinding.horizontalDivider.setVisibility(View.VISIBLE);
      }
      else
      {
        fragmentRecipeCardDetailStepBinding.horizontalDivider.setVisibility(View.GONE);
      }
    }
    else
    {
      // phone
      if(!(orientation == Configuration.ORIENTATION_LANDSCAPE && (videoAvailable || imageAvailable)))
      {
        fragmentRecipeCardDetailStepBinding.tvRecipeCardDetailStep.setText(recipeStep.getDescription());

        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {
          if((videoAvailable || imageAvailable) && recipeStep.getDescription().length() > 0)
          {
            fragmentRecipeCardDetailStepBinding.horizontalDivider.setVisibility(View.VISIBLE);
          }
          else
          {
            fragmentRecipeCardDetailStepBinding.horizontalDivider.setVisibility(View.GONE);
          }
        }

        // setup button click listeners and make buttons visible

        fragmentRecipeCardDetailStepBinding.buttonPreviousStep.setVisibility(View.VISIBLE);
        fragmentRecipeCardDetailStepBinding.buttonNextStep.setVisibility(View.VISIBLE);

        fragmentRecipeCardDetailStepBinding.buttonPreviousStep.setOnClickListener(new View.OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            recipeCardDetailStepPreviousClickListener.onPreviousButtonClick();
          }
        });

        fragmentRecipeCardDetailStepBinding.buttonNextStep.setOnClickListener(new View.OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            recipeCardDetailStepNextClickListener.onNextButtonClick();
          }
        });

        // setup button enabled states
        fragmentRecipeCardDetailStepBinding.buttonPreviousStep.setEnabled(previousButtonEnabled);
        fragmentRecipeCardDetailStepBinding.buttonNextStep.setEnabled(nextButtonEnabled);
      }
      else
      {
        // phone mode, landscape, video only
        fragmentRecipeCardDetailStepBinding.tvRecipeCardDetailStep.setVisibility(View.GONE);
        fragmentRecipeCardDetailStepBinding.buttonPreviousStep.setVisibility(View.GONE);
        fragmentRecipeCardDetailStepBinding.buttonNextStep.setVisibility(View.GONE);
      }
    }
  }

  private void setupPlayer(FragmentRecipeCardDetailStepBinding fragmentRecipeCardDetailStepBinding)
  {
    if(simpleExoPlayer == null)
    {
      RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

      fragmentRecipeCardDetailStepBinding.playerView.setPlayer(simpleExoPlayer);
    }
  }

  private void setupSample(String url)
  {
    // setup a media source
    final String appAgentString = getContext().getString(R.string.exo_player_app_agent_string);

    DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(getContext(), appAgentString);

    MediaSource mediaSource = new ExtractorMediaSource.Factory(defaultDataSourceFactory).createMediaSource(Uri.parse(url));

    boolean pastStartPosition = playerWindow != C.INDEX_UNSET;

    if(pastStartPosition)
    {
      simpleExoPlayer.seekTo(playerWindow, playerPosition);
    }

    simpleExoPlayer.prepare(mediaSource, !pastStartPosition, false);
    simpleExoPlayer.setPlayWhenReady(true);
  }
}
