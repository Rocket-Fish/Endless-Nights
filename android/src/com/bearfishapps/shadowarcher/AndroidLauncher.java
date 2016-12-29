package com.bearfishapps.shadowarcher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bearfishapps.shadowarcher.MainGameClass;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication {
	private GameHelper _gameHelper;
	private final static int REQUEST_CODE_UNUSED = 9002;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the GameHelper.
		_gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		_gameHelper.enableDebugLog(true);
		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInSucceeded()
			{
			}

			@Override
			public void onSignInFailed()
			{
			}
		};
		_gameHelper.setMaxAutoSignInAttempts(0);
		_gameHelper.setup(gameHelperListener);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainGameClass(new GPGSInterface() {

			@Override
			public boolean signIn() {
				try
				{
					runOnUiThread(new Runnable()
					{
						//@Override
						public void run()
						{
							_gameHelper.beginUserInitiatedSignIn();
						}
					});
					return true;
				}
				catch (Exception e)
				{
					Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
					return false;
				}
			}

			@Override
			public boolean signOut() {
				try
				{
					runOnUiThread(new Runnable()
					{
						//@Override
						public void run()
						{
							_gameHelper.signOut();
						}
					});
					return true;
				}
				catch (Exception e)
				{
					Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
					return false;
				}
			}

			@Override
			public void rateGame() {
				String str ="https://play.google.com/store/apps/details?id=com.bearfishapps.shadowarcher.AndroidLauncher";
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
			}

			@Override
			public boolean submitScore(long score) {
				if (isSignedIn() == true)
				{
					Games.Leaderboards.submitScore(_gameHelper.getApiClient(), getString(R.string.leaderboard_id), score);
					return true;
				}
				else
				{
					return false;
					// Maybe sign in here then redirect to submitting score?
				}

			}

			@Override
			public boolean showScores() {
				if (isSignedIn() == true) {
					startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
					return true;
				}
				else
				{
					return false;
// Maybe sign in here then redirect to showing scores?
				}
			}

			@Override
			public boolean isSignedIn() {
				return _gameHelper.isSignedIn();
			}

			@Override
			public void unlockAchievementGPGS(String achievementId) {
				Games.Achievements.unlock(_gameHelper.getApiClient(), achievementId);
			}

			@Override
			public boolean getAchievementsGPGS() {
				if (_gameHelper.isSignedIn()) {
					startActivityForResult(Games.Achievements.getAchievementsIntent(_gameHelper.getApiClient()), 101);
					return true;
				}else {
					return false;
				}

			}
			@Override
			public int loadScoreOfLeaderBoard() {
				final int[] score = {0};
				Games.Leaderboards.loadCurrentPlayerLeaderboardScore(_gameHelper.getApiClient(), getString(R.string.leaderboard_id), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
					@Override
					public void onResult(final Leaderboards.LoadPlayerScoreResult scoreResult) {
						if (isScoreResultValid(scoreResult)) {
							// here you can get the score like this
							score[0] = (int)scoreResult.getScore().getRawScore();
						}
					}
				});
				return score[0];
			}

		}), config);
	}

	private boolean isScoreResultValid(final Leaderboards.LoadPlayerScoreResult scoreResult) {
		return scoreResult != null && GamesStatusCodes.STATUS_OK == scoreResult.getStatus().getStatusCode() && scoreResult.getScore() != null;
	}

	@Override
	protected void onStart() {
		super.onStart();
		_gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		_gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		_gameHelper.onActivityResult(requestCode, resultCode, data);
	}
}
