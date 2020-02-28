package startup;

import javax.security.auth.Subject;

public interface AbstractObserver {
    Subject subject = null;
    void update();
}
