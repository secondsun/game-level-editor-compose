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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.secondsun.geometry.Camera
import dev.secondsun.geometry.Model
import dev.secondsun.geometry.Vertex
import dev.secondsun.geometry.Vertex2D
import dev.secondsun.util.CubeBuilder
import dev.secondsun.util.Resources
import java.lang.Exception
import javax.imageio.ImageIO
import kotlin.Triple

class ShapeModel(val resources: Resources = Resources()) {
    val backGroundId = resources.setImage(ImageIO.read(ShapeModel::class.java.getClassLoader().getResourceAsStream("background.png")));
    val bg1 = resources.setTexture(backGroundId,  Vertex2D(32f,32f),192,192);
    val bg2 = resources.setTexture(backGroundId,  Vertex2D(192f,192f),-192,-192);

    val cube = (
        with(CubeBuilder(Vertex(-127f,-4f,-127f ), 128f, 8f, 128f)) {
            top1 = Color.Red.toArgb()
            top2 = Color.Red.toArgb()

            bottom1 = Color.Green.toArgb()
            bottom2 = Color.Green.toArgb()

            front1 = Color.Blue.toArgb()
            front2 = Color.Blue.toArgb()

            back1 = Color.White.toArgb()
            back2 = Color.White.toArgb()

            down1 = Color.Cyan.toArgb()
            down2 = Color.Cyan.toArgb()

            up1 = bg1
            up2 = bg2
            this
        }
        ).cube()

    val shape: Model
        get() {
            return cube
        }
}

class CameraModel {
    fun toCamera(): Camera? {
        return Camera(eye.value.toVertex(), lookAt.value.toVertex(), up.value.toVertex())
    }

    var eye = mutableStateOf(Triple("30.0", "10.0", "130.0"))
    var up = mutableStateOf(Triple("0.0", ".7", ".3"))
    var lookAt = mutableStateOf(Triple("0.0", "0.0", "0.0"))

    private fun <A, B, C> Triple<A, B, C>.toVertex(): Vertex {
        try {
            return Vertex(
                first.toString().toFloat(),
                second.toString().toFloat(),
                third.toString().toFloat(),
            )
        } catch (ignore: Exception) {
            System.out.println(ignore)
            return Vertex.ZERO
        }
    }
}
