package net.chicarrida.ants2scala

import net.chicarrida.ants2scala.util.Rectangle
import processing.core.PApplet

import scala.collection.mutable.ListBuffer

class Path(p: PApplet){

  var rects: ListBuffer[Rectangle] = new ListBuffer[Rectangle]()

  def draw = {
    for(i <- 0 until rects.size) {

      if(i < rects.size && rects(i) != null ) {
        rects(i).draw
        if (rects(i).getStrength <= 0) {
          rects.remove(i)
        }
      }
    }
  }
}
