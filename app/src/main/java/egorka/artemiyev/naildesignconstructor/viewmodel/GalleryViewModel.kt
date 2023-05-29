package egorka.artemiyev.naildesignconstructor.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.model.FireImageModel

class GalleryViewModel : ViewModel(){
    var listFull = MutableLiveData<MutableList<FireImageModel>>()
    var listFavorite = MutableLiveData<MutableList<FireImageModel>>()
    var listMy = MutableLiveData<List<FireImageModel>>()

    fun getListFull(){
        listFull.value = mutableListOf()
        for (i in 1..20){
            listFull.value?.add(FireImageModel(i, ""))
        }
    }
    fun getListFavorite(){
        var emptyFavorite = mutableListOf<FireImageModel>()
        val serializedObject: String = App.dm.getListFavorite()
        val type = object : TypeToken<List<FireImageModel?>?>() {}.type
        emptyFavorite = Gson().fromJson(serializedObject, type)

        listFavorite.value = emptyFavorite
    }
}