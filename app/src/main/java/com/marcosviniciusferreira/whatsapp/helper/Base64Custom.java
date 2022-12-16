package com.marcosviniciusferreira.whatsapp.helper;

import android.util.Base64;
import android.util.Log;

public class Base64Custom {

    public static String codeBase64(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");

    }

    public static String decodeBase64(String textoCodificado) {
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
