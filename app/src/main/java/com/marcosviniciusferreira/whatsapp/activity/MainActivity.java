package com.marcosviniciusferreira.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.marcosviniciusferreira.whatsapp.R;
import com.marcosviniciusferreira.whatsapp.config.FirebaseConfig;
import com.marcosviniciusferreira.whatsapp.fragment.ContatosFragment;
import com.marcosviniciusferreira.whatsapp.fragment.ConversasFragment;
import com.marcosviniciusferreira.whatsapp.helper.RecyclerItemClickListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseConfig.getFirebaseAuth();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp");
        toolbar.setTitleTextColor(this.getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);

        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversasFragment.class)
                        .add("Contatos", ContatosFragment.class)
                        .create());

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

        //Configuracao do search view
        searchView = findViewById(R.id.materialSearchPrincipal);

        searchView.setHint("Pesquisar");


        //Listener para o search view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                fragment.recarregarConversas();

            }
        });


        //Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //Veirifcar se esta pesquisando
                //Conversas ou contatos
                //a partir da tab que esta ativa
                switch (viewPager.getCurrentItem()) {
                    case 0:

                        ConversasFragment conversasFragment = (ConversasFragment) adapter.getPage(0);
                        if (newText != null && !newText.isEmpty()) {
                            conversasFragment.pesquisarConversas(newText.toLowerCase());
                        } else {
                            conversasFragment.recarregarConversas();
                        }

                        break;

                    case 1:

                        ContatosFragment contatosFragment = (ContatosFragment) adapter.getPage(1);
                        if (newText != null && !newText.isEmpty()) {
                            contatosFragment.pesquisarContatos(newText.toLowerCase());
                        } else {
                            contatosFragment.recarregarContatos();

                        }

                        break;
                }


                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //Configurar botao de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuSair) {
            deslogarUsuario();
            finish();
        }
        if (item.getItemId() == R.id.menuConfiguracoes) {
            abrirConfiguracoes();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario() {
        try {
            auth.signOut();
        } catch (Exception e) {
            Log.i("EXCEPTION", "ERRO: " + e);
        }

    }

    public void abrirConfiguracoes() {
        Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
        startActivity(intent);
    }
}
