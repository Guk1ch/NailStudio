package egorka.artemiyev.naildesignconstructor.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.model.Record
import egorka.artemiyev.naildesignconstructor.model.RecordsList
import egorka.artemiyev.naildesignconstructor.model.SqlClient
import egorka.artemiyev.naildesignconstructor.model.utils.Case
import egorka.artemiyev.naildesignconstructor.model.utils.Case.listClients
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Date


class RecordsViewModel : ViewModel() {
    var trueList = mutableListOf<Record>()
    val isListDone = MutableLiveData(false)
    fun getListRecords() {
        val disp = App.dm.api.getRecords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                trueList = it
                trueList.forEach { r ->
                    r.idClientNavigation = getClientsFromId(r.idClient)
                    if (r == trueList.last()) isListDone.value = true
                }
            }, {

            })
    }

    private fun getClientsFromId(idClient: Int): SqlClient {
        for (i in listClients) if (i.id == idClient) return i
        return listClients[0]
    }

    fun makeRecord(time: String, date: String, context: Context){
        val user = Case.clientToRecord
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