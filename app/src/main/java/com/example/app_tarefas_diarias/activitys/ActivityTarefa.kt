package com.example.app_tarefas_diarias.activitys

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import com.example.app_tarefas_diarias.entity.EntityTarefaDateAndHora
import com.example.app_tarefas_diarias.interfaces.OnItemClickListener
import com.example.app_tarefas_diarias.model.AdapterTarefa
import com.example.app_tarefas_diarias.model.ViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_tarefa.*
import kotlinx.android.synthetic.main.recycler_tarefas.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ActivityTarefa : FragmentActivity(), View.OnClickListener, OnItemClickListener {

    private lateinit var adapterTarefa: AdapterTarefa
    private val viewModel: ViewModel by viewModel()
    private lateinit var coordinator: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        val recycler = findViewById<RecyclerView>(R.id.recycler_tarefas)
        recycler.layoutManager = LinearLayoutManager(this)
        adapterTarefa = AdapterTarefa(application, this)
        recycler.adapter = adapterTarefa

        coordinator = findViewById(R.id.container_tarefas)

        searchTarefaInit()
        listener()
        observe()
        updateDateHour()

        searchTarefaDateAndHora()
        getDateAndHora()
    }

    private fun updateDateHour(){
        val delay: Long = 0
        val interval: Long = 60000
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                captureDate()
                captureHora()
                searchTarefaDateAndHora()
            } }, delay, interval)
    }

    private fun getDateAndHora() {

        viewModel.listTarefaDateAndHora.observe(this, {
            when (it.size) {
                0 -> { next_tarefa.text = getString(R.string.Nenhuma_tarefa_prox_dias)}
                else -> calcularDateAndHora(it)
            }
        })
    }

    private fun calcularDateAndHora(it: ArrayList<EntityTarefaDateAndHora>) {

        val dateCurrent = captureDate()
        val dateS = dateCurrent.split("/")
        val horaCurrent = captureHora()
        val horaS = horaCurrent.split(":")
        val result: ArrayList<Long> = arrayListOf()

        for (i in it) {
            val dSplit = i.date.split("/")
            val hSplit = i.hora.split(":")

            val dateHours = LocalDateTime.of(Integer.parseInt(dSplit[2]),
                Integer.parseInt(dSplit[1]), Integer.parseInt(dSplit[0]),
                Integer.parseInt(hSplit[0]), Integer.parseInt(hSplit[1]))

            val current = LocalDateTime.of(Integer.parseInt(dateS[2]),
                Integer.parseInt(dateS[1]), Integer.parseInt(dateS[0]),
                Integer.parseInt(horaS[0]), Integer.parseInt(horaS[1]))

            val duration = Duration.between(current, dateHours)
            result.add(duration.toMinutes())
        }

        val minutes = result.minOrNull()
        val minuteDay = 1440

        when {
            minutes!! <= 0 -> {
                next_tarefa.text = getString(R.string.tarefas_dias_atras)
            }
            minutes < minuteDay && minutes in 1..60 -> {
                next_tarefa.text = "Próxima tarefa em $minutes minutos"
            }
            minutes in 61..minuteDay -> {
                var min = minutes
                var hour = 0
                while (min > 60){
                    min -= 60
                    hour++
                }
                when (hour) {
                    1 -> next_tarefa.text = "Próxima tarefa em $hour hora e $min minutos"
                    else -> next_tarefa.text = "Próxima tarefa em $hour horas e $min minutos"
                }
            }
            else -> {
                var min = minutes
                var day = 0

                while (min > minuteDay) {
                    min -= minuteDay
                    day++
                }

                when {
                    min > 60 -> {
                        var minRest = min
                        var hour = 0
                        while (minRest > 60){
                            minRest -= 60
                            hour++
                        }
                        when (day) {
                            1 -> {
                                next_tarefa.text =
                                    "Próxima tarefa em $day dia, $hour horas e $minRest minutos"
                            }
                            else -> {
                                next_tarefa.text =
                                    "Próxima tarefa em $day dias, $hour horas e $minRest minutos"
                            }
                        }
                    }
                    else -> {
                        next_tarefa.text = "Próxima tarefa em $day dia e $min minutos"
                    }
                }
            }
        }
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
        viewModel.getTarefasInit()
    }

    private fun searchTarefa() {
        viewModel.getTarefas()
    }

    private fun searchTarefaDateAndHora() {
        viewModel.getTarefasDateAndHora("0")
    }

    private fun observe() {
        viewModel.listTarefa.observe(this, {
            when (it.size) {
                0 -> {
                    adapterTarefa.updateTarefas(it)
                    progress_tarefa.visibility = View.GONE
                    text_preguica.visibility = View.VISIBLE
                    image_preguica.visibility = View.VISIBLE
                }
                else -> {
                    adapterTarefa.updateTarefas(it)
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
                R.id.completas -> viewModel.getTarefasCompleteOrIncomplete("1")
                R.id.incompletas -> viewModel.getTarefasCompleteOrIncomplete("0")
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

                dateText.text = SimpleDateFormat(
                    "dd/MM/yyyy", Locale.ENGLISH).format(data.time)
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
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
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
                    description.isBlank() -> {
                        showSnackBar(R.string.preencha)
                    }
                    viewModel.getDescription(description) -> {
                        showSnackBar(R.string.descricao_existe)
                    }
                    else -> {
                        saveTarefa(description, dateText.text.toString(), horaText.text.toString())
                    }
                }
            }
            alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
                showSnackBar(R.string.cancelado)
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
                    description.isBlank() -> {
                        showSnackBar(R.string.preencha)
                    }
                    else -> editTarefa(nameEdit, description, dateText.text.toString(),
                        horaText.text.toString())
                }
            }
            alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
                showSnackBar(R.string.cancelado)
            }
            alertDialog.create().show()
        }
    }

    private fun saveTarefa(descrip: String, date: String, hora: String) {

        when {
            viewModel.setTarefas( "0", descrip, date, hora) -> {
                showSnackBar(R.string.adicionado_sucesso)
                searchTarefa()
                searchTarefaDateAndHora()
            }
            else -> showSnackBar(R.string.nao_adicionado)
        }
    }

    private fun editTarefa(name: String, nameEdit: String, date: String, hora: String) {

        when {
            viewModel.editTarefas("0", name, nameEdit, date, hora) -> {
                showSnackBar(R.string.editado_sucesso)
                searchTarefa()
                searchTarefaDateAndHora()
            }
            else -> {
                showSnackBar(R.string.nao_editado)
            }
        }
    }

    private fun editTarefaComplete(complete: String, name: String) {

        when {
            viewModel.editTarefasComplete(complete, name) -> {
                if (complete == "1"){
                    showSnackBar(R.string.completa)
                }
                else showSnackBar(R.string.incompleta)
                searchTarefa()
                searchTarefaDateAndHora()
            }
            else -> {
                showSnackBar(R.string.nao_editado)
            }
        }
    }

    private fun deleteTarefa(descrip: String) {
        when {
            viewModel.deleteTarefas(descrip) -> {
                showSnackBar(R.string.excluido_sucesso)
                searchTarefa()
                searchTarefaDateAndHora()
            }
            else -> {
                showSnackBar(R.string.nao_excluido)
            }
        }
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(coordinator,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}