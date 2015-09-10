package net.chicarrida.ants2scala.util

import net.chicarrida.ants2scala.util.State.State
import processing.core.{PApplet, PConstants, PVector}

class Rectangle(val p: PApplet, val position: PVector, val angle: Float, val state: State) {

  val width: Int = 3
  val height: Int = 10
  val c: Int = p.color(0,220,0)
  var strength: Int = 50
  if(state == State.GOING_HOME)
    strength = 255

  def getStrength = strength

  def draw = {
    p.noStroke()
    p.fill(p.red(c), p.green(c), p.blue(c), strength)
    p.pushMatrix()
    p.translate(position.x, position.y)
    p.rotate(angle)
    p.rectMode(PConstants.CENTER)
    p.rect(0,0,width,height)
   // strength -= 1
    p.popMatrix()
  }
}
