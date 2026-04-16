package com.devesh.uiapp.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun ColumnLayoutsComponentsExample(){

    Column() {
        Text("Item 1")
        Text("Item 2")
        Text("Item 3")

    }
}

@Preview(showBackground = true)
@Composable
fun RowLayoutsComponentsExample(){

    Row{
        Text("Item 1", Modifier.padding(2.dp))
        Text("Item 2", Modifier.padding(2.dp))
        Text("Item 3", Modifier.padding(2.dp))

    }
}


@Preview(showBackground = true)
@Composable
fun BoxLayoutsComponentsExample(){

    Box{
        Text("Item 1", Modifier.padding(0.dp))
        Text("Hello", Modifier.padding(8.dp))
        Text("World", Modifier.padding(13.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun LazyColumnLayoutsComponentsExample(){

    val fruitList= listOf("Apple", "Banana", "Orange",
        "Mango", "Grapes")

    LazyColumn() {
        items(fruitList.size) {
            Text(text = fruitList[it])
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LazyRowLayoutsComponentsExample(){

    val fruitList= listOf("Apple", "Banana", "Orange",
        "Mango", "Grapes")

    LazyRow() {
        items(fruitList.size) {
            Text(text = fruitList[it],
                Modifier.padding(2.dp))
        }
    }


}




