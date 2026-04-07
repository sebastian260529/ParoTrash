package com.example.parotrash.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Formulario(icon1:ImageVector?,
               nombre: String,
               abajo: String?,
               icon2:Boolean,
               modifier: Modifier = Modifier,
               usuario :String,
               onTextChange:(String) -> Unit ){

    var ojo by remember {mutableStateOf(false)}

    //icono 2 ojo si cambia
    val icono = if  (ojo){
        Icons.Default.Visibility
    }else{
        Icons.Default.VisibilityOff
    }

    Column (modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween) {

        OutlinedTextField(
            value = usuario,
            onValueChange = { onTextChange(it) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(40.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFF9342B),
                focusedBorderColor = Color(0xFFF9342B)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            visualTransformation =
                if (icon2 && !ojo) PasswordVisualTransformation()
                else VisualTransformation.None,

            leadingIcon = {
                if (icon1 != null) {
                    Icon(
                        imageVector = icon1,
                        contentDescription = null,
                        tint = Color(0xFFF9342B)
                    )
                }
            },

            label = { Text(nombre) },

            trailingIcon = {
                if (icon2) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = Color(0xFFF9342B),
                        modifier = Modifier.clickable { ojo = !ojo }
                    )
                }
            }
        )


        Text( text = abajo ?: "", )

    }

}