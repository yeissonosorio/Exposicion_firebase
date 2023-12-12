package com.example.exposicion_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnIngresar, btnregistrar;
    EditText txtCorreo, txtContra;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIngresar = (Button) findViewById(R.id.btnIniciar);
        txtContra = (EditText) findViewById(R.id.txtContra);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        btnregistrar = (Button) findViewById(R.id.btnregistrar);

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = txtCorreo.getText().toString();
                String contra = txtContra.getText().toString();

                // Verificar si los campos están vacíos antes de intentar crear un usuario
                if (correo.isEmpty() || contra.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(correo, contra)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // El usuario se ha creado exitosamente
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        // Envío del correo de verificación
                                        if (user != null) {
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // El correo de verificación se envió correctamente
                                                                Toast.makeText(MainActivity.this, "Se ha enviado un correo de verificación", Toast.LENGTH_SHORT).show();
                                                                txtCorreo.setText("");
                                                                txtContra.setText("");
                                                            } else {
                                                                // Error al enviar el correo de verificación
                                                                Toast.makeText(MainActivity.this, "Error al enviar el correo de verificación", Toast.LENGTH_SHORT).show();
                                                                Log.e("SendEmailError", task.getException().getMessage());
                                                            }
                                                        }
                                                    });
                                        }

                                        // Puedes realizar acciones adicionales aquí después de la creación del usuario
                                    } else {
                                        // La creación del usuario falló, maneja el error aquí
                                        Toast.makeText(MainActivity.this, "La creación del usuario falló.", Toast.LENGTH_SHORT).show();
                                        Log.e("CreateUserError", task.getException().getMessage());
                                    }
                                }
                            });
                    }
            }
        });




        //VERIFICAR EN EL INICIO DE SESION QUE EL CORREO ESTE VERIFICADO
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = txtCorreo.getText().toString();
                String contra = txtContra.getText().toString();

                // Verificar si los campos están vacíos antes de intentar crear un usuario
                if (correo.isEmpty() || contra.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(correo, contra)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Inicio de sesión exitoso
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        if (user != null && user.isEmailVerified()) {
                                            // Usuario autenticado y correo electrónico verificado
                                            // Redirigir a la actividad principal o realizar otras acciones
                                            startActivity(new Intent(MainActivity.this, ActivityInicio.class));
                                            finish(); // Finaliza la actividad actual si es necesario
                                            txtCorreo.setText("");
                                            txtContra.setText("");
                                        } else {
                                            // El correo electrónico no está verificado
                                            Toast.makeText(MainActivity.this, "Por favor, verifica tu correo electrónico", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // El inicio de sesión falló, manejar el error aquí
                                        Toast.makeText(MainActivity.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                                        Log.e("LoginError", task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });









    }
}