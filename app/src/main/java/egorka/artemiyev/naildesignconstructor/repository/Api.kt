package egorka.artemiyev.naildesignconstructor.repository

import com.google.gson.GsonBuilder
import egorka.artemiyev.naildesignconstructor.model.ListClients
import egorka.artemiyev.naildesignconstructor.model.MasterWork
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
import egorka.artemiyev.naildesignconstructor.model.NailImage
import egorka.artemiyev.naildesignconstructor.model.SqlClient
import egorka.artemiyev.naildesignconstructor.model.SqlClientRegistration
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @GET("MasterWorks")
    fun getAllWorks() : Observable<MasterWorkList>

    @POST("MasterWorks")
    fun addWork(@Body masterWork: MasterWork) : Observable<MasterWorkList>

    @GET("Nails")
    fun getNailImages() : Observable<NailImage>

    @POST("Clients")
    fun createUser(@Body client: SqlClientRegistration, @Header("UserKey") userKey: String = "magnatjdimenya") : Observable<SqlClient>

    companion object{
        fun createApi() : Api{
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://194.67.111.16/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(Api::class.java)
        }
    }
}