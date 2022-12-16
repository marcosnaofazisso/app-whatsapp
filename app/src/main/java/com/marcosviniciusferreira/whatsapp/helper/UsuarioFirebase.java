package com.marcosviniciusferreira.whatsapp.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.marcosviniciusferreira.whatsapp.config.FirebaseConfig;

public class UsuarioFirebase {

    public static String getIdentificadorUsuario() {
        FirebaseAuth usuario = FirebaseConfig.getFirebaseAuth();
        String idUsuario = Base64Custom.codeBase64(usuario.getCurrentUser().getEmail());
        return idUsuario;
    }

    public static String getEmailUsuario() {
        FirebaseAuth usuario = FirebaseConfig.getFirebaseAuth();
        return usuario.getCurrentUser().getEmail();
    }
}
