package com.marcosviniciusferreira.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.marcosviniciusferreira.whatsapp.R;
import com.marcosviniciusferreira.whatsapp.config.FirebaseConfig;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail;
    private TextInputEditText campoSenha;
    private Button buttonLogar;
    private TextView buttonCadastro;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonCadastro = findViewById(R.id.buttonCadastro);
        buttonLogar = findViewById(R.id.buttonLogar);
        campoEmail = findViewById(R.id.textEmail);
        campoSenha = findViewById(R.id.textSenha);


        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });


        buttonLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmail = campoEmail.getText().toString();
                String textSenha = campoSenha.getText().toString();

                if (textEmail.isEmpty() || textSenha.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    auth = FirebaseConfig.getFirebaseAuth();
                    auth.signInWithEmailAndPassword(textEmail, textSenha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(getApplicationContext(), "Sucesso!!!", Toast.LENGTH_SHORT).show();
                                goToMain();
                            } else {
                                Toast.makeText(getApplicationContext(), "Deu ruim...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

    }

    public void goToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);


    }

}
