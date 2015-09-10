package net.chicarrida.ants2scala


import processing.core.{PImage, PApplet, PVector}

import scala.collection.mutable.ArrayBuffer

class Main extends PApplet {

  var ants: ArrayBuffer[Ant] = new ArrayBuffer[Ant]()
  var paths: Path = new Path(this)
  var target: Gradient = null
  var image: PImage = null
  var transparency: Float = 255

  override def setup() = {
    size(600, 600)
    //frameRate(12)
    setUpAnts
  }

  override def draw()= {
    background(0)
    transparency -= 0.001f
    tint(255, transparency)

    if (image != null) {
      image(image, 0, 0)
    }

    if (target != null) {
      target.draw()
      target = null
    }
    paths.draw
    paths.rects.clear()
    image = get()
    if (transparency < 15) transparency = 255

    //fade pic
    //draw pic
    //draw gradient
    //draw rect
    //store pic
    //process and draw Ants


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
  target = new Gradient(this, new PVector(mouseX, mouseY))
  }
}