class Button {
    float xpos, ypos, wid, hei;
    String label;
    boolean over = false;
    boolean down = false; 
    boolean clicked = false;
    boolean acti = false;
    Button(
      float tx, float ty, 
      float tw, float th, 
      String tlabel
      ) {
      xpos = tx;
      ypos = ty;
      wid = tw;
      hei = th;
      label = tlabel;
    }

    void update() {
      
      if (down&&over&&!mousePressed) {
        clicked=true;
      } else {
        clicked=false;
      }

      
      if (mouseX>xpos && mouseY>ypos && mouseX<xpos+wid && mouseY<ypos+hei) {
        over=true;
        if (mousePressed) {
          down=true;
          System.out.println("clico el boton");
          acti = true;
          
        } else {
          down=false;
        }
      } else {
        over=false;
      }
      smooth();

      
     
    }
}