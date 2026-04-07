package com.example.parotrash.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.parotrash.R

@Composable
fun ChulitoOk(modifier: Modifier = Modifier){
    Image(painter = painterResource(id = R.drawable.chulito_ok),
        contentDescription = "Chulito de OK",
        modifier = modifier,

        )
}