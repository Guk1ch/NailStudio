package egorka.artemiyev.naildesignconstructor.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import egorka.artemiyev.naildesignconstructor.model.FireImageModel

class GalleryViewModel : ViewModel(){
    var listFull = MutableLiveData<MutableList<FireImageModel>>()
    var listFavorite = MutableLiveData<MutableList<FireImageModel>>()
    var listMy = MutableLiveData<List<FireImageModel>>()

    private fun getListFull(){
        listFull.value = mutableListOf()
        for (i in 1..20){
            listFull.value?.add(FireImageModel(i, "", true))
        }
    }
    fun fillListFavorite() : MutableList<FireImageModel>{
        getListFull()
        listFavorite.value = mutableListOf()
        listFull.value?.forEach { item ->
            if (item.isInFavorite) listFavorite.value!!.add(item)
        }
        return listFavorite.value ?: mutableListOf()
    }
}