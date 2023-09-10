import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

val image = BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB)
val graphics = image.createGraphics()

graphics.color = Color.RED
graphics.fillRect(0,0,255,255)
for ( x in 0..255 step 16) {
    for (y in 0..255 step 16) {
        drawHorizontalBar(x,y, graphics);
        drawVerticalBar(x,y, graphics);
    }
}

fun drawHorizontalBar(x: Int, y: Int, graphics: Graphics2D) {
    val yMin = maxOf(y-1,0)
    val yMax = minOf(y,255)
    val xMin = maxOf(x-4,0)
    val xMax = minOf(x+3,255)
    graphics.color = Color.LIGHT_GRAY
    graphics.fillRect(xMin,yMin, xMax-xMin, yMax-yMin)
}

fun drawVerticalBar(x: Int, y: Int, graphics: Graphics2D) {
    val yMin = maxOf(y-4,0)
    val yMax = minOf(y+3,255)
    val xMin = maxOf(x-1,0)
    val xMax = minOf(x,255)
    graphics.color = Color.LIGHT_GRAY
    graphics.fillRect(xMin,yMin, xMax-xMin, yMax-yMin)
}

ImageIO.write(image, "PNG", File("background.png"))