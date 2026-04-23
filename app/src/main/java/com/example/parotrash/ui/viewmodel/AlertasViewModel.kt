package com.example.parotrash.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.modelos.Reporte
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlertasViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // --- CONFIGURACIÓN DE TIEMPO DE VIDA (en minutos) ---
    private val minutosDeVida = 1 // Las alertas se eliminarán después de 1 minuto
    // ----------------------------------------------------

    init {
        iniciarLimpiezaAutomatica()
    }

    /**
     * Inicia un bucle infinito en el viewModelScope que limpia alertas cada 30 segundos.
     */
    private fun iniciarLimpiezaAutomatica() {
        viewModelScope.launch {
            while (isActive) {
                limpiarAlertasAntiguas()
                delay(30000) // Verifica cada 30 segundos
            }
        }
    }

    /**
     * Incrementa las confirmaciones de una alerta.
     */
    fun confirmarAlerta(idAlerta: String) {
        val docRef = firestore.collection("reportes").document(idAlerta)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            if (snapshot.exists()) {
                val confirmacionesActuales = snapshot.getLong("confirmaciones") ?: 0L
                transaction.update(docRef, "confirmaciones", confirmacionesActuales + 1)
            }
        }.addOnFailureListener { e ->
            Log.e("AlertasViewModel", "Error al confirmar alerta: ${e.message}")
        }
    }

    /**
     * Incrementa los descartes de una alerta.
     * Si los descartes llegan a 10 o más, se elimina automáticamente.
     */
    fun descartarAlerta(idAlerta: String) {
        val docRef = firestore.collection("reportes").document(idAlerta)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            if (snapshot.exists()) {
                val descartesActuales = (snapshot.getLong("descartes") ?: 0L) + 1
                
                if (descartesActuales >= 10) {
                    transaction.delete(docRef)
                } else {
                    transaction.update(docRef, "descartes", descartesActuales)
                }
            }
        }.addOnSuccessListener {
            Log.d("AlertasViewModel", "Descarte procesado para: $idAlerta")
        }.addOnFailureListener { e ->
            Log.e("AlertasViewModel", "Error al descartar alerta: ${e.message}")
        }
    }

    /**
     * Limpia las alertas que tengan más de un tiempo determinado.
     */
    fun limpiarAlertasAntiguas() {
        viewModelScope.launch {
            try {
                val ahora = System.currentTimeMillis()
                val limite = ahora - (minutosDeVida * 60 * 1000)

                val querySnapshot = firestore.collection("reportes")
                    .whereLessThan("fechapublicacion", limite)
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    Log.d("AlertasViewModel", "No hay alertas antiguas para eliminar.")
                    return@launch
                }

                for (document in querySnapshot.documents) {
                    document.reference.delete().await()
                    Log.d("AlertasViewModel", "Reporte antiguo eliminado: ${document.id}")
                }
            } catch (e: Exception) {
                Log.e("AlertasViewModel", "Error al limpiar alertas: ${e.message}")
            }
        }
    }

    /**
     * Devuelve la hora formateada a partir de los milisegundos.
     */
    fun obtenerHoraFormateada(milis: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(milis))
    }
}
