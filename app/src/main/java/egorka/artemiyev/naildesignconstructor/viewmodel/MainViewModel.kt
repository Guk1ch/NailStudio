package egorka.artemiyev.naildesignconstructor.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
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
}