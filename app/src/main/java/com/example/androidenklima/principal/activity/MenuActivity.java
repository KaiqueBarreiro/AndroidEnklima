package com.example.androidenklima.principal.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.androidenklima.cadastrar.CadastrarFragment;
import com.example.androidenklima.R;
import com.example.androidenklima.consultar.fragment.ConsultarFragment;
import com.example.androidenklima.logout.LogoutFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private boolean noReplaceFragment;
    private static final String FRAGMENT_CADASTRAR = "fragment_cadastrar";
    private static final String FRAGMENT_LOGOUT = "fragment_logout";
    private static final String FRAGMENT_CONSULTAR = "fragment_consultar";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("Cadastrar Ocorrência");
        navigationView = findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);
        CadastrarFragment cadastrarFragment = CadastrarFragment.newInstance();
        cadastrarFragment.setContext(this);
        openFragment(cadastrarFragment, FRAGMENT_CADASTRAR);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_cadastrar:
                if (!noReplaceFragment) {
                    getSupportActionBar().setTitle("Cadastrar ocorrência");
                    Fragment fragmentCadastrar = CadastrarFragment.newInstance();
                    ((CadastrarFragment) fragmentCadastrar).setContext(this);
                    openFragment(fragmentCadastrar, FRAGMENT_CADASTRAR);
                }
                break;
            case R.id.navigation_consultar:
                if (!noReplaceFragment) {
                    getSupportActionBar().setTitle("Ocorrências");
                    Fragment fragmentConsultar = ConsultarFragment.newInstance();
                    ((ConsultarFragment) fragmentConsultar).setContext(this);
                    openFragment(fragmentConsultar, FRAGMENT_CONSULTAR);
                }
                break;
            case R.id.navigation_sair:
                if (!noReplaceFragment) {
                    getSupportActionBar().setTitle("Sair do aplicativo");
                    Fragment fragmentSair = LogoutFragment.newInstance();
                    ((LogoutFragment) fragmentSair).setActivity(this);
                    openFragment(fragmentSair, FRAGMENT_LOGOUT);
                }
                break;
        }
        return true;
    }

    private void openFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count >= 2) {
            managerIconsOfBottomNavigation(getSupportFragmentManager());
            super.onBackPressed();
        }
    }

    private void managerIconsOfBottomNavigation(FragmentManager fragmentManager) {
        //Apenas para mudar a cor do icones da BoottomNavigationVew
        Fragment mfragmentA = fragmentManager.findFragmentByTag(FRAGMENT_CADASTRAR);
        if (mfragmentA != null) {
            if (mfragmentA.isVisible()) {
                noReplaceFragment = true;
                getSupportActionBar().setTitle("Cadastrar ocorrência");
                navigationView.setSelectedItemId(R.id.navigation_cadastrar);
                noReplaceFragment = false;
            }
        }

        Fragment mfragmentB = fragmentManager.findFragmentByTag(FRAGMENT_LOGOUT);
        if (mfragmentB != null) {
            if (mfragmentB.isVisible()) {
                noReplaceFragment = true;
                getSupportActionBar().setTitle("Sair do aplicativo");
                navigationView.setSelectedItemId(R.id.navigation_sair);
                noReplaceFragment = false;
            }
        }

        Fragment mfragmentC = fragmentManager.findFragmentByTag(FRAGMENT_CONSULTAR);
        if (mfragmentC != null) {
            if (mfragmentC.isVisible()) {
                noReplaceFragment = true;
                getSupportActionBar().setTitle("Ocorrências");
                navigationView.setSelectedItemId(R.id.navigation_consultar);
                noReplaceFragment = false;
            }
        }
    }
}
