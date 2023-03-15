
#include <Arduino.h>
#include "sbus.h"

#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif
/* SBUS object, writing SBUS */
bfs::SbusTx sbus_tx(&Serial1,37,2,true);
/* SBUS data */
bfs::SbusData data;


BluetoothSerial SerialBT;

void setup() {

  Serial.begin(9600);
  SerialBT.begin("ESP32test"); //Bluetooth device name
  // SerialBT.flush();
  Serial.println("The device started, now you can pair it with bluetooth!");
  sbus_tx.Begin();


  data.ch[0] = map(1000, 1000, 2000, 0, 2048) + 200;
  for(int i = 1 ;i< data.NUM_CH ;i++){
    data.ch[i] = map(1500, 1000, 2000, 0, 2048);
  }
  sbus_tx.data(data);
  sbus_tx.Write();


}

uint8_t buf[6*4];
uint32_t values[6];
void loop () {
  if (SerialBT.available()) {
    if(SerialBT.read() == 112){
      SerialBT.readBytes(buf,6*4);
    };

  values[0] = (uint32_t)((uint32_t)buf[0]<<24   |  (uint32_t)buf[1]<<16   |  (uint32_t)buf[2]<<8  |  (uint32_t)buf[3]<<0);
  values[1] = (uint32_t)((uint32_t)buf[4]<<24   |  (uint32_t)buf[5]<<16   |  (uint32_t)buf[6]<<8  |  (uint32_t)buf[7]<<0);
  values[2] = (uint32_t)((uint32_t)buf[8]<<24   |  (uint32_t)buf[9]<<16   |  (uint32_t)buf[10]<<8 |  (uint32_t)buf[11]<<0);
  values[3] = (uint32_t)((uint32_t)buf[12]<<24  |  (uint32_t)buf[13]<<16  |  (uint32_t)buf[14]<<8 |  (uint32_t)buf[15]<<0);
  values[4] = (uint32_t)((uint32_t)buf[16]<<24  |  (uint32_t)buf[17]<<16  |  (uint32_t)buf[18]<<8 |  (uint32_t)buf[19]<<0);
  values[5] = (uint32_t)((uint32_t)buf[20]<<24  |  (uint32_t)buf[21]<<16  |  (uint32_t)buf[22]<<8 |  (uint32_t)buf[23]<<0);

  data.ch[0] = (-map(values[0], 1000, 2000, 0, 2000)) + 200;
  data.ch[1] = map(values[1], 1000, 2000, 0, 2000);
  data.ch[2] = map(values[2], 1000, 2000, 0, 2000);
  data.ch[3] = map(values[3], 1000, 2000, 0, 2000);
  data.ch[4] = map(values[4], 1000, 2000, 0, 2000);
  data.ch[5] = map(values[5], 1000, 2000, 0, 2000);
  }

  

 

  sbus_tx.data(data);
  sbus_tx.Write();
}