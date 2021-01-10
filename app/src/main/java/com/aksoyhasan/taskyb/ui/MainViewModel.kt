package com.aksoyhasan.taskyb.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aksoyhasan.taskyb.YoorBitApp
import com.aksoyhasan.taskyb.model.GetAllCitiesWithInfoResponse
import com.aksoyhasan.taskyb.model.GetHealthCheckForTheServiceResponse
import com.aksoyhasan.taskyb.model.GetRecordByIdResponse
import com.aksoyhasan.taskyb.repository.MainRepository
import com.aksoyhasan.taskyb.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel(app: Application,
                    private val mainRepository: MainRepository
) : AndroidViewModel(app) {

    val getAllCitiesWithInfoRes: MutableLiveData<Resource<GetAllCitiesWithInfoResponse>> = MutableLiveData()
    var getAllCitiesWithInfoResponse: GetAllCitiesWithInfoResponse? = null

    val getHealthCheckForTheServiceRes: MutableLiveData<Resource<GetHealthCheckForTheServiceResponse>> = MutableLiveData()
    var getHealthCheckForTheServiceResponse: GetHealthCheckForTheServiceResponse? = null

    val getRecordByIdRes: MutableLiveData<Resource<GetRecordByIdResponse>> = MutableLiveData()
    var timelinePage = 1
    var getRecordByIdResponse: GetRecordByIdResponse? = null

    fun getAllCitiesWithInfo() = viewModelScope.launch {
        safeGetAllCitiesWithInfoCall()
    }

    fun getHealthCheckForTheService() = viewModelScope.launch {
        safeHealthCheckForTheServiceCall()
    }

    fun getRecordById() = viewModelScope.launch {
        safeGetRecordByIdCall()
    }

    private suspend fun safeGetAllCitiesWithInfoCall() {
        getAllCitiesWithInfoRes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = mainRepository.getAllCitiesWithInfo()
                getAllCitiesWithInfoRes.postValue(handleGetAllCitiesWithInfoCallResponse(response))
            } else {
                getAllCitiesWithInfoRes.postValue(Resource.Error("İnternet bağlantısı yok!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getAllCitiesWithInfoRes.postValue(Resource.Error("Network Hatası \n" +
                        "$t"))
                else -> getAllCitiesWithInfoRes.postValue(Resource.Error("Bir hata oluştu, yeniden deneyiniz."))
            }
        }
    }

    private suspend fun safeHealthCheckForTheServiceCall() {
        getHealthCheckForTheServiceRes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = mainRepository.getHealthCheckForTheService()
                getHealthCheckForTheServiceRes.postValue(handleHealthCheckForTheServiceResponse(response))
            } else {
                getHealthCheckForTheServiceRes.postValue(Resource.Error("İnternet bağlantısı yok!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getHealthCheckForTheServiceRes.postValue(Resource.Error("Network Hatası \n$t"))
                else -> getHealthCheckForTheServiceRes.postValue(Resource.Error("Bir hata oluştu, yeniden deneyiniz."))
            }
        }
    }

    private suspend fun safeGetRecordByIdCall() {
        getRecordByIdRes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = mainRepository.getRecordById(timelinePage)
                getRecordByIdRes.postValue(handleGetRecordByIdResponse(response))
            } else {
                getRecordByIdRes.postValue(Resource.Error("İnternet bağlantısı yok!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getRecordByIdRes.postValue(Resource.Error("Network Hatası \n" +
                        "$t"))
                else -> getRecordByIdRes.postValue(Resource.Error("Bir hata oluştu, yeniden deneyiniz."))
            }
        }
    }

    private fun handleGetAllCitiesWithInfoCallResponse(response: Response<GetAllCitiesWithInfoResponse>): Resource<GetAllCitiesWithInfoResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(getAllCitiesWithInfoResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleHealthCheckForTheServiceResponse(response: Response<GetHealthCheckForTheServiceResponse>): Resource<GetHealthCheckForTheServiceResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(getHealthCheckForTheServiceResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleGetRecordByIdResponse(response: Response<GetRecordByIdResponse>): Resource<GetRecordByIdResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(getRecordByIdResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<YoorBitApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}