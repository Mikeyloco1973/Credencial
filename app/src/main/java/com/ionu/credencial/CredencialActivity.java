package com.ionu.credencial;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;   // Importante: agrega Glide en tu Gradle
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CredencialActivity extends AppCompatActivity {

    TextView tvNombres, tvApellidos, tvRut, tvCargo;
    ImageView imgFoto;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credencial);

        tvNombres = findViewById(R.id.tvNombres);
        tvApellidos = findViewById(R.id.tvApellidos);
        tvRut = findViewById(R.id.tvRut);
        tvCargo = findViewById(R.id.tvCargo);
        imgFoto = findViewById(R.id.imgFoto);

        String rut = getIntent().getStringExtra("rut");

        databaseReference = FirebaseDatabase.getInstance().getReference("tecnicos").child(rut);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    tvNombres.setText(snapshot.child("nombres").getValue(String.class));
                    tvApellidos.setText(snapshot.child("apellidos").getValue(String.class));
                    tvRut.setText(snapshot.child("rut").getValue(String.class));
                    tvCargo.setText(snapshot.child("cargo").getValue(String.class));

                    // Verificar si existe foto
                    String fotoUrl = snapshot.child("foto").getValue(String.class);
                    if(fotoUrl != null && !fotoUrl.isEmpty()){
                        // Cargar foto desde URL con Glide
                        Glide.with(CredencialActivity.this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.sin_foto) // mientras carga
                                .error(R.drawable.sin_foto)       // si falla
                                .into(imgFoto);
                    } else {
                        // Si no hay foto, mostrar la imagen por defecto
                        imgFoto.setImageResource(R.drawable.sin_foto);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
