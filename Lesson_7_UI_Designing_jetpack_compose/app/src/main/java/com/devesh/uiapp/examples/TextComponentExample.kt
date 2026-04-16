package com.devesh.uiapp.examples

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun TextComponentExample(){

    Text(text = "Hello World",
        fontSize = 15.sp)

}