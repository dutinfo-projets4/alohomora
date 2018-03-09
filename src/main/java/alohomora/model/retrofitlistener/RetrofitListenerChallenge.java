package alohomora.model.retrofitlistener;

import alohomora.model.Challenge;

public interface RetrofitListenerChallenge {
	void onChallengeLoad(Challenge challenge);

	void error(String msg);
}
