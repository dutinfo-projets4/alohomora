package alohomora.model.retrofitListner;

import alohomora.model.User;

public interface RetrofitListnerUser {
    void onUserLoad(User user);
    void error(String msg);
}
