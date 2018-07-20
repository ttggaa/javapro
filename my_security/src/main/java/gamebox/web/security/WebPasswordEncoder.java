package gamebox.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class WebPasswordEncoder implements PasswordEncoder {
    private static final Logger logger = LoggerFactory.getLogger(WebPasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean ret = rawPassword.toString().equals(encodedPassword);
        return ret;
    }
}
