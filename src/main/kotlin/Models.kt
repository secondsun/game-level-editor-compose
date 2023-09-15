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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.secondsun.geometry.Camera
import dev.secondsun.geometry.Model
import dev.secondsun.geometry.Vertex
import dev.secondsun.geometry.Vertex2D
import dev.secondsun.util.CubeBuilder
import dev.secondsun.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Exception
import javax.imageio.ImageIO
import kotlin.Triple

class ShapeModel(val resources: Resources = Resources()) {
    val backGroundId = resources.setImage(ImageIO.read(ShapeModel::class.java.getClassLoader().getResourceAsStream("background.png")));
    val bg1 = resources.setTexture(backGroundId,  Vertex2D(0f,0f),255,255);
    val bg2 = resources.setTexture(backGroundId,  Vertex2D(255f,0f),-255,255);

    val cube = (
        with(CubeBuilder(Vertex(-64f,-4f,-64f ), 128f, 8f, 128f)) {
            top1 = bg2
            top2 = bg1

            bottom1 = Color.Green.toArgb()
            bottom2 = Color.Green.toArgb()

            right1 = Color.Blue.toArgb()
            right2 = Color.Blue.toArgb()

            left1 = Color.White.toArgb()
            left2 = Color.White.toArgb()

            near1 = Color.Yellow.toArgb()
            near2 = Color.Yellow.toArgb()

            far1 = Color.Cyan.toArgb()
            far2 = Color.Cyan.toArgb()
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

    val _eye = MutableStateFlow(Triple("0.0", "0.0", "-130.0"))
    val eye  : StateFlow<Triple<String,String,String>> = _eye.asStateFlow()


    val _lookAt = MutableStateFlow(Triple("0.0", "0.0", "0.0"))
    val lookAt : StateFlow<Triple<String,String,String>> = _lookAt.asStateFlow()

    val _up = MutableStateFlow(Triple("0.0", "0.0", "-130.0"))
    val up : StateFlow<Triple<String,String,String>> = _up.asStateFlow()

    fun setEye(eyeIn : Triple<String,String,String>) {
        System.out.println(eyeIn)
        _eye.update {  eyeIn };
        calcUp(_up)
    }

    fun setLookAt(lookAtIn : Triple<String,String,String>) {
        _lookAt.update {lookAtIn}
        calcUp(_up)
    }

    private fun calcUp(_up: MutableStateFlow<Triple<String, String, String>>): StateFlow<Triple<String, String, String>> {
        val eyeV = eye.value.toVertex()
        val lookatV = lookAt.value.toVertex()
        val forward = eyeV.subtract(lookatV).normalize()
        val yAxis = Vertex(0f,1f,0f);
        val right = yAxis.cross(forward).normalize()
        val up = forward.cross(right).normalize()
        System.out.println("Up " + up)
        _up.update { up.toTriple()}
        return _up
    }



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

private fun Vertex.subtract(lookatV: Vertex): Vertex {
    return Vertex(this.x - lookatV.x,
                this.y - lookatV.y,
                this.z - lookatV.z)
}

private fun Vertex.toTriple(): Triple<String, String, String> {
    return Triple(String.format("%.2f", x),String.format("%.2f", y),String.format("%.2f", z))
}

private fun Vertex.normalize(): Vertex {
    return Vertex(this.x/length(),this.y/length(), this.z/length())
}
