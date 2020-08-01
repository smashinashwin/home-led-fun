# home-led-fun
### A repo for wifi-controlled LED projects.

## Summary
The lamp consists of 4 meters of SK6812 RGBW LEDs at 60 LEDs / meter. They are controlled by and ESP8266, and powered by a 5V 20A power supply. 
The ESP8266 microcontroller is subscribed to an MQTT broker hosted on a Raspberry Pi.
The andriod app sends messages to the same MQTT broker, which are picked up by the ESP. These messages turn the lights on/off, and change patterns and colors.

Because these LEDs are RGBW, the Adafruit Neopixel library was used instead of FastLED. This added some necessary helper functions and made the code a bit clunkier, but having a white LED is pretty sweet =).

Currently this only works if everything is on the same network.

## Hardware
- SK6812 RGBnW LEDs (4m) [amazon](https://www.amazon.com/BTF-LIGHTING-Individually-Addressable-Flexible-Waterproof/dp/B01MYV70NJ/ref=sxts_sxwds-bia-wc-p13n1_0?cv_ct_cx=sk6812&dchild=1&keywords=sk6812&pd_rd_i=B01MYV70NJ&pd_rd_r=2c7bbf26-571c-4531-883f-67f81f890309&pd_rd_w=qm9wW&pd_rd_wg=sqsCy&pf_rd_p=13bf9bc7-d68d-44c3-9d2e-647020f56802&pf_rd_r=G11ANDVGSYG5QEVVEV9D&psc=1&qid=1596243243&sr=1-1-791c2399-d602-4248-afbb-8a79de2d236f)
- ESP8266 Node MCU [amazon](https://www.amazon.com/HiLetgo-Internet-Development-Wireless-Micropython/dp/B081CSJV2V/ref=sxts_sxwds-bia-wc-p13n1_0?cv_ct_cx=esp8266&dchild=1&keywords=esp8266&pd_rd_i=B081CSJV2V&pd_rd_r=df0316f4-d64e-410d-bd11-792fb500cb8b&pd_rd_w=vRBqz&pd_rd_wg=ZbNRi&pf_rd_p=13bf9bc7-d68d-44c3-9d2e-647020f56802&pf_rd_r=HN8HDFMV68VVR537EFE5&psc=1&qid=1596243380&sr=1-1-791c2399-d602-4248-afbb-8a79de2d236f)
- 5V 20A Power Supply [amazon](https://www.amazon.com/ALITOVE-Transformer-Adapter-Converter-Charger/dp/B06XK2DDW4/ref=sr_1_3?crid=GEL8QTRR5I8O&dchild=1&keywords=5v+20a+power+supply&qid=1596243430&s=electronics&sprefix=5v+20a%2Celectronics%2C228&sr=1-3)
- Anderson powerpole connectors [amazon](https://www.amazon.com/Anderson-Powerpole-Connectors-20-Pair/dp/B00GPRIC8Y/ref=sxts_sxwds-bia-wc-p13n1_0?crid=7SGWY588T1V6&cv_ct_cx=anderson+powerpole+connectors&dchild=1&keywords=anderson+powerpole+connectors&pd_rd_i=B00GPRIC8Y&pd_rd_r=8977b5bc-8ccc-490e-bd36-4c696b1bd3ae&pd_rd_w=B320t&pd_rd_wg=FJsFV&pf_rd_p=13bf9bc7-d68d-44c3-9d2e-647020f56802&pf_rd_r=KQSFMWYAZQPYKFXX3707&psc=1&qid=1596243903&sprefix=anderson+power%2Caps%2C264&sr=1-1-791c2399-d602-4248-afbb-8a79de2d236f)
- Raspberry Pi
- PCB board + jumper wires/cables + terminals
- Logic level shifter [amazon](https://www.amazon.com/Adafruit-74LVC245-Breadboard-Friendly-Shifter/dp/B00SK8OC0S/ref=sr_1_2?dchild=1&keywords=adafruit+logic+level+shifter&qid=1596243973&s=electronics&sr=1-2)

## Software
- Arduino / C++ for the controller code
  - PubSubClient
  - ArduinoJson
  - Adafruit NeoPixel
  
- Raspberry Pi 
  - [Mosquitto MQTT server](https://mosquitto.org/)
 
 - Android App
  - Kotlin
  - XML
  
## Futu
