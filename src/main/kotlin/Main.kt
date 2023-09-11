/**
Secondsun's Game Level Editor
Copyright (C) 2023 Summers Pittman
secondsun@gmail.com

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software Foundation,
Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
**/
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.secondsun.game.ScanLineEngine
import dev.secondsun.geometry.Model
import dev.secondsun.geometry.Vertex2D
import dev.secondsun.util.Resources
import java.awt.image.BufferedImage

@Composable
@Preview
fun App() {
    var cameraModel = remember { CameraModel() }

    MaterialTheme {
        Scaffold(topBar = {
            TopAppBar(title = { Text("Shape Editor") })
        }) {
            Column(Modifier.fillMaxSize()) {
                CameraDialog(cameraModel)
                Row(Modifier.fillMaxSize()) {
                    Canvas(Modifier.fillMaxSize()) {

                        val resources = Resources()
                        val shapModel =  ShapeModel(resources)
                        drawRect(Color.LightGray)

                        val model: Model = shapModel.shape
                        val engine = ScanLineEngine(256, 192, model, resources)
                        val camera = cameraModel.toCamera()
                        model.lookAt(camera, Vertex2D(2f, 2f), Vertex2D(127f, 96f))
                        val tiles = model.triangles

                        val image = engine.draw(tiles)
                        val iOut = BufferedImage(256, 192, BufferedImage.TYPE_INT_ARGB)
                        for (x: Int in 0..255) {
                            for (y: Int in 0..191) {
                                iOut.setRGB(x, y, image[x + y * 256])
                            }
                        }

                        drawImage(iOut.toComposeImageBitmap(), filterQuality = FilterQuality.Low, dstSize = IntSize(1024.dp.roundToPx(), 768.dp.roundToPx()))
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

data class Triple(val x: String, val y: String, val z: String)
