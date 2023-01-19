package com.marcosviniciusferreira.whatsapp.model;

public class Mensagem {

    private String idUsuario;
    private String nome;
    private String mensagem;
    private String imagem;
    private String horarioEnvio;

    public Mensagem() {
        this.setNome("");
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHorarioEnvio() {
        return horarioEnvio;
    }

    public void setHorarioEnvio(String horarioEnvio) {
        this.horarioEnvio = horarioEnvio;
    }
}
