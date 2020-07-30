/*
 * to-dos:
 * as of 6/10/20
 * 1. make fastled-like palette gen function better
 * 2. test - do we need to store pixel state or should we fetch it with this new fast processor? Big refactor tho.
 * 3. Make random randomer with a seed 
 * 4. MQTT front-end; buttons, color wheel, etc. Android app?
 * 5. remove prints / move to debug 
 * 6. new patterns - chase, shooting star, blended colors, fix twinkling stars, slow color fades
*/

#include <Arduino.h>
#include <Adafruit_NeoPixel.h>
#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ESP8266mDNS.h>
#include <WiFiUdp.h>
#include <ArduinoOTA.h>
#include "config.h"

/************ MUMBO-JUMBO! ******************/

#ifndef min /* some chips don't have these fxns defined */
#define min(a,b) (((a) < (b)) ? (a) : (b))
#endif
#ifndef max
#define max(a,b) (((a) > (b)) ? (a) : (b))
#endif

/* prints free memory to serial to debug leaks */
#ifdef __arm__
extern "C" char* sbrk(int incr);
#else  // __ARM__
extern char *__brkval;
#endif  // __arm__

extern "C" {
#include "user_interface.h"
}

int freeMemory() {
  char top;
#ifdef __arm__
  return &top - reinterpret_cast<char*>(sbrk(0));
#elif defined(CORE_TEENSY) || (ARDUINO > 103 && ARDUINO != 151)
  return &top - __brkval;
#else  // __arm__
  return __brkval ? &top - __brkval : &top - __malloc_heap_start;
#endif  // __arm__
}

/************************* GLOBALS *****************************************************/
/* WIFI and MQTT Information */
const char* ssid = SSI; 
const char* password = WIFI_PASSWORD;
const char* mqtt_server = RPI_IP;
const char* mqtt_username = MQTT_U;
const char* mqtt_password = MQTT_P;
const int mqtt_port = MQTT_PRT;
const byte MAX_ATTEMPTS = 10;
WiFiClient espClient;
PubSubClient client(espClient);
WiFiServer TelnetServer(23);
WiFiClient Telnet;
unsigned long lastMem = 0;

/* FOR OTA */
const char* LIGHTNAME = LIGHT;
const char* OTApassword = OTApass;
int OTAport = 8266;

/* MQTT TOPICS */
const char* light_state_topic = "lamp/state"; 
const char* light_pattern_topic = "lamp/pattern";
const char* light_ack_topic = "lamp/ack";

/*FOR JSON */
const int BUFFER_SIZE = JSON_OBJECT_SIZE(10);
#define MQTT_MAX_PACKET_SIZE 512

/* NEOPIXEL SETUP */
const byte NUM_PIXELS = 115;
const byte NUM_STRIPS = 2;
const byte TOTAL_PIXELS = NUM_PIXELS*NUM_STRIPS;
extern const uint8_t gamma8[];
const String Pins[NUM_STRIPS] = {"D2","D4"};
#define PIN_0 D2
#define PIN_1 D4
Adafruit_NeoPixel Strips[NUM_STRIPS] = {
  Adafruit_NeoPixel(NUM_PIXELS, PIN_0, NEO_GRBW + NEO_KHZ800),
  Adafruit_NeoPixel(NUM_PIXELS, PIN_1, NEO_GRBW + NEO_KHZ800)
};

/* COLORS */
//TODO: store on 'disk' like gamma
const uint32_t BrightOrange = Adafruit_NeoPixel::Color(255, 64, 0, 64);
const uint32_t Orange = Adafruit_NeoPixel::Color(255, 64, 0, 10);
const uint32_t DarkOrange = Adafruit_NeoPixel::Color(255, 32, 0, 10);
const uint32_t Red = Adafruit_NeoPixel::Color(255, 0, 0, 10);
const uint32_t RedWhiteWhite = Adafruit_NeoPixel::Color(255, 0, 0, 10);
const uint32_t RedWhite = Adafruit_NeoPixel::Color(255, 0, 0, 50);
const uint32_t RedRedWhite = Adafruit_NeoPixel::Color(255, 0, 0, 75);
const uint32_t Yellow = Adafruit_NeoPixel::Color(255, 50, 0, 20);
const uint32_t YellowWhite = Adafruit_NeoPixel::Color(255, 100, 0, 50);
const uint32_t White = Adafruit_NeoPixel::Color(30, 0, 0, 255);
const uint32_t Purple = Adafruit_NeoPixel::Color(100, 0, 100, 0);
const uint32_t Green = Adafruit_NeoPixel::Color(0, 255, 0, 10);
const uint32_t Blue = Adafruit_NeoPixel::Color(0, 0, 255, 10);
const uint32_t Pink = Adafruit_NeoPixel::Color(255, 20, 147, 0);
const uint32_t Off = Adafruit_NeoPixel::Color(0,0,0,0);
const uint32_t StarWhite0 = Adafruit_NeoPixel::Color(0,0,250,255);
const uint32_t StarWhite1 = Adafruit_NeoPixel::Color(0,170,250,255);
const uint32_t StarWhite2 = Adafruit_NeoPixel::Color(120,0,250,255);
const uint32_t StarWhite3 = Adafruit_NeoPixel::Color(0,0,170,255);
const uint32_t StarWhite4 = Adafruit_NeoPixel::Color(0,120,120,255);
const uint32_t StarWhite5 = Adafruit_NeoPixel::Color(0,0,255,255);
const uint32_t StarWhite6 = Adafruit_NeoPixel::Color(0,140,255,255);
const uint32_t StarWhite7 = Adafruit_NeoPixel::Color(180,0,255,255);
const uint32_t StarWhite8 = Adafruit_NeoPixel::Color(0,0,145,255);
const uint32_t StarWhite9 = Adafruit_NeoPixel::Color(0,100,200,255);
const uint32_t Neutral1 = Adafruit_NeoPixel::Color(255,50,155,255);

/* COLOR GROUPS */
uint32_t fireColors[8] = {Orange, DarkOrange, Red, DarkOrange, Red, Red, Yellow, YellowWhite};
uint32_t aurora[6] = {DarkOrange, RedWhiteWhite, Blue, Purple, Yellow, Green};
uint32_t redGlitter[3] = {RedWhiteWhite, RedWhite, RedRedWhite};
uint32_t blueGlitter[3] = {Blue, Purple, White};
uint32_t stars[5] = {StarWhite0, StarWhite1, StarWhite2,StarWhite3, StarWhite4};
uint32_t allStars[10] = {StarWhite0, StarWhite1, StarWhite2,StarWhite3, StarWhite4,
                               StarWhite5, StarWhite6, StarWhite7,StarWhite8, StarWhite9,};
uint32_t girly[4] = {Blue, Green, Purple, Pink};
uint32_t allColors[19] = {DarkOrange, Red, RedWhiteWhite, RedWhite, RedRedWhite, Yellow, YellowWhite, 
                        White, Purple, Green, Blue, Pink, StarWhite0, StarWhite1, StarWhite2, StarWhite3,
                        StarWhite4, StarWhite5, Neutral1};

/* PATTERN PARAMETERS */
// these are all MQTTABLE
char* stateOn = "false";
//glitter
byte chanceOfGlitter = 200; //pair a higher number with slower delays for a candel-ey effect
float starBrightness = 1.0; // 0 to 1 please
byte glitterDimDelayMax =  15; //longer is slower glitter. 5 is glittery. 20 is fireworky. 100 is like a candle
byte glitterDimDelayMin =  4; 
byte glitterDimMax = 50;
byte glitterDimMin = 3;
byte glitterBrightenDelayMin = 0;
byte glitterBrightenDelayMax = 0;

//ember
byte emberDelayMin = 0;
byte emberDelayMax = 0;
byte emberBrightenMin = 1;
byte emberBrightenMax = 5;
byte emberDimMin = 1;
byte emberDimMax = 5;
byte emberBrightnessTriggerMin = 220; 
byte emberBrightnessTriggerMax = 255; 

byte solidColorRed = 255;
byte solidColorGreen = 50;
byte solidColorBlue = 155;
byte solidColorWhite = 255;

int numColors = 10; //size of the palette array. auto updated when you update the palette.
uint32_t *palette = allStars; //this is the group of colors the pattern will use

byte pattern = 0; // 0 = EMBER; 1 = GLITTER; 2 = TWINKLE 3 = SOLID, 4 = ember + glitter

/****************************** WIFI, TELNET, AND MQTT FUNCTIONS *******************/
void setup_wifi() {
  WiFi.printDiag(Serial);
  delay(50);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.setSleepMode(WIFI_NONE_SLEEP);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    WiFi.printDiag(Serial);

  }
  Serial.println("");
  TelnetServer.begin();
  TelnetServer.setNoDelay(true);
  Telnet.println("WiFi connected");
  Telnet.println("IP address: ");
  Telnet.println(WiFi.localIP());
  WiFi.printDiag(Serial);
}

void handleTelnet() {
  if (TelnetServer.hasClient()) {
    if (!Telnet || !Telnet.connected()) {
      if (Telnet) Telnet.stop();
      Telnet = TelnetServer.available();
    }
    else {
      TelnetServer.available().stop();
    }
  }
}

void reconnect() { //reconnect to the MQTT server
  while (!client.connected()) {
    Telnet.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect(LIGHTNAME, mqtt_username, mqtt_password)) {
      Telnet.println("connected");
      client.subscribe(light_pattern_topic);
      client.subscribe(light_state_topic);
    } else {
      Telnet.print("failed, rc=");
      Telnet.print(client.state());
      Telnet.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }    
}

void callback(char* topic, byte* payload, unsigned int length) { //choose a function based on the topic (done) and send state back (not yet implemented)
  Telnet.print("Message arrived [");
  Telnet.print(topic);
  Telnet.print("] ");

  char message[length + 1];
  for (int i = 0; i < length; i++) {
    message[i] = (char)payload[i];
  }
  message[length] = '\0';
  Telnet.println(message);
  if (strcmp(topic,light_pattern_topic) == 0) {
    Telnet.println("running pattern parse");
    Serial.println("running pattern parse");
    if (!patternJson(message)) {
      return;
    } 
  }
  else if (strcmp(topic,light_state_topic) == 0) {
    if (!stateJson(message)) {
      return;
    }
  }
  Telnet.println(stateOn);  
  client.publish(light_ack_topic, message, true); //need to make the message jsonable
}

void sendState() { //under development; would be useful for debugging json parsing errors.
  StaticJsonDocument<BUFFER_SIZE> jsonBuffer;
  serializeJson(jsonBuffer, Serial);
//  client.publish(light_ack_topic, buffer, true);
}

void modifyPointer(uint32_t *&pp, uint32_t pointee[]) { //change the color palette
    pp = pointee;
}

bool stateJson(char* message) {
  StaticJsonDocument<BUFFER_SIZE> jsonBuffer;
  DeserializationError err = deserializeJson(jsonBuffer, message);

  if (err) {
    Telnet.println("parseObject() failed");
    Telnet.println(err.c_str());
    return false;
  }

  if (jsonBuffer.containsKey("stateOn")) {
    if (strcmp("true", jsonBuffer["stateOn"]) == 0) {stateOn = "true";}
    else {stateOn = "false";}
  }
  return true;
  
}

bool patternJson(char* message) {
  StaticJsonDocument<BUFFER_SIZE> jsonBuffer;
  DeserializationError err = deserializeJson(jsonBuffer, message);

  if (err) {
    Telnet.println("parseObject() failed");
    Telnet.println(err.c_str());
    return false;
  }

  if (jsonBuffer.containsKey("pattern")) {
    pattern = (int)jsonBuffer["pattern"];
  }

  if (jsonBuffer.containsKey("chanceOfGlitter")) {
    chanceOfGlitter = (int)jsonBuffer["chanceOfGlitter"];
  }
  
  if (jsonBuffer.containsKey("glitterDimDelayMax")) {
    glitterDimDelayMax = (int)jsonBuffer["glitterDimDelayMax"];
  }
  
  if (jsonBuffer.containsKey("glitterDimDelayMin")) {
    glitterDimDelayMin = (int)jsonBuffer["glitterDimDelayMin"];
  }

  if (jsonBuffer.containsKey("glitterDimMax")) {
    glitterDimMax = (int)jsonBuffer["glitterDimMax"];
  }
  
  if (jsonBuffer.containsKey("glitterDimMin")) {
    glitterDimMin = (int)jsonBuffer["glitterDimMin"];
  }
  
  if (jsonBuffer.containsKey("glitterBrightenDelayMin")) {
    glitterBrightenDelayMin = (int)jsonBuffer["glitterBrightenDelayMin"];
  }
  
  if (jsonBuffer.containsKey("glitterBrightenDelayMax")) {
    glitterBrightenDelayMin = (int)jsonBuffer["glitterBrightenDelayMax"];
  }
  
  if (jsonBuffer.containsKey("emberDelayMin")) {
    emberDelayMin = (byte)jsonBuffer["emberDelayMin"];
  }

  if (jsonBuffer.containsKey("emberDelayMax")) {
    emberDelayMax = (byte)jsonBuffer["emberDelayMax"];
  }
  
  if (jsonBuffer.containsKey("emberBrightenMin")) {
    emberBrightenMin = (byte)jsonBuffer["emberBrightenMin"];
  }
  
  if (jsonBuffer.containsKey("emberBrightenMax")) {
    emberBrightenMax = (byte)jsonBuffer["emberBrightenMax"];
  }
  
  if (jsonBuffer.containsKey("emberDimMin")) {
    emberDimMin = (byte)jsonBuffer["emberDimMin"];
  }
  
  if (jsonBuffer.containsKey("emberDimMax")) {
    emberDimMax = (byte)jsonBuffer["emberDimMax"];
  }
  
  if (jsonBuffer.containsKey("emberBrightnessTriggerMin")) {
    emberBrightnessTriggerMin = (byte)jsonBuffer["emberBrightnessTriggerMin"];
  }

  if (jsonBuffer.containsKey("emberBrightnessTriggerMax")) {
    emberBrightnessTriggerMax = (byte)jsonBuffer["emberBrightnessTriggerMax"];
  }
  
  if (jsonBuffer.containsKey("starBrightness")) {
    starBrightness = (float)jsonBuffer["starBrightness"];
  }
    
  if (jsonBuffer.containsKey("rgbw")) {
    JsonArray rgbw = jsonBuffer["rgbw"];
    solidColorRed = rgbw[0];
    solidColorGreen = rgbw[1];
    solidColorBlue = rgbw[2];
    solidColorWhite = rgbw[3];    
  }  

  if (jsonBuffer.containsKey("solidColorRed")) {
    solidColorRed = (byte)jsonBuffer["solidColorRed"];
  }
  
  if (jsonBuffer.containsKey("solidColorGreen")) {
    solidColorGreen = (byte)jsonBuffer["solidColorGreen"];
  }

  if (jsonBuffer.containsKey("solidColorBlue")) {
    solidColorBlue = (byte)jsonBuffer["solidColorBlue"];
  }
  
  if (jsonBuffer.containsKey("solidColorWhite")) {
    solidColorWhite = (byte)jsonBuffer["solidColorWhite"];
  }
  
  if (jsonBuffer.containsKey("palette")) {
    String pal = jsonBuffer["palette"];
    if (pal == "fireColors") {
      modifyPointer(palette, fireColors);
      numColors = sizeof(fireColors)/sizeof(fireColors[0]);
    }
    if (pal == "aurora") {
      modifyPointer(palette, aurora);
      numColors = sizeof(aurora)/sizeof(aurora[0]);
    }
    if (pal == "redGlitter") {
      modifyPointer(palette, redGlitter);
      numColors = sizeof(redGlitter)/sizeof(redGlitter[0]);
    }
    if (pal == "blueGlitter") {
      modifyPointer(palette, blueGlitter);
      numColors = sizeof(blueGlitter)/sizeof(blueGlitter[0]);
    }
    if (pal == "stars") {
      modifyPointer(palette, stars);
      numColors = sizeof(stars)/sizeof(stars[0]);
    }
    if (pal == "allStars") {
      modifyPointer(palette, allStars);
      numColors = sizeof(allStars)/sizeof(allStars[0]);
    }
    if (pal == "girly") {
      modifyPointer(palette, girly);
      numColors = sizeof(girly)/sizeof(girly[0]);
    }
    if (pal == "allColors") {
      modifyPointer(palette, allColors);
      numColors = sizeof(allColors)/sizeof(allColors[0]);
    }
  }
  return true;
}

/****************************** PIXEL CLASS FOR SMARTER CONTROL ***********************/
//saving the state of every pixel in order to:
//not have to poll the pixel for its color (slow)
//dim and fade in smartly
//store last updated status
//keep logic for what the pixel should do with the state of the pixel
//should we have pattern logic in here? seems like we shouldn't
class PixelState {
  byte brightness;
  byte maxBrightness;
  byte strip;
  byte pixel;
  uint32_t color;
  
  byte state;
  byte initialWhite;
  byte initialRed;
  byte initialGreen;
  byte initialBlue;

  uint32_t initialColor;
  unsigned long lastEmber;
  unsigned long lastGlitterBrighten;
  unsigned long lastGlitterDim;
  
   
  public:
  PixelState() {
    brightness = 0;
    strip = 0;
    pixel = 0;
    color = 0;
    state = 0;

    initialColor = 0;
    initialWhite;
    initialRed;
    initialGreen;
    initialBlue;
    lastEmber = 0;
    lastGlitterBrighten = 0;
    lastGlitterDim = 0;
    
  }
  
  void setBright(int b) {
    brightness = b;
  }

  byte getBright() {
    return brightness;
  }

  void setStrip(int s) {
    strip = s;
  }

  byte getStrip() {
    return strip;
  }

  void setPixel(int p) {
    pixel = p;
  }

  int getPixel() {
    return pixel;
  }

  void setState(int s) {
    state = s;
  }

  byte getState() {
    return state;
  }
  
  void setColor() {
    initialColor = palette[random(0,numColors)];
    initialWhite = (initialColor >> 24) & 0xff;
    initialRed = (initialColor >> 16) & 0xff;
    initialGreen = (initialColor >> 8) & 0xff;
    initialBlue = initialColor & 0xff;
    
  }
  /*
  //using the palette thing i haven't quite built yet
  void setColor(uint32_t *p) {
    byte r = random(0,256);
    initialColor = p[r];
    initialWhite = (initialColor >> 24) & 0xff;
    initialRed = (initialColor >> 16) & 0xff;
    initialGreen = (initialColor >> 8) & 0xff;
    initialBlue = initialColor & 0xff;
  }
*/
  void ember() {
    if (maxBrightness > random(emberBrightnessTriggerMin, emberBrightnessTriggerMax)) {
      emberDim();
    }
    else {
      emberBrighten();
    }
    lastEmber = millis();

  }

  
  void brighten() { //glitter brighten
    if (millis() - random(glitterBrightenDelayMin, glitterBrightenDelayMax) > lastGlitterBrighten) {
      if (color == 0 ) {
        setColor();
        maxBrightness = 0;
      }
      for (int i = 0; i < 255; i++) {
        if (brightness == 255) {
          break;
        }
        brightness = min(255, brightness + 1);
        int gammaBrightness = pgm_read_byte(&gamma8[brightness]);    
        color = Adafruit_NeoPixel::Color(gammaBrightness*initialRed/255,
                                                     gammaBrightness*initialGreen/255,
                                                     gammaBrightness*initialBlue/255,
                                                     gammaBrightness*initialWhite/255);
        Strips[strip].setPixelColor(pixel, color);
        maxBrightness = max(brightness, maxBrightness);
      }
      lastGlitterBrighten = millis();    
    }
    /*
    */
    return;
  }

  void dim() { // glitter dim. again, sine wave, and gamma adjustments
    if (millis() - random(glitterDimDelayMin, glitterDimDelayMax) > lastGlitterDim) {
      if (color != 0) {
        brightness = max(0, brightness - random(glitterDimMin, glitterDimMin));
        int gammaBrightness = pgm_read_byte(&gamma8[brightness]);  

        //Ember: random(1,30)
        //Glitter: random(5,10)
        color = Adafruit_NeoPixel::Color(gammaBrightness*initialRed/255,
                                                     gammaBrightness*initialGreen/255,
                                                     gammaBrightness*initialBlue/255,
                                                     gammaBrightness*initialWhite/255);
        Strips[strip].setPixelColor(pixel, color);
        if (color == 0) {
          maxBrightness = 0;
          brightness = 0;
        }
      }
      lastGlitterDim = millis();
    }
  }

  //ember brighten
  //this could be combined with the glitter function
  //was broken out because the arduino was weirdly going slow when passing parameters to this
  //might not be a problem anymore with the esp8266
  void emberBrighten() {
    if (millis() - random(emberDelayMin, emberDelayMax) > lastEmber) {
      if (color == 0 ) {
        setColor();
        maxBrightness = 0;
      }
      brightness = min(255, brightness + random(emberBrightenMin,emberBrightenMax));
      int gammaBrightness = pgm_read_byte(&gamma8[brightness]);    
      color = Adafruit_NeoPixel::Color(gammaBrightness*initialRed/255,
                                                   gammaBrightness*initialGreen/255,
                                                   gammaBrightness*initialBlue/255,
                                                   gammaBrightness*initialWhite/255);
      Strips[strip].setPixelColor(pixel, color);
      maxBrightness = max(brightness, maxBrightness);
    }
  }

  //emmber dim
  void emberDim() {
    if (millis() - random(emberDelayMin, emberDelayMax) > lastEmber) {
      if (color != 0) {
        brightness = max(0, brightness - random(emberDimMin, emberDimMax));
        int gammaBrightness = pgm_read_byte(&gamma8[brightness]);  

        color = Adafruit_NeoPixel::Color(gammaBrightness*initialRed/255,
                                                     gammaBrightness*initialGreen/255,
                                                     gammaBrightness*initialBlue/255,
                                                     gammaBrightness*initialWhite/255);
        Strips[strip].setPixelColor(pixel, color);
        if (color == 0) {
          maxBrightness = 0;
          brightness = 0;
        }
      }
      lastEmber = millis();
    }
  }

  void setColor(uint32_t c) {
    color = c;
  }
   
};

PixelState pixels[TOTAL_PIXELS];


/************************************* PATTERN FUNCTIONS ******************************/

void ember() {
  for (int pixel = 0; pixel < NUM_STRIPS*NUM_PIXELS; pixel++) {
    pixels[pixel].ember();
  }
  
  for (int strip = 0; strip < NUM_STRIPS; strip++) {
    Strips[strip].show();
  }
}

void twinkleStars() { //stars pattern:  //Pick a random 30% of LEDS. Make them twinkle. Keep them dim. Cycle which 30% are going based on a timer.
  for (int pixel = 0; pixel < TOTAL_PIXELS*starBrightness; pixel++) {
    //if off
    if (pixels[pixel].getState() == 0) {
      pixels[pixel].setColor();
      pixels[pixel].setState(1);
      pixels[pixel].setBright(random(30, 100)); 
    }
    else if (pixels[pixel].getState() == 1) {
      pixels[pixel].brighten(); 
      if (pixels[pixel].getBright() > random(150, 200)) {
        pixels[pixel].setState(3);
      }
    }
    else if (pixels[pixel].getState() == 3) {
      pixels[pixel].dim();
      if (pixels[pixel].getBright() < random(30, 50)) {
        pixels[pixel].setState(0);
      }
    }
  }
  for (int strip = 0; strip < NUM_STRIPS; strip++) {
    Strips[strip].show();
  }
}

void glitter() {
  for (int pixel = 0; pixel < NUM_STRIPS*NUM_PIXELS; pixel++) {
    if (pixels[pixel].getBright() > 0) { //always dim pixels that are on
      pixels[pixel].dim();      
    }
  }
  if (random(0,255) > chanceOfGlitter) { 
    int b = random(20,30);
    int r = random(TOTAL_PIXELS);
    pixels[r].setColor();
    pixels[r].setBright(b);
    pixels[r].brighten();
  }
  for (int strip = 0; strip < NUM_STRIPS; strip++) {
    Strips[strip].show();
  }
}

void solidColor(byte r, byte g, byte b, byte w) {
  uint32_t c = Adafruit_NeoPixel::Color(r, g, b, w);
  for (int pixel = 0; pixel < NUM_STRIPS*NUM_PIXELS; pixel++) {
    int strip = pixels[pixel].getStrip();
    int p = pixels[pixel].getPixel();
    if (Strips[strip].getPixelColor(p) != c) {
      Strips[strip].setPixelColor(p, c);
    }
  }
  delay(50);
  
  for (int strip = 0; strip < NUM_STRIPS; strip++) {
    Strips[strip].show();
  }
}

uint32_t *palette_gen() { //beta. Working on a way to create palettes fastled-style. It currently kinda works
  static uint32_t p[255];
  for (int i = 0; i < 128; i++) {
    byte red = map(i, 0, 128, 0, 255);
    p[i] = Adafruit_NeoPixel::Color(red, 0, 0, 0);
  }

  for (int i = 128; i < 224; i++) {
    byte green = map(i, 128, 224, 0, 255);
    p[i] = Adafruit_NeoPixel::Color(255, green, 0, 0);
  }

  for (int i = 224; i < 256; i++) {
    byte white = map(i, 224, 256, 0, 255);
    byte red = map(i, 224, 255, 255, 0);
    byte green = map(i, 224, 255, 255, 0);
    p[i] = Adafruit_NeoPixel::Color(red, green, 0, white);
  }
  return p;
//0,     0,  0,  0,   //black
//128,   255,  0,  0,   //red
//224,   255,255,  0,   //bright yellow
//255,   255,255,255 }; //full white
}
//uint32_t *p;
 
/********************************** START SETUP*****************************************/

void setup() {
  delay(1000); //wait for chip to settle
  ESP.wdtDisable();
  ESP.wdtEnable(WDTO_8S); //fixing watchdog resets

  
  for (int i = 0; i<NUM_STRIPS*NUM_PIXELS; i++) { //initialize the pixel state array 
    int strip = i/NUM_PIXELS;
    pixels[i].setStrip(strip);
    int stripPixel = i;
    if (strip > 0) {
      stripPixel = stripPixel - strip*NUM_PIXELS;
    }
   pixels[i].setPixel(stripPixel);
  }
  
  for (int strip = 0; strip < NUM_STRIPS; strip++) {
    Strips[strip].begin();
  }
  Serial.begin(115200); //for logging
  Serial.println("test test test");

/* WIFI, OTA,  AND MQTT STUFF */
  setup_wifi();
  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);

  ArduinoOTA.setPort(OTAport); //OTA SETUP
  ArduinoOTA.setHostname(LIGHTNAME);
  ArduinoOTA.setPassword((const char *)OTApassword);

  ArduinoOTA.onStart([]() {
    Serial.println("Starting");
  });
  ArduinoOTA.onEnd([]() {
    Serial.println("\nEnd");
  });
  ArduinoOTA.onProgress([](unsigned int progress, unsigned int total) {
    Serial.printf("Progress: %u%%\r", (progress / (total / 100)));
  });
  ArduinoOTA.onError([](ota_error_t error) {
    Serial.printf("Error[%u]: ", error);
    if (error == OTA_AUTH_ERROR) Serial.println("Auth Failed");
    else if (error == OTA_BEGIN_ERROR) Serial.println("Begin Failed");
    else if (error == OTA_CONNECT_ERROR) Serial.println("Connect Failed");
    else if (error == OTA_RECEIVE_ERROR) Serial.println("Receive Failed");
    else if (error == OTA_END_ERROR) Serial.println("End Failed");
  });
  ArduinoOTA.begin();

  Telnet.println("Ready");
  Telnet.print("IP Address: ");
  Telnet.println(WiFi.localIP());
  //randomize the array
  //for starry pattern
  /*
  for (int i = TOTAL_PIXELS-1; i >= 0; i-- ) {
    int j = random(0, i+1);
    PixelState tmp = pixels[i];
    pixels[i] = pixels[j];
    pixels[j] = tmp;
  }
*/

  //this could be cool someday
  //p = palette_gen();
}

/********************************** MAIN LOOP ****************************************/
void loop() {
  ESP.wdtFeed();
  /* KEEP WIFI, TELNET, AND MQTT RUNNING */
  if (!client.connected()) {
    reconnect();
  }
  if (WiFi.status() != WL_CONNECTED) {
    delay(1);
    Serial.print("WIFI Disconnected. Attempting reconnection.");
    setup_wifi();
    //return;
  }
  client.loop();
  ArduinoOTA.handle();
  handleTelnet();
  /*
  if (millis() - lastMem > 2000) {
    Telnet.printf("settings heap size: %u\n", ESP.getFreeHeap()); 
    lastMem = millis();
  }
  */
  /* DO LIGHTS */
  if (strcmp(stateOn, "false") == 0) {
    solidColor(0, 0, 0, 0);
  }
  else {
   if (pattern == 0) {
      ember();
    }
    if (pattern == 1) {
      glitter();
    }
    if (pattern == 2) {
      twinkleStars();
    }
    if (pattern == 3) {
      solidColor(solidColorRed, solidColorGreen, solidColorBlue, solidColorWhite);
    }
    if (pattern == 4) {
      glitter();
      ember();
    }  
  }
}


const uint8_t PROGMEM gamma8[] = {
    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
    1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,
    2,  2,  2,  2,  2,  2,  2,  3,  3,  3,  3,  3,  3,  3,  4,  4,
    4,  4,  4,  5,  5,  5,  5,  6,  6,  6,  6,  6,  7,  7,  7,  8,
    8,  8,  8,  9,  9,  9, 10, 10, 10, 11, 11, 12, 12, 12, 13, 13,
   14, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 20, 21,
   22, 22, 23, 23, 24, 25, 25, 26, 27, 27, 28, 29, 29, 30, 31, 32,
   32, 33, 34, 35, 35, 36, 37, 38, 39, 40, 40, 41, 42, 43, 44, 45,
   46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 61, 62,
   63, 64, 65, 67, 68, 69, 70, 72, 73, 74, 76, 77, 78, 80, 81, 82,
   84, 85, 87, 88, 90, 91, 93, 94, 96, 97, 99,101,102,104,105,107,
  109,111,112,114,116,118,119,121,123,125,127,129,131,132,134,136,
  138,140,142,144,147,149,151,153,155,157,159,162,164,166,168,171,
  173,175,178,180,182,185,187,190,192,195,197,200,202,205,207,210,
  213,215,218,221,223,226,229,232,235,237,240,243,246,249,252,255 };



//breathe using sin: https://codebender.cc/sketch:136737#NeoPixel%20Breathe.ino
//simple breathe https://gist.github.com/m-bodmer/928b9a926757f92db62f
