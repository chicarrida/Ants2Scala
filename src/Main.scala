import processing.core.{PImage, PVector, PApplet}



class Main extends PApplet {

var a: Ant = null
override def setup() = {
  size(400, 400)
  a = new Ant(this, new PVector(width/2, height/2))
}

override def draw()= {
  background(125)
  a.update
  a.render
  a.boundaries

}
}
