package pucp.telecom.moviles.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import pucp.telecom.moviles.lab3.Fragments.DialogFragmentEjemplo;
import pucp.telecom.moviles.lab3.Fragments.DialogFragmentGuardarLocal;
import pucp.telecom.moviles.lab3.Fragments.DialogFragmentGuardarRemoto;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonGuardarLocal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragmentGuardarLocal dialogFragment = new DialogFragmentGuardarLocal();
                dialogFragment.show(getSupportFragmentManager(), "guardarLocal");
            }
        });

        findViewById(R.id.buttonGuardarRemoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragmentGuardarRemoto dialogFragment = new DialogFragmentGuardarRemoto();
                dialogFragment.show(getSupportFragmentManager(), "guardarRemoto");
            }
        });
    }
}