package br.universidade.approomproduto.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.universidade.approomproduto.databinding.ActivityCadastroBinding
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSalvar.setOnClickListener { salvarProduto() }
    }

    private fun salvarProduto() {
        val nome = binding.edtNome.text.toString().trim()
        val categoria = binding.edtCategoria.text.toString().trim()
        val preco = binding.edtPreco.text.toString().trim().replace(',', '.').toDoubleOrNull()

        var valido = true
        if (nome.isEmpty()) {
            binding.edtNome.error = "Informe o nome"
            valido = false
        }
        if (categoria.isEmpty()) {
            binding.edtCategoria.error = "Informe a categoria"
            valido = false
        }
        if (preco == null) {
            binding.edtPreco.error = "Informe um preço válido"
            valido = false
        }
        if (!valido || preco == null) return

        val produto = hashMapOf(
            "nome" to nome,
            "categoria" to categoria,
            "preco" to preco
        )

        // Coleção "Produtos"; o documento recebe um ID gerado automaticamente
        FirebaseFirestore.getInstance()
            .collection("Produtos")
            .add(produto)
            .addOnSuccessListener {
                Toast.makeText(this, "Produto cadastrado!", Toast.LENGTH_SHORT).show()
                binding.edtNome.setText("")
                binding.edtCategoria.setText("")
                binding.edtPreco.setText("")
                binding.edtNome.requestFocus()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao cadastrar: " + e.message, Toast.LENGTH_LONG).show()
            }
    }
}
