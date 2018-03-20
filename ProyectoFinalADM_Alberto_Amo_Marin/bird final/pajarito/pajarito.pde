 //<>// //<>//
//import ketai.sensors.*;
//KetaiSensor sensor;

PImage imgfondo1,imgfondo2; 
PImage imgpajaro,imgpajaro2; 
PImage imgmuro; 
PImage imginicial;
Button bt1, bt2;
boolean imagen1 = false, imagen2 = false;
int cont;
int estado = 1; 
int puntuacion = 0, maxPuntuacion = 0; 
int x = -200, y, vy = 0; 
// para los muros, tenemos que "wx" contiene la distancia horizontal y "wy" la vertical
int wx[] = new int[2], wy[] = new int[2]; 
void setup() { 
  size(600, 700); //usaremos este tamaño de pantalla pues es el adaptado para mi movil
  //sensor = new KetaiSensor(this);
  //sensor.start();
  imgfondo1=loadImage("playa.png");
  imgfondo2=loadImage("volcan.png");
  imgpajaro=loadImage("p1.png");
  imgpajaro2=loadImage("p2.png");
  imgmuro=loadImage("columna2.png");
  imginicial=loadImage("portada2.png");
  
  
  
  cont = 0;
  fill(0); 
  textSize(30);  
}
void draw() {  
  switch(estado)
  {
  
  case 0: // Si estamos en juego, se hará todo lo siguiente
    // ponemos el fondo del juego
    
    imageMode(CORNER);
    
    if(imagen1)
    {
      image(imgfondo1, x, 0);
      image(imgfondo1, x+imgfondo1.width, 0);  
    }
    
    if(imagen2)
    {
      image(imgfondo2, x, 0);
      image(imgfondo2, x+imgfondo1.width, 0);  
    }
    
    
    x -= width*0.01+puntuacion/2; //vamos avanzando mas rapido conforme a las columnas que vamos avanzando
    vy += 1;
    y += vy; // aceleracion en caida del pajaro
    if(x <= -1800) x = 0; // reiniciamos el fondo, pues lo hemos recorrido entero
    for(int i = 0 ; i < 2; i++) { // bucle para renderizar las paredes
      imageMode(CENTER);
      image(imgmuro, wx[i], wy[i] - (imgmuro.height/2+100));
      image(imgmuro, wx[i], wy[i] + (imgmuro.height/2+100));
      if(wx[i] < 0) { 
        wy[i] = (int)random(200,height-200);
        wx[i] = width;
      }
      if(wx[i] == width/2){ // Si atravesamos el muro
        puntuacion++; // Ganamos un punto
        maxPuntuacion = max(puntuacion, maxPuntuacion); // comprobamos si hay record
      }
      if(y>height||y<0||(abs(width/2-wx[i])<25 && abs(y-wy[i])>100)){ 
      // Si tocamos techo, suelo, o una pared,perderemos.
        estado=1; // volvemos al inicio
      }
      
      wx[i] -= (width*0.01); // velocidad de desplazamiento de las paredes
    }
    
    if(cont%2!=0)
    image(imgpajaro, width/2, y); // mostramos al pájaro en la pantalla
    if(cont%2==0)
    image(imgpajaro2, width/2, y);
    text(""+puntuacion, width/2-15, 100); // Mostramos la puntuación
  break;
  
  case 2:
  selecLevel();
  break;
  
  default: 
  // Centrar imágenes y mostramos la máxima puntuación
    imageMode(CENTER);
    imginicial.resize(width,height); //ponemos imagen principal
    image(imginicial, width/2,height/2);
    text("Máxima puntuación: "+maxPuntuacion, width/4, height/2);
    
  break;
  }
}


void mousePressed() { // Se llama cuando hacemos click
  vy = -17; // Hacemos "saltar" a nuestro pequeñín
  cont++;
  if(estado==1) { // Si el juego está en el menú
  // Inicializamos las paredes, y ponemos marcadores y modo de juego a 0
  estado = estado +1;  
  }
  
 
}

/*void onGyroscopeEvent(float x, float y, float z)
{
  cont++;
  if(x>=1)
  vy=-17;
}*/

void selecLevel()
{
  PImage img;

  
  img=loadImage("pantallas.png");
  bt1=new Button(width*0.113,height*0.13,width*0.761,height*0.293,"");
  bt2 = new Button(width*0.113,height*0.535,width*0.755,height*0.281,"");
  
  image(img,300,350);
  bt1.update();
  bt2.update();
  
  if(bt1.acti)
  {
    imagen1 = true;
    imagen2 = false;
    System.out.println("clico boton1");
    estado = 0;
    wx[0] = 600;
    wy[0] = y = height/2;
    wx[1] = 900;
    wy[1] = 500;
    x = estado = puntuacion = 0;  
}
    
  if(bt2.acti)
  {
     imagen1 = false;
     imagen2 = true;
     System.out.println("clico boton2");  
     estado = 0;
     wx[0] = 600;
    wy[0] = y = height/2;
    wx[1] = 900;
    wy[1] = 500;
    x = estado = puntuacion = 0; 
}
  
}