package egorka.artemiyev.naildesignconstructor.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.model.FireImageModel
import egorka.artemiyev.naildesignconstructor.model.MasterWork
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GalleryViewModel : ViewModel(){
    val isRequestComplete = MutableLiveData<Boolean>()
    var listFull = MutableLiveData<MasterWorkList>()
    var listFavorite = MutableLiveData<MasterWorkList>()

    fun getListFull(){
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
    fun getListFavorite(){
        listFavorite.value = App.dm.getListFavorite()
    }
}