package com.example.app_tarefas_diarias.activitys

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import com.example.app_tarefas_diarias.entity.EditTask
import com.example.app_tarefas_diarias.entity.EntityTask
import com.example.app_tarefas_diarias.entity.EntityTaskDateAndHora
import com.example.app_tarefas_diarias.interfaces.ITaskListener
import com.example.app_tarefas_diarias.interfaces.ITaskViewModel
import com.example.app_tarefas_diarias.interfaces.OnItemClickListener
import com.example.app_tarefas_diarias.model.AdapterTask
import com.example.app_tarefas_diarias.model.TasksViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_tarefa.*
import kotlinx.android.synthetic.main.recycler_tarefas.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

class ActivityTask : FragmentActivity(), ITaskViewModel, View.OnClickListener, OnItemClickListener,
    AppBarLayout.OnOffsetChangedListener {

    private lateinit var adapterTask: AdapterTask
    private val taskViewModel: TasksViewModel by viewModel()
    private lateinit var coordinator: CoordinatorLayout
    private val showTitleToolBar = 0.9f
    private val animationDuration = 200
    private var titleVisible = false
    private var textTitle: TextView? = null
    private var appBarLayout: AppBarLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        val recycler = findViewById<RecyclerView>(R.id.recycler_tarefas)
        recycler.layoutManager = LinearLayoutManager(this)
        adapterTask = AdapterTask(application, this)
        recycler.adapter = adapterTask

        instanceView()
        startAlphaAnimation(textTitle!!, 0, View.INVISIBLE)

        listener()
        getTask()
        observe()
        updateDateHour()
        getDateAndHora()
    }

    private fun instanceView(){
        coordinator = findViewById(R.id.container_tarefas)
        appBarLayout = findViewById(R.id.appbar)
        textTitle = findViewById(R.id.text_title)
        appBarLayout!!.addOnOffsetChangedListener(this)
    }

    private fun updateDateHour(){
        val delay: Long = 0
        val interval: Long = 60000
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                captureDate()
                getTaskDateAndHora("0")
            } }, delay, interval)
    }

    private fun getDateAndHora() {

        taskViewModel.listTaskDateAndHora.observe(this, {
            when (it.size) {
                0 -> { next_tarefa.text = getString(R.string.Nenhuma_tarefa_prox_dias) }
                else -> calculateDateAndHora(it)
            }
        })
    }

    private fun calculateDateAndHora(it: ArrayList<EntityTaskDateAndHora>) {

        val dateCurrent = returnDate()
        val dateS = dateCurrent.split("/")
        val horaCurrent = returnHora()
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
            minutes!! <= 0 -> { next_tarefa.text = getString(R.string.tarefas_atras) }
            minutes < minuteDay && minutes in 1..60 -> {
                setTextStatus("Pr??xima tarefa em $minutes minutos")
            }
            minutes in 61..minuteDay -> {
                var min = minutes
                var hour = 0
                while (min > 60){
                    min -= 60
                    hour++
                }
                when (hour) {
                    1 -> setTextStatus("Pr??xima tarefa em $hour hora e $min minutos")
                    else -> setTextStatus("Pr??xima tarefa em $hour horas e $min minutos")
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
                                setTextStatus("Pr??xima tarefa em $day dia, $hour horas e $minRest minutos")
                            }
                            else -> {
                                setTextStatus("Pr??xima tarefa em $day dias, $hour horas e $minRest minutos")
                            }
                        }
                    }
                    else -> {
                        setTextStatus("Pr??xima tarefa em $day dia e $min minutos")
                    }
                }
            }
        }
    }

    private fun setTextStatus(text: String){
        next_tarefa.text = text
    }

    private fun captureDate(){
        val calendar = Calendar.getInstance().time
        val local = Locale("pt", "BR")
        val date = SimpleDateFormat("E dd 'de' MMMM 'de' yyyy, HH:mm", local)
        val hourCurrent = date.format(calendar)
        date_toolbar.text = hourCurrent.toString()
    }

    private fun returnDate(): String{
        val calendar = Calendar.getInstance().time
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return date.format(calendar)
    }

    private fun returnHora(): String{
        val calendar = Calendar.getInstance().time
        val hora = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        return hora.format(calendar)
    }

    override fun onEditClick(position: Int) {
        val complete = recycler_tarefas[position].complete_tarefa.tag
        val name = recycler_tarefas[position].text_nome_tarefa.text.toString()
        val date = recycler_tarefas[position].text_data_tarefa.text.toString()
        val hora = recycler_tarefas[position].text_hora_tarefa.text.toString()
        dialogAddTask(complete.toString(), name, date, hora)
    }

    override fun onDeleteClick(position: Int) {
        val name = recycler_tarefas[position].text_nome_tarefa.text.toString()
        deleteTasks(name)
    }

    override fun onCompleteClick(position: Int) {
        val name = recycler_tarefas[position].text_nome_tarefa.text.toString()
        val complete = recycler_tarefas[position].complete_tarefa.tag
        if (complete == 1){ editTasksComplete("0", name) }
        else{ editTasksComplete("1", name) }
    }

    override fun getTask() {
        taskViewModel.getTask()
    }

    override fun getTaskDateAndHora(complete: String) {
        taskViewModel.getTaskDateAndHora(complete)
    }

    private fun observe() {
        taskViewModel.listTask.observe(this, {
            when (it.size) {
                0 -> {
                    adapterTask.updateTasks(it)
                    progress_tarefa.visibility = View.GONE
                    text_preguica.visibility = View.VISIBLE
                    image_preguica.visibility = View.VISIBLE
                }
                else -> {
                    adapterTask.updateTasks(it)
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
            add_tarefa -> dialogAddTask("","", "", "")
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
                R.id.todas -> getTask()
                R.id.completas -> taskViewModel.getTaskCompleteOrIncomplete("1")
                R.id.incompletas -> taskViewModel.getTaskCompleteOrIncomplete("0")
            }
            true
        }
        menuOption.show()
    }

    private fun dialogAddTask(complete: String, nameEdit: String, dateEdit: String, horaEdit: String) {

        val inflateView = layoutInflater.inflate(R.layout.dialog_add_tarefa, null)
        val textTask = inflateView.findViewById<EditText>(R.id.text_tarefa)
        val dateText = inflateView.findViewById<TextView>(R.id.text_data_dialog)
        val horaText = inflateView.findViewById<TextView>(R.id.text_hora_dialog)
        val calendar = inflateView.findViewById<ImageView>(R.id.image_caledar)
        val clock = inflateView.findViewById<ImageView>(R.id.image_clock)

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

            val date = returnDate()
            dateText.text = date
            val hora = returnHora()
            horaText.text = hora

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.Add_tarefa))
            alertDialog.setView(inflateView)
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(getString(R.string.salvar)) { _, _ ->

                val description = textTask.text.toString()
                when {
                    description.isBlank() -> showSnackBar(R.string.preencha)
                    taskViewModel.getDescription(description) -> {
                        showSnackBar(R.string.descricao_existe)
                    }
                    else -> {
                        setTasks(EntityTask(0, "0", description, dateText.text.toString(),
                            horaText.text.toString()))
                    }
                }
            }
            alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
                showSnackBar(R.string.cancelado)
            }
            alertDialog.create().show()

        } else {
            textTask.setText(nameEdit)
            dateText.text = dateEdit
            horaText.text = horaEdit

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.editar_tarefa))
            alertDialog.setView(inflateView)
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(R.string.editar) { _, _ ->

                val description = textTask.text.toString()
                when  {
                    description.isBlank() -> {
                        showSnackBar(R.string.preencha)
                    }
                    else -> editTasks(EditTask(complete, nameEdit, description,
                        dateText.text.toString(), horaText.text.toString()))
                }
            }
            alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
                showSnackBar(R.string.cancelado)
            }
            alertDialog.create().show()
        }
    }

    override fun setTasks(entityTask: EntityTask): Boolean {

        when {
            taskViewModel.setTasks(entityTask) -> {
                showSnackBar(R.string.adicionado_sucesso)
                getTask()
                getTaskDateAndHora("0")
            }
            else -> showSnackBar(R.string.nao_adicionado)
        }
        return true
    }

    override fun editTasks(editTask: EditTask): Boolean {

        when {
            taskViewModel.editTasks(editTask) -> {
                showSnackBar(R.string.editado_sucesso)
                getTask()
                getTaskDateAndHora("0")
            }
            else -> {
                showSnackBar(R.string.nao_editado)
            }
        }
        return true
    }

    override fun editTasksComplete(complete: String, name: String): Boolean {

        when {
            taskViewModel.editTasksComplete(complete, name) -> {
                if (complete == "1"){
                    showSnackBar(R.string.completa)
                }
                else showSnackBar(R.string.incompleta)
                getTask()
                getTaskDateAndHora("0")
            }
            else -> {
                showSnackBar(R.string.nao_editado)
            }
        }
        return true
    }

    override fun deleteTasks(description: String): Boolean {

        when {
            taskViewModel.deleteTasks(description) -> {
                showSnackBar(R.string.excluido_sucesso)
                getTask()
                getTaskDateAndHora("0")
            }
            else -> {
                showSnackBar(R.string.nao_excluido)
            }
        }
        return true
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

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(verticalOffset).toFloat() / maxScroll.toFloat()

        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= showTitleToolBar) {
            if (!titleVisible) {
                startAlphaAnimation(textTitle!!, animationDuration.toLong(), View.VISIBLE)
                titleVisible = true
            }
        } else {
            if (titleVisible) {
                startAlphaAnimation(textTitle!!, animationDuration.toLong(), View.INVISIBLE)
                titleVisible = false
            }
        }
    }

    private fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f)
                            else AlphaAnimation(1f, 0f)
        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }
}