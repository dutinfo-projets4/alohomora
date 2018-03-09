package alohomora.model.retrofitlistener;

import alohomora.model.User;

public interface RetrofitListenerUser {
	void onUserLoad(User user);

	void error(String msg);
}
