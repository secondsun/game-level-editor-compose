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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.Triple

@Composable
fun CameraDialog(cameraModel: CameraModel) {

    val eye =  cameraModel.eye.collectAsState()
    val lookAt =  cameraModel.lookAt.collectAsState()
    val up =  cameraModel.up.collectAsState()
    Row {
        Column() {
            TripleFieldRow("Camera Position", eye.value) {cameraModel.setEye(it)}
            TripleFieldRow("Camera Up", up.value){}
            TripleFieldRow("Camera Look At", lookAt.value){cameraModel.setLookAt(it)}
        }
    }
}

@Composable
fun TripleFieldRow(label: String, triple: Triple<String, String, String>, onValueChange: (Triple<String,String,String>)->Unit) {

    Row {
        Text(textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically).widthIn(min = 64.dp).fillMaxWidth(.1f), text = label)
        Spacer(Modifier.padding(8.dp))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth().wrapContentWidth(align = Alignment.End)) {
            TextField(
                modifier = Modifier.background(Color.White).weight(1f),
                label = { Text("X") },
                value = (triple.first),
                onValueChange = {onValueChange(Triple(it, triple.second,triple.third))}
            )
            TextField(
                modifier = Modifier.background(Color.White).weight(1f),
                label = { Text("Y") },
                value = (triple.second),
                onValueChange = {onValueChange(Triple(triple.first, it,triple.third))}
            )
            TextField(
                modifier = Modifier.background(Color.White).weight(1f),
                label = { Text("Z") },
                value = (triple.third),
                onValueChange = {onValueChange(Triple(triple.first, triple.second, it))}
            )
        }
    }
}
