package net.chicarrida.ants2scala

import processing.core.{PApplet, PVector}

import scala.collection.mutable.ArrayBuffer

class Main extends PApplet {

  var ants: ArrayBuffer[Ant] = new ArrayBuffer[Ant]()
  var paths: Path = new Path(this)
  var targets: ArrayBuffer[Gradient] = new ArrayBuffer[Gradient]()

  override def setup() = {
    size(600, 600)
   // frameRate(24)
    setUpAnts
  }

  override def draw()= {
    background(0)
    for(t <- targets){
      t.draw()
    }
    paths.draw
    for (a <- ants) {
      paths.rects += a.update
      a.render
      a.boundaries
    }

  }

  def setUpAnts = {
    ants.clear()
    for(i <- 0 until 10)
      ants += new Ant(this,Math.random().toFloat*6,  new PVector(width/2, height/2))
  }

  override def keyPressed = {
      if (keyCode == 'r' || keyCode == 'R')
        setUpAnts
  }

  override  def mousePressed:Unit = {
  targets += new Gradient(this, new PVector(mouseX, mouseY))
  }
}