package com.shakenbeer.composecocktail.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.ui.theme.ComposeCocktailTheme

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Trouble(painter: Painter, message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painter, contentDescription = null,
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colors.secondary
        )
        Text(text = message)
        Spacer(modifier = Modifier.size(0.dp, 16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.retry).uppercase())
        }
    }
}

@Preview
@Composable
fun TroublePreview() {
    ComposeCocktailTheme {
        Trouble(rememberVectorPainter(image = Icons.Filled.ThumbUp), "No categories found")
    }
}