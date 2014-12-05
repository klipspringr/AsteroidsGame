import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AsteroidsGame extends PApplet {

SpaceShip serenity;
Stars[] billy;
ArrayList <Asteroid> hailey = new ArrayList <Asteroid>();
ArrayList <Bullet> buffaloBill = new ArrayList <Bullet>();

public void setup() 
{
  size(800,800);
  background(0);
  // frameRate(25);
  billy = new Stars[80];
  for (int i = 0; i < billy.length; i++)
  {
    billy[i] = new Stars();
  }
  serenity = new SpaceShip();
  
  for (int i = 0; i<8; i++)
  {
    hailey.add(new Asteroid());
  }
}
public void draw() 
{
  background(0);
  for (int i = 0; i < billy.length; i++)
  {
    billy[i].base();
    billy[i].wrap();
    billy[i].move();
    billy[i].show();
  }
  serenity.move();
  serenity.show();
  
  // outer:
  for (int i = 0; i < buffaloBill.size(); i++)
  {
    buffaloBill.get(i).move();
    buffaloBill.get(i).show();
    if (buffaloBill.get(i).getX() > 800 || buffaloBill.get(i).getX() < 0 || buffaloBill.get(i).getY() > 800 || buffaloBill.get(i).getY() < 0)
    {
      buffaloBill.remove(i);
      // break outer;
      break;
    }

  }

  outer:  
  for (int i = 0; i < hailey.size(); i++)
  {
    hailey.get(i).move();
    hailey.get(i).show();
    if(dist((float)hailey.get(i).getX(), (float)hailey.get(i).getY(), (float)serenity.myCenterX, (float)serenity.myCenterY) <=(8*hailey.get(i).sizeFactor))
    {
      hailey.remove(i);
      // println("removed by collision");
      break;
    }
    for (int j = 0; j < buffaloBill.size(); j++)
    {

      if(dist((float)hailey.get(i).getX(), (float)hailey.get(i).getY(), (float)buffaloBill.get(j).getX(), (float)buffaloBill.get(j).getY()) <=(8*hailey.get(i).getSizeFactor()))
      {
        
        hailey.remove(i);
        // System.out.println("Asteroid " + i);?
        buffaloBill.remove(j);
        // System.out.println("Bullet " + j);
        // println("removed by bullet");
        break outer;
      } 
    
   }
  }

  
}
public void keyPressed()
{
    if(keyCode == LEFT) 
    {
      serenity.rotate(-13);
    }
    if(keyCode == RIGHT)
    {
      serenity.rotate(13);
    }
    if(keyCode == UP)
    {
      serenity.accelerate(.5f);
      serenity.booster();
    }
    if(keyCode == DOWN)
    {
      serenity.accelerate(-.5f);
    }
    if(keyCode == ENTER || keyCode == RETURN)
    {
      serenity.setX((int)((Math.random()*800)));
      serenity.setY((int)((Math.random()*800)));
      serenity.setDirectionX(0);
      serenity.setDirectionY(0);
      serenity.setPointDirection((int)(Math.random()*360));
    }
    if(keyCode == TAB)
    {
    
      buffaloBill.add(new Bullet(serenity));
    }

}
class Stars 
{
  int y, opacity;
  float x;
  boolean isMoving;
  Stars()
  {
    x = ((int)(random(0, 800)));
    y = ((int)(random(0, 800)));
    isMoving = true;
    opacity = 255;
  }
  public void show()
  {

    fill(242,242,242,opacity);
    stroke(0);
    ellipse(x, y, 7, 7) ;
    stroke(255, opacity);
    line(x - 4, y - 4, x + 4, y + 4); //diagonal top left
    line(x, y + 5, x, y - 5); //perpendicular
    line(x + 5, y, x - 5, y); //horizontal
    line(x - 4, y +4, x+4, y-4);
    stroke(0);
     fill(0, 255, 0);   
    stroke(0, 255, 0);  

  }
  public void base()
  {
    fill(0);
    stroke(0);

    ellipse(x, y, 12, 12);
    fill(0,255,0);
    stroke(0,255,0);
  }
  public void move()
  {
    if (isMoving == true)
    {
      x+= 0.5f;
      opacity = (int)((Math.random()*150)+105);
    }
  }
  public void wrap()
  {
    if(x >= 800)
    {
      x = 0;
      y = ((int)(random(0, 800)));
    }
  }
}

abstract class Floater //Do NOT modify the Floater class! Make changes in the SpaceShip class 
{   
  protected int corners;  //the number of corners, a triangular floater has 3   
  protected int[] xCorners;   
  protected int[] yCorners;   
  protected int myColor;   
  protected double myCenterX, myCenterY; //holds center coordinates   
  protected double myDirectionX, myDirectionY; //holds x and y coordinates of the vector for direction of travel   
  protected double myPointDirection; //holds current direction the ship is pointing in degrees    
  abstract public void setX(int x);  
  abstract public int getX();   
  abstract public void setY(int y);   
  abstract public int getY();   
  abstract public void setDirectionX(double x);   
  abstract public double getDirectionX();   
  abstract public void setDirectionY(double y);   
  abstract public double getDirectionY();   
  abstract public void setPointDirection(int degrees);   
  abstract public double getPointDirection(); 

  //Accelerates the floater in the direction it is pointing (myPointDirection)   
  public void accelerate (double dAmount)   
  {          
    //convert the current direction the floater is pointing to radians    
    double dRadians =myPointDirection*(Math.PI/180);     
    //change coordinates of direction of travel    
    myDirectionX += ((dAmount) * Math.cos(dRadians));    
    myDirectionY += ((dAmount) * Math.sin(dRadians));       
  }   
  public void rotate (int nDegreesOfRotation)   
  {     
    //rotates the floater by a given number of degrees    
    myPointDirection+=nDegreesOfRotation;   
  }   
  public void move ()   //move the floater in the current direction of travel
  {      
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;     

    //wrap around screen    
    if(myCenterX >width)
    {     
      myCenterX = 0;    
    }    
    else if (myCenterX<0)
    {     
      myCenterX = width;    
    }    
    if(myCenterY >height)
    {    
      myCenterY = 0;    
    }   
    else if (myCenterY < 0)
    {     
      myCenterY = height;    
    }   
  }   
  public void show ()  //Draws the floater at the current position  
  {             
    fill(242, 5, 5);   
    stroke(myColor);    
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);                 
    int xRotatedTranslated, yRotatedTranslated;    
    beginShape();         
    for(int nI = 0; nI < corners; nI++)    
    {     
      //rotat                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 e and translate the coordinates of the floater using current direction 
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);     
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY);      
      vertex(xRotatedTranslated,yRotatedTranslated);    
    }   
    endShape(CLOSE);  
    stroke(0);
  }  
} 
class SpaceShip extends Floater  
{   
  private int[] bxCorners;   
  private int[] byCorners;  
  private int bCorners;
  SpaceShip()
  {
    corners = 4;
    myColor = color(140);
    xCorners = new int[corners];
    yCorners = new int[corners];
    xCorners[0] = -8;
    yCorners[0] = -8;
    xCorners[1] = 16;
    yCorners[1] = 0;
    xCorners[2] = -8;
    yCorners[2] = 8;
    xCorners[3] = -2;
    yCorners[3] = 0;
    myCenterX = 400;
    myCenterY = 400;
    myDirectionX = 0;
    myDirectionY = 0;
    myPointDirection = 0;

    bCorners = 6;
    bxCorners = new int[bCorners];
    byCorners = new int[bCorners];
    bxCorners[0] = 2;
    byCorners[0] = 0;
    bxCorners[1] = -12;
    byCorners[1] = 5;
    bxCorners[2] = -9;
    byCorners[2] = 3;
    bxCorners[3] = -10;
    byCorners[3] = 0;
    bxCorners[4] = -9;
    byCorners[4] = -3;
    bxCorners[5] = -12;
    byCorners[5] = -5;

  }
  public void setX(int x){myCenterX = x;}
  public int getX(){return (int)myCenterX;}
  public void setY(int y){myCenterY = y;}
  public int getY(){return (int)myCenterY;}
  public void setDirectionX(double x){myDirectionX = x;}
  public double getDirectionX(){return myDirectionX;}
  public void setDirectionY(double y){myDirectionY = y;}
  public double getDirectionY(){return myDirectionY;}
  public void setPointDirection(int degrees){myPointDirection = degrees;}
  public double getPointDirection(){return myPointDirection;}
  public void booster()
  {
    fill(0);   
    stroke(255, 174, 61);    
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);                 
    int xRotatedTranslated, yRotatedTranslated;    
    beginShape();         
    for(int nI = 0; nI < corners; nI++)    
    {     
      //rotat                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 e and translate the coordinates of the floater using current direction 
      xRotatedTranslated = (int)((bxCorners[nI]* Math.cos(dRadians)) - (byCorners[nI] * Math.sin(dRadians))+myCenterX);     
      yRotatedTranslated = (int)((bxCorners[nI]* Math.sin(dRadians)) + (byCorners[nI] * Math.cos(dRadians))+myCenterY);      
      vertex(xRotatedTranslated,yRotatedTranslated);    
    }   
    endShape(CLOSE);  
    stroke(0);

  }
}
class Asteroid extends Floater
{
  private int asteroidRotation, sizeFactor;
  Asteroid()
  {
    corners = 9;
    myColor = color(173);
    sizeFactor = (int)((Math.random()*4)+2);
    xCorners = new int[corners];
    yCorners = new int[corners];
    xCorners[0] = 6 * sizeFactor;
    yCorners[0] = 2* sizeFactor;
    xCorners[1] = 2* sizeFactor;
    yCorners[1] = 2* sizeFactor;
    xCorners[2] = 6* sizeFactor;
    yCorners[2] = -2* sizeFactor;
    xCorners[3] = 2* sizeFactor;
    yCorners[3] = -6* sizeFactor;
    xCorners[4] = -2* sizeFactor;
    yCorners[4] = -6* sizeFactor;
    xCorners[5] = -6* sizeFactor;
    yCorners[5] = -2* sizeFactor;
    xCorners[6] = -6* sizeFactor;
    yCorners[6] = 2* sizeFactor;
    xCorners[7] = -2* sizeFactor;
    yCorners[7] = 6 * sizeFactor;
    xCorners[8] = 2* sizeFactor;
    yCorners[8] = 6* sizeFactor;
    myCenterX = (int)((Math.random()*800));;
    myCenterY = (int)((Math.random()*800));;
    myDirectionX = (int)((Math.random()*8)-4);
    myDirectionY = (int)((Math.random()*8)-4);
    myPointDirection = 0;
    asteroidRotation = (int)((Math.random()*20)-10);
  }
  public void setX(int x){myCenterX = x;}
  public int getX(){return (int)myCenterX;}
  public void setY(int y){myCenterY = y;}
  public int getY(){return (int)myCenterY;}
  public void setDirectionX(double x){myDirectionX = x;}
  public double getDirectionX(){return myDirectionX;}
  public void setDirectionY(double y){myDirectionY = y;}
  public double getDirectionY(){return myDirectionY;}
  public void setPointDirection(int degrees){myPointDirection = degrees;}
  public double getPointDirection(){return myPointDirection;}
  public int getSizeFactor(){return sizeFactor;}

  public void move()
  { 
 
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;     

    //wrap around screen    
    if(myCenterX >width)
    {     
      myCenterX = 0;    
    }    
    else if (myCenterX<0)
    {     
      myCenterX = width;    
    }    
    if(myCenterY >height)
    {    
      myCenterY = 0;    
    }   
    else if (myCenterY < 0)
    {     
      myCenterY = height;    
    }  
     myPointDirection+=asteroidRotation; 
     
  }
  public void show()
  {
    fill(173);   
    stroke(150);    
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);                 
    int xRotatedTranslated, yRotatedTranslated;    
    beginShape();         
    for(int nI = 0; nI < corners; nI++)    
    {     
      //rotat                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 e and translate the coordinates of the floater using current direction 
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);     
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY);      
      vertex(xRotatedTranslated,yRotatedTranslated);    
    }   
    endShape(CLOSE);  
    stroke(0);
  }  
  
}
class Bullet extends Floater
{

  Bullet(SpaceShip theShip)
  {
    myCenterX = theShip.getX();
    myCenterY = theShip.getY();
    myPointDirection = theShip.getPointDirection();
    double dRadians = myPointDirection*(Math.PI/180);    
    myDirectionX = 5 * Math.cos(dRadians) + theShip.getDirectionX();
    myDirectionY = 5 * Math.sin(dRadians) + theShip.getDirectionY();
  }
  public void setX(int x){myCenterX = x;}
  public int getX(){return (int)myCenterX;}
  public void setY(int y){myCenterY = y;}
  public int getY(){return (int)myCenterY;}
  public void setDirectionX(double x){myDirectionX = x;}
  public double getDirectionX(){return myDirectionX;}
  public void setDirectionY(double y){myDirectionY = y;}
  public double getDirectionY(){return myDirectionY;}
  public void setPointDirection(int degrees){myPointDirection = degrees;}
  public double getPointDirection(){return myPointDirection;}
  public void show()
  {
    fill(0, 255, 0);   
    stroke(0, 255, 0);    
    ellipse((float)myCenterX, (float)myCenterY, 5, 5);
    
  }
  public void move()
  {

    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;  
  }

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AsteroidsGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
