package br.universidade.approomproduto.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.universidade.approomproduto.databinding.ActivityListaBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carregarProdutos()
    }

    // Recupera todos os documentos da coleção "Produtos"
    private fun carregarProdutos() {
        FirebaseFirestore.getInstance()
            .collection("Produtos")
            .get()
            .addOnSuccessListener { resultado ->
                val itens = ArrayList<String>()
                for (doc in resultado) {
                    val nome = doc.getString("nome") ?: ""
                    val categoria = doc.getString("categoria") ?: ""
                    val preco = doc.getDouble("preco") ?: 0.0
                    itens.add(nome + "\nCategoria: " + categoria + "\nPreço: R$ " + "%.2f".format(preco))
                }

                binding.listProdutos.adapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, itens)
                binding.txtVazio.visibility = if (itens.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar: " + e.message, Toast.LENGTH_LONG).show()
            }
    }
}
