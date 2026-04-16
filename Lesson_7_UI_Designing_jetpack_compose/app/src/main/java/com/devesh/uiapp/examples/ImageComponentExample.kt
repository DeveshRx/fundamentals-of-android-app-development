package com.devesh.uiapp.examples

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devesh.uiapp.R

@Preview(showBackground = true)
@Composable
fun ImageComponentExample(){
    Image(
        painter = painterResource(
            R.drawable.ic_android_24dp
        ),
        contentDescription = "My Android Photo",
        modifier = Modifier.size(60.dp)
    )
}