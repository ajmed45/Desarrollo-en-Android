import ketai.sensors.*;

float x1, y1,z1;
KetaiSensor sensor;

void setup()
{
  
  sensor = new KetaiSensor(this);
  sensor.start();
  orientation(PORTRAIT);
  
  x1 = width/2;
  y1 = height/2;
  z1=0;
  
}

void draw()
{
  background(78, 93, 75);
  point(x1,y1);
}

void onGyroscopeEvent(float x, float y, float z)
{
  
  strokeWeight(10);
  x1 += x;
  y1 += y;
  
  
  
  /*if(y1<=0 || y1>=height)
  {
   y1=height/2;
   x1=width/2;
   
  }
  
  if(x1<=0 || x1>=width)
  {
    y1=height/2;
     x1=width/2; 
     
  }
  */
  
}