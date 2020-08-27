
## Photos
### Lamp + App

![lamp_app](https://github.com/smashinashwin/home-led-fun/blob/master/Photos/lamp_app_demo.gif)


### Wiring:
![Wiring](https://github.com/smashinashwin/home-led-fun/blob/master/Photos/Wiring.jpg)

## Summary
This is a wifi-controlled smart lamp. It uses SK6812 RGBW Leds, powered by a 5V 20A power supply, controlled by an ESP8266 Microcontroller, listening to a Mosquitto broker on a Raspberry Pi and an Android app to provide light, look awesome, and enable easy control.

## Quick Architecture Diagram
![Arch](https://github.com/smashinashwin/home-led-fun/blob/master/Photos/artchitecture.jpg)

## Getting started (software)
1. Install the Arduino IDE and Android Studio.
2. Install the ESP8266 drivers on the Arduino IDE. See this [link](https://dzone.com/articles/programming-the-esp8266-with-the-arduino-ide-in-3) for help.
3. Clone the repo.
4. Set up a Mosquitto MQTT broker on whatever you want to use as your server. Check out this [link](https://www.pakstech.com/blog/raspberry-pi-mosquitto-getting-started/) to get started. Currently, the lamp listens for the lamp/pattern and the lamp/state topics.
5. Rename 'controller_code/corner-lamp/sample_config.h' to 'config.h', and android-projects/Lights/app/src/main/res/raw/_sample_mqttconfig.txt to '_mqttconfig.txt.' Change the variables in these file to match your wifi settings, Arduino settings, MQTT settings, and Android settings.
6. You may need to install relevant packages in your Arduino IDE (Adafruit_Neopixel, PubSubClient, ArduinoJson, etc).
7. Connect your ESP8266 to a power supply and some LEDs, and change #define PIN_0 D2 & #define PIN_1 D4 in corner_lamp.ino to the pins you used. (Wiring LEDs is a bit out of scope for this readme, but some of the links below should help). You may need to change other variables in the 'NEOPIXEL SETUP' section depending on your setup.
8. Plug your computer into the ESP8266, and upload your .ino file.
9. Run the app from Android studio in an emulator or on your phone to interact with the LEDs. You can also send mqtt messages via command line.


## Hardware

The 20V 5A power supply is hooked up to terminals on the PCB board. Power runs to the ESP8266 chip, Logic Level Shifter (converting data signals from 3.3V to 5V), and the LEDs. The LED data wires are connected to pins 2 and 4 on the ESP8266. See this handy [link](https://tttapa.github.io/ESP8266/Chap04%20-%20Microcontroller.html#:~:text=The%20ESP8266%20has%2017%20GPIO,you%20might%20crash%20your%20program.) for more information on the ESP866 pinout.

- SK6812 RGBnW LEDs (4m) [amazon](https://www.amazon.com/BTF-LIGHTING-Individually-Addressable-Flexible-Waterproof/dp/B01MYV70NJ/ref=sxts_sxwds-bia-wc-p13n1_0?cv_ct_cx=sk6812&dchild=1&keywords=sk6812&pd_rd_i=B01MYV70NJ&pd_rd_r=2c7bbf26-571c-4531-883f-67f81f890309&pd_rd_w=qm9wW&pd_rd_wg=sqsCy&pf_rd_p=13bf9bc7-d68d-44c3-9d2e-647020f56802&pf_rd_r=G11ANDVGSYG5QEVVEV9D&psc=1&qid=1596243243&sr=1-1-791c2399-d602-4248-afbb-8a79de2d236f)
- ESP8266 Node MCU [amazon](https://www.amazon.com/HiLetgo-Internet-Development-Wireless-Micropython/dp/B081CSJV2V/ref=sxts_sxwds-bia-wc-p13n1_0?cv_ct_cx=esp8266&dchild=1&keywords=esp8266&pd_rd_i=B081CSJV2V&pd_rd_r=df0316f4-d64e-410d-bd11-792fb500cb8b&pd_rd_w=vRBqz&pd_rd_wg=ZbNRi&pf_rd_p=13bf9bc7-d68d-44c3-9d2e-647020f56802&pf_rd_r=HN8HDFMV68VVR537EFE5&psc=1&qid=1596243380&sr=1-1-791c2399-d602-4248-afbb-8a79de2d236f)
- 5V 20A Power Supply [amazon](https://www.amazon.com/ALITOVE-Transformer-Adapter-Converter-Charger/dp/B06XK2DDW4/ref=sr_1_3?crid=GEL8QTRR5I8O&dchild=1&keywords=5v+20a+power+supply&qid=1596243430&s=electronics&sprefix=5v+20a%2Celectronics%2C228&sr=1-3)
- Anderson powerpole connectors [amazon](https://www.amazon.com/Anderson-Powerpole-Connectors-20-Pair/dp/B00GPRIC8Y/ref=sxts_sxwds-bia-wc-p13n1_0?crid=7SGWY588T1V6&cv_ct_cx=anderson+powerpole+connectors&dchild=1&keywords=anderson+powerpole+connectors&pd_rd_i=B00GPRIC8Y&pd_rd_r=8977b5bc-8ccc-490e-bd36-4c696b1bd3ae&pd_rd_w=B320t&pd_rd_wg=FJsFV&pf_rd_p=13bf9bc7-d68d-44c3-9d2e-647020f56802&pf_rd_r=KQSFMWYAZQPYKFXX3707&psc=1&qid=1596243903&sprefix=anderson+power%2Caps%2C264&sr=1-1-791c2399-d602-4248-afbb-8a79de2d236f)
- LED connector cables [amazon](https://www.amazon.com/gp/product/B082W6F4MQ/ref=ppx_yo_dt_b_search_asin_title?ie=UTF8&psc=1)
- Raspberry Pi
- PCB board + jumper wires/cables + terminals. 16 gauge wires are used to run power from the supply to the PCB board. 20 gauge wires run power from the PCB board to the LEDs. 
- Logic level shifter [amazon](https://www.amazon.com/Adafruit-74LVC245-Breadboard-Friendly-Shifter/dp/B00SK8OC0S/ref=sr_1_2?dchild=1&keywords=adafruit+logic+level+shifter&qid=1596243973&s=electronics&sr=1-2)

## Software
- Arduino IDE / C++ for the controller code
    - PubSubClient
    - ArduinoJson
    - Adafruit NeoPixel
  
- Raspberry Pi 
    - [Mosquitto MQTT broker](https://mosquitto.org/)
 
 - Android App
    - Kotlin
    - XML
## Future Work
**Scheduling and saving**
- Build a backend on the raspberry pi that can database patterns / colors / palettes, and run a schedule for the lights. Build a front-end on the app to add schedules / settings / colors.
- Add more accent lighting and connect them to this system (enable the Window and Door checkboxes)
- Convert constants to variable names in all Kotlin files.

**Refactor patterns**
- These are currently clunky, and all held on the microcontroller. One thing to try would be to make the microcontroller solely responsible for receiving information, and putting all pattern logic onto the raspberry pi. 

## Problems and gotchas encountered:
- Melting stuff. Make sure to use wires with enough [ampacity](https://xtronics.com/wiki/Wire-Gauge_Ampacity.html) for your project.
- RGBW isn't supported by FastLED
- These LEDs don't play well with multi-core microcontrollers (the ESP32 for example). If you want to use the ESP32 and/or FastLED, check out APA* lights [amazon](https://www.amazon.com/gp/product/B078JVS2VG/ref=ppx_yo_dt_b_search_asin_title?ie=UTF8&psc=1)
- The ESP8266 can get overwhelmed if continuously parsing JSON or sending instructions to the LEDs. Use delays or timers. The android app queues messages and sends them on a 50ms delay. The watchdog timer is also finnicky, so setting the clock to something custom helps with stability. (see these lines in corner_lamp.ino:)   

`ESP.wdtDisable();`
`ESP.wdtEnable(WDTO_8S); //fixing watchdog resets`
- Hand-sawing is both hard and dangerous.
- Soldering to PCB boards takes a lot of care. Luckily, dev boards are cheap.


## Helpful Links and Resources
- [This project is most similar to the work in this guide; super helpful!](https://www.youtube.com/watch?v=9KI36GTgwuQ)
Interesting other light projects:
- https://learn.adafruit.com/ever-burning-flame-painting?view=all
- https://github.com/daterdots/LEDs/blob/master/TwinkleSparkle2014/TwinkleSparkle2014.ino
- https://www.tweaking4all.com/hardware/arduino/adruino-led-strip-effects/#LEDStripEffectCylon
- https://learn.adafruit.com/1500-neopixel-led-curtain-with-raspberry-pi-fadecandy/overview
- https://www.youtube.com/watch?v=yninmUrl4C0
- https://github.com/FastLED/FastLED/wiki/Gradient-color-palettes
- https://github.com/technobly/NeoPixel-KnightRider/blob/master/NeoPixel_KnightRider.ino
- https://github.com/Makuna/NeoPixelBus/tree/master/src 
- https://github.com/timpear/NeoCandle/blob/master/NeoCandle8/NeoCandle8.ino
- https://github.com/justcallmekoko/Arduino-FastLED-Music-Visualizer
- https://github.com/kitesurfer1404/WS2812FX/blob/master/src/WS2812FX.cpp
- https://github.com/mariusmotea/diyHue	
- https://github.com/Shinyhero36/NeoPixel-Fire2012/blob/master/FireEffect/FireEffect.ino
- https://forum.arduino.cc/index.php?topic=534555.0
- https://github.com/dlas/burningman
- https://github.com/Vortex375/mood-light/tree/fd6b7352b29ec6ae6bea15a0f15aa2650d621c1c



