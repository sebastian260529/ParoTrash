package com.example.parotrash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
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
fun Boton(icon1:ImageVector?,nombre: String, abajo: String?,icon2:ImageVector?, modifier: Modifier = Modifier){
    Surface(
        modifier = Modifier
            .padding(5.dp),
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(1.dp, Color(0xFFF9342B)),
        color = Color.White){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (icon1 != null) {
                    Icon(
                        imageVector = icon1,
                        contentDescription = null,
                        tint = Color(0xFFF9342B),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = nombre

                )

            }

            if (icon2 != null) {
                Icon(
                    imageVector = icon2,
                    contentDescription = null,
                    tint = Color(0xFFF9342B),
                    modifier = Modifier.size(24.dp),

                    )
            }
        }
    }

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
fun BotonLoading(nombre: String ){
    Surface(
        modifier = Modifier
            .padding(5.dp),
        shape = RoundedCornerShape(100.dp),
        color = Color(0xFFF9342B)


    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text=nombre,
                color = Color.White
            )
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
        Boton( icon1 = Icons.Default.Email,nombre="Correo",abajo="Olvido su contrase;a", icon2= Icons.Default.Call)
        BotonLoading(nombre= "A")
    }
}
