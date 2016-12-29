package com.bearfishapps.shadowarcher;

public interface GPGSInterface {
    public boolean signIn();
    public boolean signOut();
    public void rateGame();
    public boolean submitScore(long score);
    public boolean showScores();
    public boolean isSignedIn();
    public void unlockAchievementGPGS(String achievementId);
    public boolean getAchievementsGPGS();
    public int loadScoreOfLeaderBoard();
}
