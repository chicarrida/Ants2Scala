package net.chicarrida.ants2scala

import net.chicarrida.ants2scala.util.{Rectangle, State}
import net.chicarrida.ants2scala.util.State.State
import processing.core.{PApplet, PConstants, PVector}


import scala.collection.mutable.ArrayBuffer


class Ant(var p:PApplet, var angle: Float = 0.0f, var position: PVector = null, var debug: Boolean = false) {
  if(position == null )
    position = new PVector(p.width/2, p.height/2)

  var speed: Float = 1.0f
  var speedCounter: Int = 20
  var rotationCounter: Int = -25
  var lRed: Float = 0.0f
  var rRed: Float = 0.0f
  var lGreen: Float = 0.0f
  var rGreen: Float = 0.0f
  var ranAgainstTheWall: Boolean = false
  var rotateLeft: Boolean = true
  var rotationStep: Float = 0.08f

  val RED_THRESHOLD: Int = 150
  val DISTANCE_TO_WINDOW: Int = 20

  var velocity: PVector  = new PVector(0.0f, -0.5f)
  var dir: PVector =  new PVector(0.0f, 1.0f)
  var home: PVector = new PVector(p.width/2, p.height/2)
  var c: Int = p.color(125,125,125)
  var state: State = State.SEARCHING
  var globalSensorPositions : ArrayBuffer[PVector] = new ArrayBuffer[PVector]()
  var sensorPositions: ArrayBuffer[PVector] = new ArrayBuffer[PVector]()
  sensorPositions += new PVector(15, -7)
  sensorPositions += new PVector(15, 7)

def update: Rectangle = {
  if(globalSensorPositions.size > 0 && state == State.SEARCHING)
    search
  if (state == State.GOING_HOME)
    goHome

  velocity.add(dir)
  velocity.limit(speed)
  position.add(velocity)

  new Rectangle(p, new PVector(position.x,position.y), angle, state)
}


  def search = {
    lRed = p.red(p.get(globalSensorPositions(0).x.toInt, globalSensorPositions(0).y.toInt))
    rRed = p.red(p.get(globalSensorPositions(1).x.toInt, globalSensorPositions(1).y.toInt))
    rGreen = p.green(p.get(globalSensorPositions(0).x.toInt, globalSensorPositions(0).y.toInt))
    lGreen = p.green(p.get(globalSensorPositions(1).x.toInt, globalSensorPositions(1).y.toInt))

    if(rRed > RED_THRESHOLD || lRed > RED_THRESHOLD){
      c = p.color(0,255,0)
      state = State.GOING_HOME
    }else if(lRed > rRed){
      angle -= p.random(0.05f, 0.08f)
    }else if(rRed > lRed){
      angle += p.random(0.05f, 0.08f)
    }else if(lGreen > rGreen && lGreen > 50){
      angle -= 0.05f
    }else if(rGreen > lGreen && rGreen > 50){
      angle += 0.05f
    }else{
      angle += p.random(-0.05f, 0.05f)
    }
    setRotationAngle(angle)
  }
//FIXME mel this is not working correctly and also needs random rotation
  def goHome = {
    if (position.x < home.x +5 && position.y < home.y+5 && position.x > home.x -5 && position.y > home.y-5) {
        setRotationAngle(angle+PConstants.PI)
      state = State.SEARCHING
      c = p.color(127)
    }

    dir = home
    dir.sub(position)
    angle = PApplet.atan2(dir.y, dir.x)
    setRotationAngle(angle)
  }

  def correctPath( d: PVector) = {
    val a = PVector.angleBetween(dir, d)
    setRotationAngle(a)
  }

  def setRotationAngle(a: Float) = {
    angle = a
    dir.x = 1
    dir.y = 0
    rotate2D(angle)
  }

  def rotate2D(a: Float) = {
    val xTemp = dir.x
    dir.x = dir.x*math.cos(a).toFloat - dir.y*math.sin(a).toFloat
    dir.y = xTemp*math.sin(a).toFloat + dir.y*math.cos(a).toFloat
  }

  def calculateSensorPositionsToBaseCoordinateSystem ={
    globalSensorPositions.clear()
    for(i <- 0 until sensorPositions.length){
      val x = p.screenX(sensorPositions(i).x, sensorPositions(i).y)
      val y = p.screenY (sensorPositions(i).x, sensorPositions(i).y)
      globalSensorPositions += new PVector(x,y)
    }
  }

  def boundaries = {
    var newDirection: PVector = null
    if(!ranAgainstTheWall) {
      val offset: Int = 20
      if (position.x < DISTANCE_TO_WINDOW) {
        newDirection = new PVector(offset, velocity.y)
      }
      else if (position.x > p.width - DISTANCE_TO_WINDOW) {
        newDirection = new PVector(-offset, velocity.y)
      }
      if (position.y < DISTANCE_TO_WINDOW) {
        newDirection = new PVector(velocity.x, offset)
      }
      else if (position.y > p.height - DISTANCE_TO_WINDOW) {
        newDirection = new PVector(velocity.x, -offset)
      }
    }

    if(newDirection != null){
      ranAgainstTheWall = true
      rotationCounter = 36
       determineRotationDirectionAndStep(newDirection)
      speedCounter = 20
      speed = 0.3f
    }else{
      if(rotateAfterRunnningAgainstWall)
      speedCounter = speedCounter - 1
      if(speedCounter < 0 )
      speed = 1.0f
    }
  }

  def determineRotationDirectionAndStep(pVector: PVector) = {
    val angleBetween = PVector.angleBetween(pVector, dir)
    rotationCounter = (angleBetween/rotationStep).toInt
    if(((Math.random()* 10) %2)== 0)
      rotateLeft = true
    else
      rotateLeft = false
  }

  //FIXME neuen State hierfür hinzufügen und alles auslagern 
  //FIXME und natürlich noch collission detection...

  def rotateAfterRunnningAgainstWall: Boolean = {
    rotationCounter -= 1
    var finishedRotating = true
    if(rotationCounter > 0){
      if(rotateLeft)
        angle += 0.08f
      else
        angle -= 0.08f
      setRotationAngle(angle)
      finishedRotating = true
    }else if(rotationCounter > -25){
      ranAgainstTheWall = false
      finishedRotating = false
    }
    finishedRotating
  }


  def render = {
    p.pushMatrix()
    p.translate(position.x, position.y)
    p.rotate(dir.heading())
    p.scale(0.8f)
    p.fill(c)
    p.stroke(55)
    calculateSensorPositionsToBaseCoordinateSystem
    p.ellipseMode(PConstants.RADIUS)
    p.ellipse(-8,0,8,6)
    p.ellipse(3,0,6,4)
    for(pos <- sensorPositions)
      p.ellipse(pos.x, pos.y, 1,1)
    p.popMatrix()
  }
}