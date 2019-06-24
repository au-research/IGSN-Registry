package org.csiro.igsn.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.apache.log4j.Logger;
import org.csiro.igsn.web.controllers.WebFormIGSNMintCtrl;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ShaPasswordEncoder implements PasswordEncoder {

    final Logger log = Logger.getLogger( ShaPasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        final StringBuilder sb = new StringBuilder(rawPassword.length());
        sb.append(rawPassword);
        return encryptPassword(sb.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        final StringBuilder sb = new StringBuilder(rawPassword.length());
        sb.append(rawPassword);
        return encode(rawPassword).equals(encodedPassword);
    }

    public static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }




}
