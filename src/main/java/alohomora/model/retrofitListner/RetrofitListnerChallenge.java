package alohomora.model.retrofitListner;

import alohomora.model.Challenge;

public interface RetrofitListnerChallenge {
    void onChallengeLoad(Challenge challenge);
    void error(String msg);
}
