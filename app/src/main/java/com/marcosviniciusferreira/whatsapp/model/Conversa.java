package com.marcosviniciusferreira.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.marcosviniciusferreira.whatsapp.config.FirebaseConfig;

public class Conversa {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario usuarioExibicao;
    private String isGrupo;
    private Grupo grupo;

    public Conversa() {
        this.setIsGrupo("false");
    }

    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }

    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }


    public void salvar() {

        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference conversaRef = database.child("conversas");

        conversaRef
                .child(this.getIdRemetente())
                .child(this.getIdDestinatario())
                .setValue(this);
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public String getIsGrupo() {
        return isGrupo;
    }

    public void setIsGrupo(String isGrupo) {
        this.isGrupo = isGrupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
