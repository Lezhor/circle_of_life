# Circle of Life
This Android-App is a Life-Managing App which allows the user to manage their todos and cycles.
There is an account-system, which uses a Server. The server is a separate application which runs on the desktop.

The server repo can be found [here](https://github.com/Lezhor/circle_of_life_server).

# Setup
If you want to use/test the Project for yourself, you most probably will need the server too. However this is not necessary;
The App can be used without a server at all (But be aware that many tests, for example the synchronization ones will fail).
One of the two EndToEnd-Tests works without a server in case you have no access to it.

The server is connected with the IP and PORT - both of these can be set in the Utils-Class:

```txt
app/src/main/java/com/android/circleoflife/application/App
```

```txt
Default IP: localhost
Default PORT: 31163
```