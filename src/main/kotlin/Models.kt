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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.secondsun.geometry.*
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
    val backGroundId = resources.setImage(ImageIO.read(ShapeModel::class.java.getClassLoader().getResourceAsStream("bg12.png")));
    val backGround2Id = resources.setImage(ImageIO.read(ShapeModel::class.java.getClassLoader().getResourceAsStream("bg34.png")));
    val bg1 = resources.setTexture(backGroundId,  Vertex2D(128f,128f),128,128);
    val bg2 = resources.setTexture(backGroundId,  Vertex2D(0f,0f),-128,-128);

    val bg3 = resources.setTexture(backGround2Id,  Vertex2D(0f,0f),32,92);
    val bg4 = resources.setTexture(backGround2Id,  Vertex2D(0f,0f),-32,-92);

    val column2builder = (
            with(CubeBuilder(Vertex(-32f,0f,-48f ), 16f, 64f, 16f)) {
                top1 = Color.Black.toArgb()
                top2 = Color.Black.toArgb()

                bottom1 = bg1
                bottom2 = bg2

                right1 = bg3
                right2 = bg4

                left1 = bg3
                left2 = bg4

                near1 = bg3
                near2 = bg4

                far1 = bg3
                far2 = bg4
                this
            }).disableBottom()

    val column1Builder  = (
            with(CubeBuilder(Vertex(16f,0f,24f ), 16f, 64f, 16f)) {
                top1 = Color.Black.toArgb()
                top2 = Color.Black.toArgb()

                bottom1 = bg1
                bottom2 = bg2

                right1 = bg3
                right2 = bg4

                left1 = bg3
                left2 = bg4

                near1 = bg3
                near2 = bg4

                far1 = bg3
                far2 = bg4
                this
            }).disableBottom()

    val floorBuilder = (
        with(CubeBuilder(Vertex(-64f,-64f,-64f ), 128f, 64f, 128f)) {
            top1 = bg1
            top2 = bg2

            bottom1 = bg1
            bottom2 = bg2

            right1 = bg1
            right2 = bg2

            left1 = bg1
            left2 = bg2

            near1 = bg1
            near2 = bg2

            far1 = bg1
            far2 = bg2
            this
        })

    val shape: Model
        get() {
            return Model.of((ArrayList<Triangle?>().apply {
                addAll(column1Builder.cube().triangles)
                addAll(column2builder.cube().triangles)
                addAll(floorBuilder.cube().triangles)
            }))
        }
}

class CameraModel {
    fun toCamera(): Camera? {
        return Camera(eye.value.toVertex(), lookAt.value.toVertex(), up.value.toVertex())
    }

    val _eye = MutableStateFlow(Triple("0.02", "90.0", "0.01"))
    val eye  : StateFlow<Triple<String,String,String>> = _eye.asStateFlow()


    val _lookAt = MutableStateFlow(Triple("0.0", "0.0", "0.0"))
    val lookAt : StateFlow<Triple<String,String,String>> = _lookAt.asStateFlow()

    val _up = MutableStateFlow(Triple("-0.89", "0.0", "-0.45"))
    val up : StateFlow<Triple<String,String,String>> = _up.asStateFlow()

    fun setEye(eyeIn : Triple<String,String,String>) {
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
        _up.update { up.toTriple()}
        return _up
    }

    //TODO rename it doesn't do what I think it does yet
    fun panLookAt(panAmount: Vertex) {
        val lookAtVertex = _lookAt.value.toVertex()
        val newLookat = lookAtVertex.translate(panAmount)
        _lookAt.update { newLookat.toTriple() }
    }
    
    //TODO rename it doesn't do what I think it does yet
    fun panEye(panAmount: Vertex) {
        val eyeVertex = _eye.value.toVertex()
        val newEye = eyeVertex.translate(panAmount)
        _eye.update { newEye.toTriple() }
    }


}

private fun Vertex.translate(panAmount: Vertex): Vertex {
    this.x += panAmount.x
    this.y += panAmount.y
    this.z += panAmount.z
    return this
}

fun <A, B, C> Triple<A, B, C>.toVertex(): Vertex {
    try {
        return Vertex(
            first.toString().toFloat(),
            second.toString().toFloat(),
            third.toString().toFloat(),
        )
    } catch (ignore: Exception) {
        return Vertex.ZERO
    }
}
fun Vertex.subtract(lookatV: Vertex): Vertex {
    return Vertex(this.x - lookatV.x,
                this.y - lookatV.y,
                this.z - lookatV.z)
}

fun Vertex.toTriple(): Triple<String, String, String> {
    return Triple(String.format("%.2f", x),String.format("%.2f", y),String.format("%.2f", z))
}

fun Vertex.normalize(): Vertex {
    return Vertex(this.x/length(),this.y/length(), this.z/length())
}
