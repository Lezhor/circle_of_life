package com.android.circleoflife.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.User;

import java.io.IOException;
import java.util.Date;

public class AuthenticationImpl implements Authentication {

    private static volatile AuthenticationImpl instance;

    /**
     * AuthenticationImpl follows the singleton-pattern
     * @return only existing instance of this class
     */
    public static AuthenticationImpl getInstance() {
        if (instance == null) {
            synchronized (AuthenticationImpl.class) {
                if (instance == null) {
                    instance = new AuthenticationImpl();
                }
            }
        }
        return instance;
    }

    // TODO: 16.12.2023 Implement all methods!!

    private volatile User user;

    public AuthenticationImpl() {
        // TODO: 16.12.2023 Temp implementation!!!
        LiveData<User> liveData = App.getDatabaseController().getUser("john_doe");
        liveData.observeForever(new Observer<>() {
            @Override
            public void onChanged(User user) {
                AuthenticationImpl.this.setUser(user);
                liveData.removeObserver(this);
            }
        });
    }

    @Override
    public boolean localOnly() throws AuthenticationFailedException {
        return false;
    }

    @Override
    public boolean enableServerSide() throws AuthenticationFailedException {
        return false;
    }

    @Override
    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
        this.notifyAll();
    }

    @Override
    public User waitForUser() throws InterruptedException {
        if (user == null) {
            synchronized (this) {
                if (user == null) {
                    this.wait();
                }
            }
        }
        return null;
    }

    @Override
    public String getUserName() throws AuthenticationFailedException {
        return null;
    }

    @Override
    public String usernameToDisplayedVersion(String username) {
        return null;
    }

    @Override
    public String displayedUsernameToActualVersion(String displayedUsername) {
        return null;
    }

    @Override
    public String getPasswordHash() throws AuthenticationFailedException {
        return null;
    }

    @Override
    public Date getTimeStampOfAccountCreation() throws AuthenticationFailedException {
        return null;
    }

    @Override
    public String getAuthenticationString() throws AuthenticationFailedException {
        return null;
    }

    @Override
    public boolean authenticated(String userName) {
        return false;
    }

    @Override
    public boolean login(String userName, String password) throws AuthenticationFailedException {
        return false;
    }

    @Override
    public void logout() {

    }

    @Override
    public void signUp(String userName, String password, boolean localOnly) throws SignUpException {

    }

    @Override
    public void validateUserName(String userName) throws IllegalArgumentException {

    }

    @Override
    public boolean checkIfUserNameAvailable(String userName, boolean onServerToo) throws IOException {
        return false;
    }

    @Override
    public void validatePassword(String password) throws IllegalArgumentException {

    }
}
