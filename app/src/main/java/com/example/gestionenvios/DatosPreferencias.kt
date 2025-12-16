package com.example.gestionenvios

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// accesible desde cualquier parte de la app para guardar datos
val Context.dataStore by preferencesDataStore(name = "datos")

// clave con la que se guarda y se lee el contador
val CONTADOR_CORREOS = intPreferencesKey("contadorCorreos")

