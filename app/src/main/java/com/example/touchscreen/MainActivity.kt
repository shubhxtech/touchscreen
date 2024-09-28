package com.example.touchscreen

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val path = remember { mutableStateOf< Path?>(Path()) }

            var currentColor by remember { mutableStateOf(Color.Red) }
            var strokeWidth by remember { mutableStateOf(5f) }

//            Column(modifier = Modifier.fillMaxSize()) {
                DrawingCanvas(path, currentColor, strokeWidth)
//                Spacer(modifier = Modifier.height(16.dp))

                // Buttons for changing color

            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()
                .padding(8.dp)) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    ModernButton(onClick = { currentColor = Color.Red }, text = "Red")
                    ModernButton(onClick = { currentColor = Color.Blue }, text = "Blue")
                    ModernButton(onClick = { currentColor = Color.Green }, text = "Green")
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Buttons for changing stroke width
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ModernButton(onClick = { strokeWidth = 5f }, text = "Thin")

                    ModernButton(onClick = { strokeWidth = 10f }, text = "Medium")
                    ModernButton(onClick = { strokeWidth = 15f }, text = "Thick")

                }
            }
            }
        }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(path: MutableState<Path?>, currentColor: Color, strokeWidth: Float) {
    Surface(color = Color.White,) {
        val currentPosition = remember { mutableStateOf(Offset.Zero) }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.value?.moveTo(motionEvent.x, motionEvent.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        path.value?.lineTo(motionEvent.x, motionEvent.y)
                        currentPosition.value = Offset(motionEvent.x, motionEvent.y)
                    }

                    MotionEvent.ACTION_UP -> {
                        path.value?.lineTo(motionEvent.x, motionEvent.y)
                        currentPosition.value = Offset.Zero
                    }
                }
                val tempPath = path.value
                path.value = null
                path.value = tempPath
                true
            },
            onDraw = {
                path.value?.let {
                    drawPath(
                        path = it,
                        color = currentColor,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        )
    }
}

@Composable
fun ModernButton(onClick: () -> Unit, text: String) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray, // Purple background
            contentColor = Color.Black          // Text color
        ),
        shape = RoundedCornerShape(16.dp),     // Rounded corners
        elevation = ButtonDefaults.buttonElevation(4.dp), // Shadow effect
        modifier = Modifier.padding(vertical = 4.dp)
            .width(100.dp)
            .height(40.dp)
    ) {
        Text(text = text)
    }
}