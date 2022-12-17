package com.marcosviniciusferreira.whatsapp.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth usuario = FirebaseConfig.getFirebaseAuth();
        return usuario.getCurrentUser();
    }


    public static boolean atualizarNomeUsuario(String nome) {

        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }
                }
            });
        } catch (Exception e) {
            Log.i("Perfil", "Error: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

        return true;
    }


    public static boolean atualizarFotoUsuario(Uri url) {

        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar foto de perfil");
                    }
                }
            });
        } catch (Exception e) {
            Log.i("Perfil", "Error: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

        return true;
    }
}
