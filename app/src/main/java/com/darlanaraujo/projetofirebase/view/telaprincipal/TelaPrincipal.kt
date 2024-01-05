package com.darlanaraujo.projetofirebase.view.telaprincipal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.darlanaraujo.projetofirebase.R
import com.darlanaraujo.projetofirebase.databinding.ActivityTelaPrincipalBinding
import com.darlanaraujo.projetofirebase.view.formlogin.FormLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException

class TelaPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityTelaPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogoff.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
}