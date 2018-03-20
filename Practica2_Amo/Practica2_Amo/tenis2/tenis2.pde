/**
 * <p>Ketai Library for Android: http://KetaiProject.org</p>
 *
 * <p>KetaiWiFiDirect wraps the Android WiFiDirect Features:
 * <ul>
 * <li>Enables wifi-direct for sketch through android</li>
 * <li>Provides list of available Devices</li>
 * <li>Enables Discovery</li>
 * <li>Allows writing data to device</li>
 * </ul>
 * <p>Updated: 2012-05-18 Daniel Sauter/j.duran</p>
 */
import android.os.Bundle;
 
import ketai.net.wifidirect.*;

import ketai.net.*;
import ketai.ui.*;
import oscP5.*;
import netP5.*;

KetaiWiFiDirect net;

int rad = 10;        // Width of the shape
int xpos, ypos;    // Starting position of shape    

int xspeed = 8;  // Speed of the shape
int yspeed = 7;  // Speed of the shape

int xdirection = 1;  // Left or Right
int ydirection = 1;  // Top to Bottom
int server = 0;

String info = "";
ArrayList<String> devices = new ArrayList();
boolean isConfiguring = true;
String UIText;
OscP5 oscP5;
String clientip = "";
KetaiList connectionList;
PVector remoteCursor = new PVector();
boolean gameStart = false;
 


void setup()
{   
  //orientation(PORTRAIT);
  background(78, 93, 75);
  stroke(255);
  textSize(24);

  UIText =  "d - discover devices\n" +
    "c - connect to device\n     from peer list.\n" +
    "i - show net information\n" +
    "o - start OSC Server\n" + 
    "p - list paired devices\n" + 
    "r - reset all connections\n" ;

  //register for key events(keyPressed currently Broken)
  //registerMethod("keyEvent", this);
}

void draw()
{
  background(78, 93, 75);
  if (isConfiguring)
  {
    info="";
    //based on last key pressed lets display
    //  appropriately
    if (key == 'i')
      info = getNetInformation();
    if (key == 'd')
    {
      info = "Discovered Devices:\n";
      devices = net.getPeerNameList();
      for (int i=0; i < devices.size(); i++)
      {
        info += "["+i+"] "+devices.get(i).toString() + "\t\t"+devices.size()+"\n";
      }
    }
    else if (key == 'p')
    {
      info += "Peers: \n";
      for (String s:net.getPeerNameList())
        info+= "\t" + s + "\n";
    }
    text(UIText + "\n\n" + info, 5, 90);
  }
  else
  {
    //cuando estas conectado y le das al interact
    
    pushStyle();
    noStroke();
    Pelotita();    
    popStyle();
    
    
  }
  drawUI();
}

void Pelotita()
{

    pushStyle();
    
    stroke(0);
    strokeWeight(3);
    line(0, height*0.52, width, height*0.52);
    
    noStroke();
    fill(255);
    
    rect(mouseX, height*0.05, 150, 50);
    ellipse(mouseX, mouseY, 20, 20);
    fill(255, 0, 0);
    rect(remoteCursor.x, height*0.95, 150, 50);
    popStyle();
    if(server == 1){
      
    // Actualizar posicion de la figura
    xpos = xpos + ( xspeed * xdirection );
    ypos = ypos + ( yspeed * ydirection );
  
  // Test to see if the shape exceeds the boundaries of the screen
  // If it does, reverse its direction by multiplying by -1
  if (xpos > width || xpos < 0) {
    xdirection *= -1;
  }
  
if ((xpos > mouseX && xpos < mouseX+150 && ypos < 100) || (xpos > remoteCursor.x && xpos < remoteCursor.x+150 && ypos > height - 55) ) {
    
       ydirection *= -1;

   
  }

if(ypos < 0 || ypos > height){
  ypos = height / 2;
  xpos = width / 2;
}

  // Draw the shape
  ellipse(xpos, ypos, rad, rad);
  }
  else{
    ellipse(width - xpos, height - ypos, rad, rad);
    
  }
}

void mousePressed()
{
  //keyboard button -- toggle virtual keyboard
  gameStart = true;
  if (mouseY <= 50 && mouseX > 0 && mouseX < width/3)
    KetaiKeyboard.toggle(this);
  else if (mouseY <= 50 && mouseX > width/3 && mouseX < 2*(width/3)) //config button
  {
    isConfiguring=true;
  }
  else if (mouseY <= 50 && mouseX >  2*(width/3) && mouseX < width) // draw button
  {
    if (isConfiguring)
    {
      isConfiguring=false;
    }
  }
}

void mouseDragged()
{
  if (isConfiguring)
    return;

  OscMessage m = new OscMessage("/remoteCursor/");
  m.add(pmouseX);
  m.add(pmouseY);

  if (oscP5 != null)
  {
    NetAddress myRemoteLocation = null;

    if (clientip != "")
      myRemoteLocation = new NetAddress(clientip, 12000);
    else if (net.getIPAddress() != KetaiNet.getIP())
      myRemoteLocation = new NetAddress(net.getIPAddress(), 12000);

    if (myRemoteLocation != null)
      oscP5.send(m, myRemoteLocation);
  }
}

String getNetInformation()
{
  String Info = "Server Running: ";

  Info += "\n my IP: " + KetaiNet.getIP();
  Info += "\n initiator's IP:  " + net.getIPAddress();

  return Info;
}

void oscEvent(OscMessage m) {

  //lets send stuff back to whoever we got a message from
  if (net.getIPAddress() != m.netAddress().address())
    clientip = m.netAddress().address();

  if (m.checkAddrPattern("/remoteCursor/"))
  {
    if (m.checkTypetag("ii"))
    {
      remoteCursor.x = m.get(0).intValue();
      remoteCursor.y = m.get(1).intValue();
    }
  }
}