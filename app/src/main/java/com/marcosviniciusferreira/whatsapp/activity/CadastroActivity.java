package com.marcosviniciusferreira.whatsapp.activity;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.marcosviniciusferreira.whatsapp.R;
import com.marcosviniciusferreira.whatsapp.config.FirebaseConfig;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText cadastroNome;
    private TextInputEditText cadastroEmail;
    private TextInputEditText cadastroSenha;
    private Button buttonCadastrar;

    private String textNome;
    private String textEmail;
    private String textSenha;

    FirebaseAuth authCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        cadastroNome = findViewById(R.id.textNomeCadastro);
        cadastroEmail = findViewById(R.id.textEmailCadastro);
        cadastroSenha = findViewById(R.id.textSenhaCadastro);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);


        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textNome = cadastroNome.getText().toString();
                textEmail = cadastroEmail.getText().toString();
                textSenha = cadastroSenha.getText().toString();

                if (textNome.isEmpty() || textEmail.isEmpty() || textSenha.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    cadastrarUsuario();
                    finish();
                }

            }
        });


    }

    public void cadastrarUsuario() {
        authCadastro = FirebaseConfig.getFirebaseAuth();
        authCadastro.createUserWithEmailAndPassword(textEmail, textSenha).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Erro ao Cadastrar!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
