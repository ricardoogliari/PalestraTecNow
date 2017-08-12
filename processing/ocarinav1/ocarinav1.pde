/**
 * Forward FFT
 * by Damien Di Fede.
 *  
 * This sketch demonstrates how to use an FFT to analyze an AudioBuffer 
 * and draw the resulting spectrum. It also allows you to turn windowing 
 * on and off, but you will see there is not much difference in the spectrum.
 * Press 'w' to turn on windowing, press 'e' to turn it off.
 */

import ddf.minim.analysis.*;
import ddf.minim.*;
import processing.net.*;
import java.util.Date;


String data;
Client c;

Minim minim;
AudioPlayer jingle;
FFT fft;
String windowName;
AudioInput in;
void setup()
{
  size(512, 200);
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 2048);
  c = new Client(this, "localhost", 8080); // Connect to server on port 80

  // create an FFT object that has a time-domain buffer the same size as jingle's sample buffer
  // note that this needs to be a power of two and that it means the size of the spectrum
  // will be 512. see the online tutorial for more info.
  fft = new FFT(in.bufferSize(), in.sampleRate());
  System.out.println("timeSize = " + in.bufferSize());
  System.out.println("sampleRate = " + in.sampleRate());
  textFont(createFont("SanSerif", 12));
  windowName = "None";
}

void draw()
{
  background(0);
  stroke(255);
  // perform a forward FFT on the samples in jingle's left buffer
  // note that if jingle were a MONO file, this would be the same as using jingle.right or jingle.left
  fft.forward(in.mix);
  text("Prototype developed by Globalcode based on Processing FFT Sample: "+height, 5, 30);
  for(int i = 0; i < fft.specSize(); i++)
  {
    // draw the line for frequency band i, scaling it by 4 so we can see it a bit better
    line(i, height, i, height - fft.getBand(i)*4);
  }
  fill(255);
  text("Prototype developed by Globalcode based on Processing FFT Sample", 5, 40);

  if(l%1001==0) {
  for(int x=0;x<2048;x++) {
    if (fft.getFreq(x) >= 50){
      if (x == 350){
        if (ultAcao != 1){
          System.out.println("liga");
          ultAcao = 1;
          sendTojHome();
        }
        
      } else if (x == 650){
        if (ultAcao != 2){
          System.out.println("desliga");
          ultAcao = 2;
          sendTojHome();
        }
      }
    }
  }
  }
  text("Prototype developed by Globalcode based on Processing FFT Sample", 5, 20);
}

int l=0;
void keyReleased()
{
  if ( key == 'w' ) 
  {
    fft.window(FFT.HAMMING);
    windowName = "Hamming";
  }

  if ( key == 'e' ) 
  {
    fft.window(FFT.NONE);
    windowName = "None";
  }
}

void stop()
{
  minim.stop();

  super.stop();
}

int notePosition(String note) {
  if(note=="C") return 0;
  else return 9;  
}  

void restartNotes() {
  for(int x=0;x<countNote.length;x++) {
    countNote[x] = 0;
  }
}

int countNote[] = new int[7];

long countMillis = 0;
long MAXMILLIS = 1000;
int  FIRECOUNT = 10;
int ultAcao = 0;//1 liga, 2 desliga
void sendTojHome() {

  if(millis() - countMillis>MAXMILLIS) { //max period expired
    restartNotes();    
  }
  countMillis=millis();
  try {
      c = new Client(this, "localhost", 8052); // Connect to server on port 80
      if(ultAcao == 1) {
              c.write("GET /ligar" + " HTTP/1.1\n"); // Use the HTTP "GET" command to ask for a Web page
      }
      else if(ultAcao == 2){
              c.write("GET /desligar" + " HTTP/1.1\n"); // Use the HTTP "GET" command to ask for a Web page
      }
      c.write("Host: localhost:8052\n\n"); // Be polite and say who we are    
    }                                                                                                                
    catch(Exception e) {
      e.printStackTrace();
      
    }    
    countMillis=0;  
    restartNotes();
}  