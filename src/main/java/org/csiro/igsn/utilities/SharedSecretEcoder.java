package org.csiro.igsn.utilities;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


public class SharedSecretEcoder implements PasswordEncoder {

    final Logger log = Logger.getLogger(SharedSecretEcoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        log.info(rawPassword);
        final StringBuilder sb = new StringBuilder(rawPassword.length());
        sb.append(rawPassword);
        return sb.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }

}
