package com.marcosviniciusferreira.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosviniciusferreira.whatsapp.config.FirebaseConfig;
import com.marcosviniciusferreira.whatsapp.helper.UsuarioFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;

    public Usuario() {
    }

    public void salvar() {

        DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("usuarios").child(getId());

        usuario.setValue(this);

    }

    public void atualizar() {
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference usuariosRef = database.child("usuarios").child(identificadorUsuario);

        usuariosRef.updateChildren(converterParaMap());


    }

    @Exclude
    public Map<String, Object> converterParaMap() {
        HashMap<String, Object> usuarioMap = new HashMap<>();

        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getFoto());
        return usuarioMap;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
