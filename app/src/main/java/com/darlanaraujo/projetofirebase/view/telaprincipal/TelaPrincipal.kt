package com.darlanaraujo.projetofirebase.view.telaprincipal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.darlanaraujo.projetofirebase.R
import com.darlanaraujo.projetofirebase.databinding.ActivityTelaPrincipalBinding
import com.darlanaraujo.projetofirebase.view.formlogin.FormLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.firestore.FirebaseFirestore

class TelaPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityTelaPrincipalBinding
    private val bd = FirebaseFirestore.getInstance()
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

        binding.btnSalvar.setOnClickListener {

            val usuario = hashMapOf(
                "nome" to "Darlan",
                "sobrenome" to "Araujo",
                "idade" to 37
            )

            bd.collection("usuarios").document("1").set(usuario).addOnCompleteListener {
                Log.d("bd", "Dados salvos com sucesso!")
            }.addOnFailureListener {
                Log.d("bd", it.toString())
            }
        }

        binding.btnEditar.setOnClickListener {
            bd.collection("usuarios").document("1").update("sobrenome", "P. Araujo", "idade", 38).addOnCompleteListener {
                Log.d("bd", "Dados alterados com sucesso!")
            }.addOnFailureListener {
                Log.d("bd", it.toString())
            }
        }

        binding.btnDeletar.setOnClickListener {
            bd.collection("usuarios").document("1").delete().addOnCompleteListener {
                Log.d("bd", "Usuário deletado com sucesso!")
            }.addOnFailureListener {
                Log.d("bd", it.toString())
            }
        }

        binding.btnLer.setOnClickListener {
            bd.collection("usuarios").document("1").addSnapshotListener { value, error ->
                if(value != null) {
                    binding.txtNome.text = value.getString("nome")
                    binding.txtSobrenome.text = value.getString("sobrenome")
                    binding.txtIdade.text = value.getLong("idade").toString()
                }
            }
        }
    }
}