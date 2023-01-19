package com.marcosviniciusferreira.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marcosviniciusferreira.whatsapp.R;
import com.marcosviniciusferreira.whatsapp.adapter.ContatosAdapter;
import com.marcosviniciusferreira.whatsapp.adapter.MensagensAdapter;
import com.marcosviniciusferreira.whatsapp.config.FirebaseConfig;
import com.marcosviniciusferreira.whatsapp.helper.Base64Custom;
import com.marcosviniciusferreira.whatsapp.helper.UsuarioFirebase;
import com.marcosviniciusferreira.whatsapp.model.Conversa;
import com.marcosviniciusferreira.whatsapp.model.Grupo;
import com.marcosviniciusferreira.whatsapp.model.Mensagem;
import com.marcosviniciusferreira.whatsapp.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private EditText editMensagem;
    private Usuario usuarioDestinatario;
    private Usuario usuarioLogadoRemetente;
    private Grupo grupo;
    private ImageView imageCamera;
    private TextView textHorarioEnvio;

    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private StorageReference storage;

    private ChildEventListener childEventListenerMensagens;

    private static final int SELECAO_CAMERA = 100;


    //Identificado de usuarios remetente e destinatario
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        imageCamera = findViewById(R.id.imageCamera);
        textHorarioEnvio = findViewById(R.id.textHorarioEnvio);

        //Recuperar dados do usuário remetente
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogadoRemetente = UsuarioFirebase.getDadosUsuarioLogado();


        //Recuperar dados do usuário destinatario
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("chatGrupo")) {

                grupo = (Grupo) bundle.getSerializable("chatGrupo");
                idUsuarioDestinatario = grupo.getId();
                textViewNome.setText(grupo.getNome());

                String fotoGrupo = grupo.getFoto();
                if (fotoGrupo != null) {
                    Uri url = Uri.parse(grupo.getFoto());
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(circleImageViewFoto);
                } else {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);

                }


            } else {
                usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
                textViewNome.setText(usuarioDestinatario.getNome());

                String foto = usuarioDestinatario.getFoto();
                if (foto != null) {
                    Uri url = Uri.parse(usuarioDestinatario.getFoto());
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(circleImageViewFoto);
                } else {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }

                //Recuperar dados usuario destinatario
                idUsuarioDestinatario = Base64Custom.codeBase64(usuarioDestinatario.getEmail());

            }


        }

        //Configuracao adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        //Configuracao recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);

        //Recuperando instancia do database e referencia das mensagens
        database = FirebaseConfig.getFirebaseDatabase();
        storage = FirebaseConfig.getFirebaseStorage();
        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);


        //Evento de clique na camera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }

                if (imagem != null) {

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();

                    //Configurar as referencias do firebase
                    StorageReference imagemRef = storage.child("imagens")
                            .child("fotos")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem);

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao fazer upload");
                            Toast.makeText(ChatActivity.this,
                                    "Falha ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                            if (usuarioDestinatario != null) {

                                Mensagem mensagem = new Mensagem();
                                mensagem.setIdUsuario(idUsuarioRemetente);
                                mensagem.setMensagem("imagem.jpeg");
                                mensagem.setImagem(downloadUrl);

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    OffsetTime offset = OffsetTime.now();
                                    mensagem.setHorarioEnvio(String.valueOf(offset.getHour() - 3 + " : " + offset.getMinute()));
                                }

                                //Salvar mensagem remetente
                                salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                                //Salvar mensagem destinatario
                                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                            } else {

                                for (Usuario membro : grupo.getMembros()) {

                                    String idRemetenteGrupo = Base64Custom.codeBase64(membro.getEmail());
                                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();

                                    Mensagem mensagem = new Mensagem();
                                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                                    mensagem.setMensagem("imagem.jpeg");
                                    mensagem.setNome(usuarioLogadoRemetente.getNome());
                                    mensagem.setImagem(downloadUrl);

                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        OffsetTime offset = OffsetTime.now();
                                        mensagem.setHorarioEnvio(String.valueOf(offset.getHour() - 3 + " : " + offset.getMinute()));
                                    }

                                    //Salvar mensagem para o membro do grupo
                                    salvarMensagem(idRemetenteGrupo, idUsuarioDestinatario, mensagem);

                                    //Salvar conversa para o membro do grupo
                                    salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinatario, mensagem, true);


                                }

                            }

                            Toast.makeText(ChatActivity.this,
                                    "Sucesso ao enviar imagem",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void enviarMensagem(View view) {

        String textoMensagem = editMensagem.getText().toString();

        if (!textoMensagem.isEmpty()) {

            if (usuarioDestinatario != null) {
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioRemetente);
                mensagem.setMensagem(textoMensagem);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    OffsetTime offset = OffsetTime.now();
                    mensagem.setHorarioEnvio(String.valueOf(offset.getHour() - 3 + " : " + offset.getMinute()));
                }

                //Salvar mensagem para o remetente
                salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                //Salvar mensagem para o destinatario
                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                //Salvar conversa para o remetente
                salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinatario, mensagem, false);

                //Salvar conversa para o destinatario
                salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioLogadoRemetente, mensagem, false);

            } else {

                for (Usuario membro : grupo.getMembros()) {

                    String idRemetenteGrupo = Base64Custom.codeBase64(membro.getEmail());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setNome(usuarioLogadoRemetente.getNome());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        OffsetTime offset = OffsetTime.now();
                        mensagem.setHorarioEnvio(String.valueOf(offset.getHour() - 3 + " : " + offset.getMinute()));
                    }

                    //Salvar mensagem para o membro do grupo
                    salvarMensagem(idRemetenteGrupo, idUsuarioDestinatario, mensagem);

                    //Salvar conversa para o membro do grupo
                    salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinatario, mensagem, true);


                }


            }


        } else {
            Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar!",
                    Toast.LENGTH_LONG).show();

        }

    }

    private void salvarConversa(String idRemetente, String idDestinatario, Usuario usuarioExibicao, Mensagem msg, boolean isGrupo) {

        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idRemetente);
        conversaRemetente.setIdDestinatario(idDestinatario);
        conversaRemetente.setUltimaMensagem(msg.getMensagem());

        if (isGrupo) {
            conversaRemetente.setIsGrupo("true");
            conversaRemetente.setGrupo(grupo);

        } else {
            conversaRemetente.setUsuarioExibicao(usuarioExibicao);
            conversaRemetente.setIsGrupo("false");

        }
        conversaRemetente.salvar();


    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {

        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        //Limpar texto
        editMensagem.setText("");


    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagens() {

        mensagens.clear();

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
