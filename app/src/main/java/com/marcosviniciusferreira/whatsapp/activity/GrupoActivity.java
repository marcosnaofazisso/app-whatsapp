package com.marcosviniciusferreira.whatsapp.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcosviniciusferreira.whatsapp.R;
import com.marcosviniciusferreira.whatsapp.adapter.ContatosAdapter;
import com.marcosviniciusferreira.whatsapp.config.FirebaseConfig;
import com.marcosviniciusferreira.whatsapp.helper.UsuarioFirebase;
import com.marcosviniciusferreira.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GrupoActivity extends AppCompatActivity {

    private RecyclerView recyclerMembros, recyclerMembrosSelecionados;
    private ContatosAdapter contatosAdapter;
    private List<Usuario> listaMembros = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private DatabaseReference usuariosRef;
    private FirebaseUser usuarioAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //botao voltar ja ativado
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerMembros = findViewById(R.id.recyclerMembros);
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);
        usuariosRef = FirebaseConfig.getFirebaseDatabase().child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();


        //Configurar adapter
        contatosAdapter = new ContatosAdapter(listaMembros, getApplicationContext());

        //Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(contatosAdapter);


    }


    public void recuperarMembros() {
        valueEventListenerMembros = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUsuarioAtual = usuarioAtual.getEmail();

                    if (!emailUsuarioAtual.equals(usuario.getEmail())) {
                        listaMembros.add(usuario);

                    }
                }

                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        recuperarMembros();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerMembros);
    }

}
