package com.marcosviniciusferreira.whatsapp.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.marcosviniciusferreira.whatsapp.R;
import com.marcosviniciusferreira.whatsapp.adapter.GrupoSelecionadoAdapter;
import com.marcosviniciusferreira.whatsapp.helper.RecyclerItemClickListener;
import com.marcosviniciusferreira.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();

    private TextView textTotalParticipantes;
    private RecyclerView recyclerParticipantes;
    private CircleImageView imageGrupo;
    private EditText editNomeGrupo;
    private RecyclerView recyclerMembrosSelecionados;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastrar Novo Grupo");
        setSupportActionBar(toolbar);

        //Configuracoes iniciais
        textTotalParticipantes = findViewById(R.id.textTotalParticipantes);
        imageGrupo = findViewById(R.id.imageGrupo);
        editNomeGrupo = findViewById(R.id.editNomeGrupo);
        recyclerParticipantes = findViewById(R.id.recyclerParticipantes);


        FloatingActionButton fab = findViewById(R.id.fabAvancarCadastro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar lista de membros passada
        if (getIntent().getExtras() != null) {
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            listaMembrosSelecionados.addAll(membros);

            textTotalParticipantes.setText("Participantes: " + listaMembrosSelecionados.size());

        }

        //Configurar adapter para os Membros Selecionados
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());

        //Configurar recyclerview para os Membros Selecionados
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerParticipantes.setLayoutManager(layoutManager);
        recyclerParticipantes.setHasFixedSize(true);
        recyclerParticipantes.setAdapter(grupoSelecionadoAdapter);


    }

}
