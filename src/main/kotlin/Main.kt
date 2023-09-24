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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.minus
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.secondsun.game.ScanLineEngine
import dev.secondsun.geometry.Model
import dev.secondsun.geometry.Triangle
import dev.secondsun.geometry.Vertex
import dev.secondsun.geometry.Vertex2D
import dev.secondsun.util.Maths
import dev.secondsun.util.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.awt.image.BufferedImage
import kotlin.math.PI


@Composable
@Preview
fun App() {
    var cameraModel = CameraModel()
    val resources = Resources()
    var triangles :List<Triangle> = (listOf())
    var xzRot:Int = 0;
    var yzRot:Int = 0;
    val shapeModel =  ShapeModel(resources)
    var model: Model = shapeModel.shape
    val engine = ScanLineEngine(64, 64, model, resources)

    fun updateEye() {
        var vertex = cameraModel.eye.value.toVertex()
        var x = Math.sin(Math.toRadians(xzRot.toDouble()))*128
        var z = Math.cos(Math.toRadians(xzRot.toDouble()))*128
        vertex.x = x.toFloat()
        vertex.z = z.toFloat()
        cameraModel.setEye(vertex.toTriple())
    }

     Dispatchers.Default.dispatch(Dispatchers.Default) {
         runBlocking {
             cameraModel.eye.collect({ it ->  val camera = cameraModel.toCamera();model = shapeModel.shape;model.lookAt(camera, Vertex2D(.5f, .5f), Vertex2D(64f, 64f))})
             cameraModel.up.collect({ it ->   val camera = cameraModel.toCamera();model = shapeModel.shape;model.lookAt(camera, Vertex2D(.5f, .5f), Vertex2D(64f, 64f))})
             cameraModel.lookAt.collect({ it ->   val camera = cameraModel.toCamera();model = shapeModel.shape;model.lookAt(camera, Vertex2D(.5f, .5f), Vertex2D(64f, 64f))})
         }

     }
    MaterialTheme {
        Scaffold(
            topBar = {
            TopAppBar(title = { Text("Shape Editor") })
        }) {
            Column(Modifier.fillMaxSize()) {
                CameraDialog(cameraModel)
                Row(Modifier.fillMaxSize()) {
                    val isShiftDown = LocalKeyboard.current.isShiftDown
                    Canvas(Modifier.fillMaxSize()
                        .pointerInput(LocalKeyboard.current) {
                        detectDragGestures { change, dragAmount ->
                            System.out.println(isShiftDown)
                            if (isShiftDown) {

                                //val pan = Vertex(dragAmount.x,dragAmount.y,0f)//.translateX(previousPan.x).translateY(previousPan.y);
                                //val panMatrix = pan.project(cameraModel.toCamera()!!.lookAt())
                                val panMatrixY = cameraModel.up.value.toVertex().scale(Maths.normalize(Vertex(dragAmount.x,dragAmount.y, 0f)).y)
                                val eyeLine = Maths.subtract(cameraModel.lookAt.value.toVertex(),cameraModel.eye.value.toVertex())
                                val panMatrixX = cameraModel.up.value.toVertex().rotateAround(eyeLine, Math.toRadians(90.0).toFloat()).scale(Maths.normalize(Vertex(dragAmount.x,dragAmount.y, 0f)).x * -1.0f)
                                //System.out.println(pan)
                                //System.out.println(panMatrix)
                                cameraModel.panEye(panMatrixY)
                                cameraModel.panLookAt(panMatrixY)
                                cameraModel.panEye(panMatrixX)
                                cameraModel.panLookAt(panMatrixX)
                            } else {

                                yzRot += dragAmount.y.toInt()
                                xzRot += dragAmount.x.toInt()
                                updateEye()}
                            }

                        detectTapGestures {
                            offset->

                                //remove padding from offset
                                var newOffset = offset.minus(IntOffset(16.dp.roundToPx(), 16.dp.roundToPx()))
                                //detect if offset in draw region
                                val imageRect = IntSize(64.dp.roundToPx(), 64.dp.roundToPx())
                                if ((newOffset.x < imageRect.width &&
                                    newOffset.y < imageRect.height) && (
                                            newOffset.x >= 0 &&
                                            newOffset.y >= 0
                                            )) {
                                    //scale offset to engine size

                                    var newOffset = IntOffset((newOffset.x/((64.dp.roundToPx()/512))).toInt(),
                                                              (newOffset.y/(64.dp.roundToPx()/384)).toInt())

                                    for(triangle in triangles) {
                                        if (isInsideTriangle(triangle.v1,triangle.v2,triangle.v3, newOffset)) {

                                        }
                                    }
                                    //look for triangle with x,y of offset
                                }


                            }
                    }) {
                        val padding = IntOffset(16.dp.roundToPx(), 16.dp.roundToPx())

                        val tiles = model.triangles
                        triangles = model.triangles

                        val image = engine.draw(tiles)
                        val iOut = BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)
                        for (x: Int in 0..63) {
                            for (y: Int in 0..63) {
                                iOut.setRGB(x, y, (0xFF000000 or image[x + y * 64].toLong()).toInt())
                            }
                        }

                        drawImage(iOut.toComposeImageBitmap(), filterQuality = FilterQuality.Low, dstOffset = padding, dstSize = IntSize(512.dp.roundToPx(), 384.dp.roundToPx()))
                    }
                }
            }
        }
    }
}



fun main() = application {
    var isShiftDown by remember { mutableStateOf(false) }

    Window(onCloseRequest = ::exitApplication, title = "Scene Editor", onPreviewKeyEvent = {isShiftDown = it.isShiftPressed; false}) {
        App2(isShiftDown)

    }
}

@Composable fun App2(isShiftDown: Boolean) {
    CompositionLocalProvider(LocalKeyboard provides  KeyboardState(isShiftDown)) {
        App()
    }
}

fun triangleToString(triangle: Triangle) : String {

    return "{${triangle.v1}}\n{${triangle.v2}}\n{${triangle.v3}}\nnormal:${triangle.normal()}\n" +
            "colox:${triangle.textureId}\n\n"
}

//From : https://www.geeksforgeeks.org/check-whether-a-given-point-lies-inside-a-triangle-or-not/
fun isInsideTriangle(A: Vertex, B: Vertex, C: Vertex, P: IntOffset) : Boolean{

    // Calculate the barycentric coordinates
    // of point P with respect to triangle ABC
    val denominator = (B.y - C.y) * (A.x - C.x) +
            (C.x - B.x) * (A.y - C.y)
    val a= ((B.y - C.y) * (P.x - C.x) +
            (C.x - B.x) * (P.y - C.y)) / denominator
    val b = ((C.y - A.y) * (P.x - C.x) +
            (A.x - C.x) * (P.y - C.y)) / denominator
    val c = 1.0f - a - b

    // Check if all barycentric coordinates
    // are non-negative
    return if (a >= 0 && b >= 0 && c >= 0) {
        return true
    } else {
        return false
    }
}


data class Triple(val x: String, val y: String, val z: String)
