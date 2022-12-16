package com.marcosviniciusferreira.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode) {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> listaPermissoes = new ArrayList<>();

            //Percorrer as permissoes passadas varificando se já tem a permissão liberada
            for (String permissao : permissoes) {
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if(!temPermissao) listaPermissoes.add(permissao);
            }

            //Caso a lista esteja vazia, não será necessário solicitar permissões
            if(listaPermissoes.isEmpty()) return true;

            //Converter um List de Strings para um Array de Permissoes (Strings[])
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //Se não, solicitaremos a permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }

        return true;
    }
}
