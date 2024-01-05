package com.darlanaraujo.projetofirebase.view.formlogin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.darlanaraujo.projetofirebase.R
import com.darlanaraujo.projetofirebase.databinding.ActivityFormLoginBinding
import com.darlanaraujo.projetofirebase.view.formcadastro.FormCadastro
import com.darlanaraujo.projetofirebase.view.telaprincipal.TelaPrincipal
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormLogin : AppCompatActivity() {

    // Variável que elimina o uso do findViewById
    private lateinit var binding: ActivityFormLoginBinding

    // Variável que faz a conexão e recupera a instacia do backend do firebase
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            it.fecharTeclado()

            if(email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(it, R.string.msg_campo_vazio ,Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                // Pego o retorno da instancia comparando o email e senha e uso um metodo de sucesso que retorna uma tarefa de autenticação
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener {
                    // Verifico se o retorno da tarefa foi bem sucedida
                    if(it.isSuccessful) {
                        val intent = Intent(this, TelaPrincipal::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    val mensagemErro = when(exception) {
                        is FirebaseAuthInvalidCredentialsException -> R.string.msg_erro_nao_cadastrado
                        is FirebaseNetworkException -> R.string.msg_erro_conexao // Foi adicionado uma permission no manifest
                        else -> R.string.msg_erro_login
                    }

                    val snackbar = Snackbar.make(it, mensagemErro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }
            }
        }

        binding.txtCadastrar.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)

        }
    }

    private fun View.fecharTeclado() {
        val input = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(windowToken, 0)
    }

    // Metodo nativo que é iniciado junto com a aplicação. Ele verifica se o usuário já fez o login em algum momento e caso sim, mantem esse login ativo
    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if(usuarioAtual != null) {
            val intent = Intent(this, TelaPrincipal::class.java)
            startActivity(intent)
            finish()
        }
    }
}