package com.example.app_tarefas_diarias.activitys

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import com.example.app_tarefas_diarias.model.AdapterTarefa
import com.example.app_tarefas_diarias.model.ViewModel
import kotlinx.android.synthetic.main.activity_tarefa.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ActivityTarefa : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAdapterTarefa: AdapterTarefa
    private val mViewModel: ViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        val recycler = findViewById<RecyclerView>(R.id.recycler_tarefas)
        recycler.layoutManager = LinearLayoutManager(this)
        mAdapterTarefa = AdapterTarefa(application)
        recycler.adapter = mAdapterTarefa

        searchTarefa()
        listener()
        observe()
    }

    private fun searchTarefa(){
        mViewModel.getTarefas()
    }

    private fun observe(){
        mViewModel.listTarefa.observe(this, {
            when (it.size) {
                0 -> {
                    progress_tarefa.visibility = View.INVISIBLE
                    text_preguica.visibility = View.VISIBLE
                    image_preguica.visibility = View.VISIBLE
                }
                else -> {
                    mAdapterTarefa.udateTarefas(it)
                    progress_tarefa.visibility = View.GONE
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
            image_toolbar_tarefa -> dialogPoint()
            float_bottom_tarefa -> dialogPoint()
        }
    }

    private fun dialogPoint(){

        val inflateView = layoutInflater.inflate(R.layout.dialog_add_tarefa, null)
        val descriptionEdit = inflateView.findViewById<EditText>(R.id.text_tarefa)
        val dateText = inflateView.findViewById<TextView>(R.id.text_data_dialog)
        val horaText = inflateView.findViewById<TextView>(R.id.text_hora_dialog)
        val calendar = inflateView.findViewById<ImageView>(R.id.image_caledar)
        val clock = inflateView.findViewById<ImageView>(R.id.image_clock)

        // Captura data atual
        val date = Calendar.getInstance().time
        val dateTime = SimpleDateFormat("dd-MM-YYYY", Locale.ENGLISH)
        val dateCurrent = dateTime.format(date)
        dateText.text = dateCurrent

        // Captures hora atual
        val hora = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val hourCurrent = hora.format(date)
        horaText.text = hourCurrent

        //Captura hora selecionada
        calendar.setOnClickListener {
            val data = Calendar.getInstance()
            val dataTime = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                data.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                data.set(Calendar.MONTH, month)
                data.set(Calendar.YEAR, year)

                dateText.text = SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(data.time)
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
                horaText.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(
                this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true
            ).show()
        }

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.Add_tarefa))
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(getString(R.string.salvar)) { _, _ ->

            val description = descriptionEdit.text.toString()
            when (description){
                "" -> Toast.makeText(this, "Preencha a descrição", Toast.LENGTH_SHORT).show()
                else -> saveTarefa("0", description, dateText.text.toString(), horaText.text.toString())
            }
        }
        alertDialog.setNegativeButton(getString(R.string.Cancelar)) { _, _ ->
            Toast.makeText(this, R.string.cancelado, Toast.LENGTH_SHORT).show()
        }
        alertDialog.create().show()
    }

    private fun saveTarefa(complete: String, descrip: String, date: String, hora: String){
        mViewModel.setTarefas(complete, descrip, date, hora)
        mViewModel.result.observe(this, {
            when(it){
                true -> Toast.makeText(this, R.string.adicionado_sucesso, Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(this, R.string.nao_adicionado, Toast.LENGTH_SHORT).show()
            }
        })
        searchTarefa()
    }
}