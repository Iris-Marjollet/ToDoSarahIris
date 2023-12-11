package com.sarahiris.todo.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarahiris.todo.detail.ui.theme.ToDoSarahIrisTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoSarahIrisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Detail("Android")
                }
            }
        }
    }
}

/*@Composable
fun Detail(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Task Detail",
        modifier = modifier
    )
}*/

@Composable
fun Detail(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)


    ){
        Text(
            text = "Task Detail",
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )

        Text(
            text = "title",
        )

        Text(
            text = "description"
        )

        Button(
            onClick = {
                // Action à effectuer lors du clic sur le bouton
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Valider")
        }
    }


}


@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    ToDoSarahIrisTheme {
        Detail("Android")
    }
}