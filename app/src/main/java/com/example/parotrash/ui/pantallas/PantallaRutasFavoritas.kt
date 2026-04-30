package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parotrash.modelos.LugarBusqueda
import com.example.parotrash.modelos.RutaFavorita
import com.example.parotrash.ui.componentes.BuscadorEstaciones
import com.example.parotrash.ui.componentes.DialogoConfirmarEstacion
import com.example.parotrash.ui.componentes.SelectorUbicacion
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.RutasFavoritasViewModel
import com.example.parotrash.ui.viewmodel.RutasFavoritasViewModelFactory

@Composable
fun PantallaRutasFavoritas(
    irAHome: () -> Unit,
    irATransmilenio: () -> Unit,
    irASITP: () -> Unit
) {
    val viewModel: RutasFavoritasViewModel = viewModel(
        factory = RutasFavoritasViewModelFactory(android.app.Application())
    )
    val rutas by viewModel.rutas.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var rutaAEditar by remember { mutableStateOf<RutaFavorita?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = irAHome) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Text(
                    text = "Mis Rutas Favoritas",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (rutas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes rutas guardadas",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "Toca + para agregar tu primera ruta",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(rutas.take(5)) { ruta ->
                        TarjetaRutaFavorita(
                            ruta = ruta,
                            onEditar = { rutaAEditar = ruta },
                            onEliminar = { viewModel.eliminarRuta(ruta.id) {} }
                        )
                    }

                    if (rutas.size > 5) {
                        item {
                            Text(
                                text = "+ ${rutas.size - 5} rutas más",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { mostrarDialogoAgregar = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar ruta"
            )
        }

        if (mostrarDialogoAgregar) {
            DialogoAgregarRuta(
                onDismiss = { mostrarDialogoAgregar = false },
                onGuardar = { nombre, desde, hasta ->
                    viewModel.guardarRuta(
                        nombre = nombre,
                        desdeNombre = desde.nombre,
                        desdeLat = desde.latitud,
                        desdeLng = desde.longitud,
                        hastaNombre = hasta.nombre,
                        hastaLat = hasta.latitud,
                        hastaLng = hasta.longitud
                    ) {
                        mostrarDialogoAgregar = false
                    }
                },
                busquedaViewModel = viewModel
            )
        }

        rutaAEditar?.let { ruta ->
            DialogoEditarRuta(
                ruta = ruta,
                onDismiss = { rutaAEditar = null },
                onGuardar = { rutaEditada ->
                    viewModel.actualizarRuta(rutaEditada) {
                        rutaAEditar = null
                    }
                },
                onEliminar = {
                    viewModel.eliminarRuta(ruta.id) {
                        rutaAEditar = null
                    }
                }
            )
        }
    }
}

@Composable
private fun TarjetaRutaFavorita(
    ruta: RutaFavorita,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditar() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ParoTrashTheme.customColors.mapElementBackground
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ruta.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Row {
                    IconButton(onClick = onEditar, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onEliminar, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = ruta.desdeNombre,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = ruta.hastaNombre,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun DialogoAgregarRuta(
    onDismiss: () -> Unit,
    onGuardar: (String, LugarBusqueda, LugarBusqueda) -> Unit,
    busquedaViewModel: RutasFavoritasViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var desde by remember { mutableStateOf<LugarBusqueda?>(null) }
    var hasta by remember { mutableStateOf<LugarBusqueda?>(null) }
    var busquedaDesde by remember { mutableStateOf("") }
    var busquedaHasta by remember { mutableStateOf("") }
    var resultadosDesde by remember { mutableStateOf<List<LugarBusqueda>>(emptyList()) }
    var resultadosHasta by remember { mutableStateOf<List<LugarBusqueda>>(emptyList()) }
    var mostrarDesde by remember { mutableStateOf(false) }
    var mostrarHasta by remember { mutableStateOf(false) }

    val resultados by busquedaViewModel.resultadoBusqueda.collectAsStateWithLifecycle()

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = ParoTrashTheme.customColors.mapElementBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Nueva Ruta Favorita",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                androidx.compose.material3.OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre (ej: Casa - Trabajo)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                androidx.compose.material3.OutlinedTextField(
                    value = busquedaDesde,
                    onValueChange = { 
                        busquedaDesde = it
                        busquedaViewModel.buscarLugares(it)
                        mostrarDesde = true
                    },
                    label = { Text("Desde") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (mostrarDesde && resultados.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.height(120.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(resultados.take(5)) { lugar ->
                            androidx.compose.material3.Text(
                                text = "${lugar.nombre} (${lugar.tipo})",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        desde = lugar
                                        busquedaDesde = lugar.nombre
                                        mostrarDesde = false
                                    }
                                    .padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                androidx.compose.material3.OutlinedTextField(
                    value = busquedaHasta,
                    onValueChange = { 
                        busquedaHasta = it
                        busquedaViewModel.buscarLugares(it)
                        mostrarHasta = true
                    },
                    label = { Text("Hasta") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (mostrarHasta && resultados.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.height(120.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(resultados.take(5)) { lugar ->
                            androidx.compose.material3.Text(
                                text = "${lugar.nombre} (${lugar.tipo})",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        hasta = lugar
                                        busquedaHasta = lugar.nombre
                                        mostrarHasta = false
                                    }
                                    .padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    androidx.compose.material3.Button(
                        onClick = {
                            if (desde != null && hasta != null && nombre.isNotBlank()) {
                                onGuardar(nombre, desde!!, hasta!!)
                            }
                        },
                        enabled = desde != null && hasta != null && nombre.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogoEditarRuta(
    ruta: RutaFavorita,
    onDismiss: () -> Unit,
    onGuardar: (RutaFavorita) -> Unit,
    onEliminar: () -> Unit
) {
    var nombre by remember { mutableStateOf(ruta.nombre) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = ParoTrashTheme.customColors.mapElementBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Editar Ruta",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                androidx.compose.material3.OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "Desde: ${ruta.desdeNombre}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Hasta: ${ruta.hastaNombre}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = { mostrarConfirmacion = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Eliminar")
                    }

                    androidx.compose.material3.Button(
                        onClick = {
                            onGuardar(ruta.copy(nombre = nombre))
                        },
                        enabled = nombre.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }

        if (mostrarConfirmacion) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { mostrarConfirmacion = false }) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("¿Eliminar esta ruta?")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            androidx.compose.material3.OutlinedButton(
                                onClick = { mostrarConfirmacion = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                            androidx.compose.material3.Button(
                                onClick = {
                                    mostrarConfirmacion = false
                                    onEliminar()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}