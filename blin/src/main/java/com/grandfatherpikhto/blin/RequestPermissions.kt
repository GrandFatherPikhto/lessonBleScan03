package com.grandfatherpikhto.blin

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class RequestPermissions constructor(private val activity: AppCompatActivity) : DefaultLifecycleObserver {
    companion object {
        private val mutableStateFlowPermission = MutableStateFlow<Pair<Boolean, String>?>(null)
        val stateFlowPermission get() = mutableStateFlowPermission.asStateFlow()
        val valuePermission get() = mutableStateFlowPermission.value
    }

    private val requestedPermissions = mutableListOf<Pair<Boolean, String>>()

    /**
     * Запрос группы разрешений
     * Ланчер необходимо вынести в глобальные переменные, потому что
     * он должен быть инициализирован ДО запуска Активности.
     * В противном случае, будет ошибка запроса, если мы вздумаем
     * перезапросить разрешения после запуска полного запуска приложения
     */
    private val permissionsLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results?.entries?.forEach { result ->
                val name = result.key
                val granted = result.value
                mutableStateFlowPermission.tryEmit(Pair(granted, name))
            }
        }

    fun requestPermissions(permissions: List<String> = listOf()) {
        val requestPermissions = mutableListOf<String>()

        permissions.forEach { permission ->
            if(activity.checkSelfPermission(permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions.add(permission)
            }
        }

        if(requestPermissions.isNotEmpty()) {
            permissionsLauncher.launch(requestPermissions.toTypedArray())
        }
    }

    init {
        requestedPermissions.clear()
        CoroutineScope(Dispatchers.IO).launch {
            stateFlowPermission.filterNotNull().collect { permission ->
                requestedPermissions.add(permission)
            }
        }
    }
}