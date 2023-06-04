package egorka.artemiyev.naildesignconstructor.viewmodel

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.InputDateDialogViewBinding
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
import egorka.artemiyev.naildesignconstructor.model.Record
import egorka.artemiyev.naildesignconstructor.model.SqlClient
import egorka.artemiyev.naildesignconstructor.model.utils.Case.listClients
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    var listFull = MutableLiveData<MasterWorkList>()
    val isRequestComplete = MutableLiveData<Boolean>()

    fun getListFull() {
        val disp = App.dm.api.getAllWorks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.reverse()
                listFull.value = it
            }, {
                Log.d("абибка", it.message.toString())
            }, {
                isRequestComplete.value = true
            })
    }
    fun makeRecord(time: String, date: String, context: Context){
        val key = App.dm.getUserKey()
        var user = SqlClient("skgjhdkfjg", 0, "999999999", listOf(), key)
        for (i in listClients){
            if (i.userKey == key) {
                user = i
            }
        }
        val disp = App.dm.api.addRecord(Record(0, 0, user.id, date, user, false, time))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show()
            }, {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            })
    }
}