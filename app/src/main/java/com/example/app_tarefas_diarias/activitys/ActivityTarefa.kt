package com.example.app_tarefas_diarias.activitys

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import com.example.app_tarefas_diarias.interfaces.OnItemClickListener
import com.example.app_tarefas_diarias.model.AdapterTarefa
import com.example.app_tarefas_diarias.model.ViewModel
import kotlinx.android.synthetic.main.activity_tarefa.*
import kotlinx.android.synthetic.main.recycler_tarefas.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ActivityTarefa : FragmentActivity(), View.OnClickListener, OnItemClickListener {

    private lateinit var mAdapterTarefa: AdapterTarefa
    private val mViewModel: ViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        val recycler = findViewById<RecyclerView>(R.id.recycler_tarefas)
        recycler.layoutManager = LinearLayoutManager(this)
        mAdapterTarefa = AdapterTarefa(application, this)
        recycler.adapter = mAdapterTarefa

        searchTarefaInit()
        listener()
        observe()
    }

    override fun onItemClick(position: Int) {

        recycler_tarefas[position].delete_tarefa.setOnClickListener {
            val name = recycler_tarefas[position].text_nome_tarefa.text.toString()
            deleteTarefa(name, position)
        }

        recycler_tarefas[position].edit_tarefa.setOnClickListener {
            val name = recycler_tarefas[position].text_nome_tarefa.text.toString()
            val date = recycler_tarefas[position].text_data_tarefa.text.toString()
            val hora = recycler_tarefas[position].text_hora_tarefa.text.toString()
            dialogAddTarefa(name, date, hora)
        }
    }

    private fun searchTarefaInit(){
        mViewModel.getTarefasInit()
    }

    private fun searchTarefa(){
        mViewModel.getTarefas()
    }

    private fun observe(){
        mViewModel.listTarefa.observe(this, {
            when (it.size) {
                0 -> {
                    mAdapterTarefa.updateTarefas(it)
                    progress_tarefa.visibility = View.GONE
                    text_preguica.visibility = View.VISIBLE
                    image_preguica.visibility = View.VISIBLE
                }
                else -> {
                    mAdapterTarefa.updateTarefas(it)
                    progress_tarefa.visibility = View.GONE
                    text_preguica.visibility = View.GONE
                    image_preguica.visibility = View.GONE
                }
            }
        })
    }

    private fun listener(){
        image_toolbar_tarefa.setOnClickListener(this)
        float_bottom_tarefa.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_toolbar_tarefa -> dialogAddTarefa("", "", "")
            float_bottom_tarefa -> dialogAddTarefa("", "", "")
        }
    }

    private fun dialogAddTarefa(nameEdit: String, dateEdit: String, horaEdit: String){

        val inflateView = layoutInflater.inflate(R.layout.dialog_add_tarefa, null)
        val textTarefa = inflateView.findViewById<EditText>(R.id.text_tarefa)
        val dateText = inflateView.findViewById<TextView>(R.id.text_data_dialog)
        val horaText = inflateView.findViewById<TextView>(R.id.text_hora_dialog)
        val calendar = inflateView.findViewById<ImageView>(R.id.image_caledar)
        val clock = inflateView.findViewById<ImageView>(R.id.image_clock)

        // Captura data selecionada
        calendar.setOnClickListener {
            val data = Calendar.getInstance()
            val dataTime = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                data.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                data.set(Calendar.MONTH, month)
                data.set(Calendar.YEAR, year)

                dateText.text = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(data.time)
            }
            DatePickerDialog(
                this, dataTime, data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Captura hora selecionada
        clock.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                horaText.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(cal.time)
            }
            TimePickerDialog(
                this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true
            ).show()
        }

        if (nameEdit == ""){

            // Captures current date
            val date = Calendar.getInstance().time
            val dateTime = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val dateCurrent = dateTime.format(date)
            dateText.text = dateCurrent.toString()

            // Captures current hour
            val hora = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val hourCurrent = hora.format(date)
            horaText.text = hourCurrent.toString()

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.Add_tarefa))
            alertDialog.setView(inflateView)
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(getString(R.string.salvar)) { _, _ ->

                when (val description = textTarefa.text.toString()){
                    "" -> Toast.makeText(this, R.string.preencha, Toast.LENGTH_SHORT).show()
                    else -> saveTarefa("0", description, dateText.text.toString(), horaText.text.toString())
                }
            }
            alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
                Toast.makeText(this, R.string.cancelado, Toast.LENGTH_SHORT).show()
            }
            alertDialog.create().show()
        }

        else {
            textTarefa.setText(nameEdit)
            dateText.text = dateEdit
            horaText.text = horaEdit

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.editar_tarefa))
            alertDialog.setView(inflateView)
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(getString(R.string.editar)) { _, _ ->
                when (val description = textTarefa.text.toString()){
                    "" -> Toast.makeText(this, R.string.preencha, Toast.LENGTH_SHORT).show()
                    else -> editTarefa("0", nameEdit, description, dateText.text.toString(), horaText.text.toString())
                }
            }
            alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
                Toast.makeText(this, R.string.cancelado, Toast.LENGTH_SHORT).show()
            }
            alertDialog.create().show()
        }
    }

    private fun saveTarefa(complete: String, descrip: String, date: String, hora: String){

            when{
                mViewModel.setTarefas(complete, descrip, date, hora) -> {
                    Toast.makeText(this, R.string.adicionado_sucesso, Toast.LENGTH_SHORT).show()
                    searchTarefa()
                }
                else -> Toast.makeText(this, R.string.nao_adicionado, Toast.LENGTH_SHORT).show()
            }
    }

    private fun editTarefa(complete: String, descrip: String, nameEdit: String, date: String, hora: String){

            when {
                mViewModel.editTarefas(complete, descrip, nameEdit, date, hora) -> {
                    Toast.makeText(this, R.string.editado_sucesso, Toast.LENGTH_SHORT).show()
                    searchTarefa()
                }
                else -> { Toast.makeText(this, R.string.nao_editado, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun deleteTarefa(descrip: String, position: Int){

            when {
                mViewModel.deleteTarefas(descrip) -> {
                    mAdapterTarefa.updatePosition(position)
                    Toast.makeText(this, R.string.excluido_sucesso, Toast.LENGTH_SHORT).show()
                    searchTarefa()
                }
                else -> {
                    Toast.makeText(this, R.string.nao_excluido, Toast.LENGTH_SHORT).show()
                }
            }
    }
}