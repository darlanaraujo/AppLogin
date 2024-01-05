package com.darlanaraujo.projetofirebase.view.formcadastro

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.darlanaraujo.projetofirebase.R
import com.darlanaraujo.projetofirebase.databinding.ActivityFormCadastroBinding
import com.darlanaraujo.projetofirebase.view.formlogin.FormLogin
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import org.w3c.dom.Text

class FormCadastro : AppCompatActivity() {

//    Variável que elimina o uso do findViewById
    private lateinit var binding: ActivityFormCadastroBinding

//    Variável que faz a conexão e recupera a instacia do backend do firebase
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastrar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            it.fecharteclado()

            if(email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(it, R.string.msg_campo_vazio, Snackbar.LENGTH_SHORT)

                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                // Envia os dados digitados para o autenticador do firebase
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener{ cadastro ->
                    // Se o retorno desse envio for positivo (cumprio os requisitos) ele finaliza o cadastro
                    if(cadastro.isSuccessful) {
                        val snackbar = Snackbar.make(it, R.string.msg_cadastro_sucesso, Snackbar.LENGTH_SHORT)
                        snackbar.show()

                        // Limpa os campos de texto
                        binding.editEmail.setText("")
                        binding.editSenha.setText("")
                    }
                    // Se os requisitos não forem cumpridos, fazemos o tratamento das exceptions
                }.addOnFailureListener {exception ->

                    val mensagemErro = when(exception) {
                        is FirebaseAuthWeakPasswordException -> R.string.msg_erro_senha
                        is FirebaseAuthInvalidCredentialsException -> R.string.msg_erro_email
                        is FirebaseAuthUserCollisionException -> R.string.msg_erro_usuario_cadastrado
                        is FirebaseNetworkException -> R.string.msg_erro_conexao // Foi adicionado uma permission no manifest
                        else -> R.string.msg_erro_cadastro
                    }

                    val snackbar = Snackbar.make(it, mensagemErro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }
            }
        }

        binding.txtVoltarLogin.setOnClickListener {
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
        }
    }
    private fun View.fecharteclado() {
        val input = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(windowToken, 0)
    }
}