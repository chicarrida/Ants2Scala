package net.chicarrida.ants2scala

import processing.core.{PConstants, PVector, PApplet}

class Gradient(val p: PApplet, val pos: PVector, val radius:Float = 100) {

  var c: Int = 0

  def draw()={
    p.ellipseMode(PConstants.RADIUS)
    var h: Double = 0
    p.noStroke()
    for(r <- (0 to radius.toInt).reverse){
      val tmpR = PApplet.map(r,0,radius,0,6)
      h= 2/(tmpR*tmpR+0.5)
      h = PApplet.map(h.toFloat,0,3,0,255)
      if(h > 20){
        c= p.color(h.toInt,0,0)
        p.fill(c)
        p.ellipse(pos.x, pos.y, r, r)

      }
    }
  }
}
