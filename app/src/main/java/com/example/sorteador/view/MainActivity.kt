package com.example.sorteador.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sorteador.R
import com.example.sorteador.databinding.ActivityMainBinding
import com.example.sorteador.model.Draw
import java.lang.NumberFormatException

class MainActivity: AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private var draw = Draw()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListener()
    }

    override fun onClick(v: View) {
        when (v) {
            binding.buttonUseLimit -> {
                val limit: Int? = try {
                    binding.editLimit.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    null
                }
                if (limit != null && limit > 1) {
                    draw = Draw(limit)
                    updateUI()
                } else {
                    Toast.makeText(
                        this,
                        "Por favor, ingrese un límite válido mayor que 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            binding.buttonDraw -> {
                try {
                    binding.textviewExit.text = draw.getNumber().toString()
                    updateListview()
                } catch (e: IllegalStateException) {
                    // Manejar la excepción
                    showErrorDialog("¡Todos los números posibles dentro del límite ya han sido seleccionados!")
                }
            }
        }
    }


    private fun setClickListener() {
        binding.buttonDraw.setOnClickListener(this)
        binding.buttonUseLimit.setOnClickListener(this)
    }

    private fun updateUI() {
        val str = String.format("Intervalo de 1 a %, d.", draw.getHighBorder())
        binding.textviewInterval.text = str
        binding.editLimit.text.clear()
        binding.textviewExit.text = getString(R.string.inicie_o_sorteio)
        updateListview()
    }

    private fun updateListview() {
        val adapter: ArrayAdapter<Int> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            draw.getHistory()
        )
        binding.listviewDraw.adapter = adapter
    }

    private fun showErrorDialog(errorMessage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("Ok") { dialog, _ ->
                if (dialog is DialogInterface) {
                    dialog.dismiss()
                }
            }
        val dialog = builder.create()
        dialog.show()
    }
}