package juegosretro.session;

import juegosretro.model.User;
import java.util.concurrent.atomic.AtomicReference;

public final class Session {
    private static final AtomicReference<User> currentUser = new AtomicReference<>();

    private Session() {
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static void clear() {
        currentUser.set(null);
    }
}
