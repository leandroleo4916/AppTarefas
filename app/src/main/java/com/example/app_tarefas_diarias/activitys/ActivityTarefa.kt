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
        captureDate()
        captureHora()
    }

    private fun captureDate(): String{
        val calendar = Calendar.getInstance().time
        val dateTime = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dateCurrent = dateTime.format(calendar)
        date_toolbar.text = dateCurrent.toString()
        return dateCurrent
    }

    private fun captureHora(): String{
        val calendar = Calendar.getInstance().time
        val hora = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val hourCurrent = hora.format(calendar)
        hora_toolbar.text = hourCurrent.toString()
        return hourCurrent
    }

    override fun onEditClick(position: Int) {
        val name = recycler_tarefas[position].text_nome_tarefa.text.toString()
        val date = recycler_tarefas[position].text_data_tarefa.text.toString()
        val hora = recycler_tarefas[position].text_hora_tarefa.text.toString()
        dialogAddTarefa(name, date, hora)
    }

    override fun onDeleteClick(position: Int) {
        val name = recycler_tarefas[position].text_nome_tarefa.text.toString()
        deleteTarefa(name)
    }

    override fun onCompleteClick(position: Int) {
        val name = recycler_tarefas[position].text_nome_tarefa.text.toString()
        val complete = recycler_tarefas[position].complete_tarefa.tag
        if (complete == 1){ editTarefaComplete("0", name) }
        else{ editTarefaComplete("1", name) }
    }

    private fun searchTarefaInit() {
        mViewModel.getTarefasInit()
    }

    private fun searchTarefa() {
        mViewModel.getTarefas()
    }

    private fun observe() {
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

    private fun listener() {
        filter_tarefa.setOnClickListener(this)
        add_tarefa.setOnClickListener(this)
        option_tarefa.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            option_tarefa -> dialogOption()
            filter_tarefa -> dialogFilter()
            add_tarefa -> dialogAddTarefa("", "", "")
        }
    }

    private fun dialogOption() {
        val menuOption = PopupMenu(this, option_tarefa)
        menuOption.menuInflater.inflate(R.menu.menu, menuOption.menu)
        menuOption.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout_app_menu -> finish() }
            true
        }
        menuOption.show()
    }

    private fun dialogFilter() {
        val menuOption = PopupMenu(this, filter_tarefa)
        menuOption.menuInflater.inflate(R.menu.popup, menuOption.menu)
        menuOption.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.todas -> searchTarefa()
                R.id.completas -> mViewModel.getTarefasCompleteOrIncomplete("1")
                R.id.incompletas -> mViewModel.getTarefasCompleteOrIncomplete("0")
            }
            true
        }
        menuOption.show()
    }

    private fun dialogAddTarefa(nameEdit: String, dateEdit: String, horaEdit: String) {

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

                SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(data.time)
            }
            DatePickerDialog(this, dataTime, data.get(Calendar.YEAR),
                data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH)).show()
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
                cal.get(Calendar.MINUTE), true).show()
        }

        if (nameEdit == "") {

            val date = captureDate()
            dateText.text = date

            val hora = captureHora()
            horaText.text = hora

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.Add_tarefa))
            alertDialog.setView(inflateView)
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(getString(R.string.salvar)) { _, _ ->

                val description = textTarefa.text.toString()
                when {
                    description == "" -> {
                        Toast.makeText(this, R.string.preencha,
                            Toast.LENGTH_SHORT).show()
                    }
                    mViewModel.getDescription(description) -> {
                        Toast.makeText(this, R.string.descricao_existe,
                            Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        saveTarefa("0", description, dateText.text.toString(),
                            horaText.text.toString())
                    }
                }
            }
            alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
                Toast.makeText(this, R.string.cancelado, Toast.LENGTH_SHORT).show()
            }
            alertDialog.create().show()

        } else {
            textTarefa.setText(nameEdit)
            dateText.text = dateEdit
            horaText.text = horaEdit

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.editar_tarefa))
            alertDialog.setView(inflateView)
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(R.string.editar) { _, _ ->

                val description = textTarefa.text.toString()
                when  {
                    description == "" -> {
                        Toast.makeText(this, R.string.preencha,
                            Toast.LENGTH_SHORT).show()
                    }
                    mViewModel.getDescription(description) -> {
                        Toast.makeText(this, R.string.descricao_existe,
                            Toast.LENGTH_SHORT).show()
                    }
                    else -> editTarefa("0",
                        nameEdit,
                        description,
                        dateText.text.toString(),
                        horaText.text.toString())
                }
            }
            alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
                Toast.makeText(this, R.string.cancelado, Toast.LENGTH_SHORT).show()
            }
            alertDialog.create().show()
        }
    }

    private fun saveTarefa(complete: String, descrip: String, date: String, hora: String) {

        when {
            mViewModel.setTarefas(complete, descrip, date, hora) -> {
                Toast.makeText(this, R.string.adicionado_sucesso,
                    Toast.LENGTH_SHORT).show()
                searchTarefa()
                captureHora()
            }
            else -> Toast.makeText(this, R.string.nao_adicionado,
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun editTarefa(complete: String, name: String, nameEdit: String, date: String,
                           hora: String, ) {

        when {
            mViewModel.editTarefas(complete, name, nameEdit, date, hora) -> {
                Toast.makeText(this, R.string.editado_sucesso, Toast.LENGTH_SHORT).show()
                searchTarefa()
                captureHora()
            }
            else -> {
                Toast.makeText(this, R.string.nao_editado, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editTarefaComplete(complete: String, name: String) {

        when {
            mViewModel.editTarefasComplete(complete, name) -> {
                if (complete == "1"){
                    Toast.makeText(this, R.string.completa, Toast.LENGTH_SHORT).show()
                }
                else {Toast.makeText(this, R.string.incompleta, Toast.LENGTH_SHORT).show()}
                searchTarefa()
                captureHora()
            }
            else -> {
                Toast.makeText(this, R.string.nao_editado, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteTarefa(descrip: String) {
        when {
            mViewModel.deleteTarefas(descrip) -> {
                Toast.makeText(this, R.string.excluido_sucesso, Toast.LENGTH_SHORT).show()
                searchTarefa()
                captureHora()
            }
            else -> {
                Toast.makeText(this, R.string.nao_excluido, Toast.LENGTH_SHORT).show()
            }
        }
    }
}