package com.example.parotrash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotonPantalla()
        }
    }
}

@Composable
fun Boton(icon1:ImageVector?,nombre: String, abajo: String?,icon2:Boolean, modifier: Modifier = Modifier){

    var usuario by remember { mutableStateOf("") }
    var ojo by remember {mutableStateOf(false)}

    //icono 2 ojo si cambia
    val icono = if  (ojo){
        Icons.Default.Visibility
    }else{
        Icons.Default.VisibilityOff
    }

    OutlinedTextField(
        //Estado del usuario
        value= usuario,
        onValueChange = { usuario = it },
        //Todo lo que tiene que ver con la estetica
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        shape = RoundedCornerShape(40.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFF9342B),
            focusedBorderColor = Color(0xFFF9342B)

        ),
        //funcionalidad
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine= true,
        visualTransformation =
            if (icon2 && !ojo){
            PasswordVisualTransformation()
                }else{
                    VisualTransformation.None
            },


        //Orden cosas
        //Icono izquierda
        leadingIcon = {
            if (icon1 != null) {
                Icon(
                    imageVector = icon1,
                    contentDescription = null,
                    tint = Color(0xFFF9342B),
                    modifier = Modifier.size(24.dp)
                )
            }

        },
        //texto
        label = {Text (nombre)},
        //icono derecha

        trailingIcon ={
            if (icon2) {

                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Color(0xFFF9342B),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable{ojo = !ojo}
                )

            }

        }


    )
    //texto abajo

    Column(    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "$abajo"

        )
    }



}
@Composable
fun BotonCargando(nombre: String ){
    var cargando by remember { mutableStateOf(false) }

    Button(
        onClick = { cargando = !cargando },
        enabled = !cargando,
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF9342B)
        ),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth())
    { if (cargando)
    {
        CircularProgressIndicator()
    } else {
        Text(nombre)
    }
    }
}


@Preview
@Composable
fun BotonPantalla(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Boton( icon1 = Icons.Default.Email,nombre="Correo",abajo="Olvido su contrase;a", icon2= true)

    }
}
