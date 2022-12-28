#define  F_CPU 16000000
#include <xc.h>
#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>
#include "control/servo.h"
#include "communication/usart.h"
#include <stdlib.h>




#define SBUS_PACKAGE_SIZE 25

void decode_sbus(uint16_t* channels ,uint8_t *sbusData) {
	channels[0] = ((sbusData[1] | sbusData[2] << 8) & 0x07FF);
	channels[1] = ((sbusData[2] >> 3 | sbusData[3] << 5) & 0x07FF);
	channels[2] = ((sbusData[3] >> 6 | sbusData[4] << 2 | sbusData[5] << 10) & 0x07FF);
	channels[3] = ((sbusData[5] >> 1 | sbusData[6] << 7) & 0x07FF);
	channels[4] = ((sbusData[6] >> 4 | sbusData[7] << 4) & 0x07FF);
	channels[5] = ((sbusData[7] >> 7 | sbusData[8] << 1 | sbusData[9] << 9) & 0x07FF);
	channels[6] = ((sbusData[9] >> 2 | sbusData[10] << 6) & 0x07FF);
	channels[7] = ((sbusData[10] >> 5 | sbusData[11] << 3) & 0x07FF); // & the other 8 + 2 channels if you need them
	channels[8] = ((sbusData[12] | sbusData[13] << 8) & 0x07FF);
	channels[9] = ((sbusData[13] >> 3 | sbusData[14] << 5) & 0x07FF);
	channels[10] = ((sbusData[14] >> 6 | sbusData[15] << 2 | sbusData[16] << 10) & 0x07FF);
	channels[11] = ((sbusData[16] >> 1 | sbusData[17] << 7) & 0x07FF);
	channels[12] = ((sbusData[17] >> 4 | sbusData[18] << 4) & 0x07FF);
	channels[13] = ((sbusData[18] >> 7 | sbusData[19] << 1 | sbusData[20] << 9) & 0x07FF);
	channels[14] = ((sbusData[20] >> 2 | sbusData[21] << 6) & 0x07FF);
	channels[15] = ((sbusData[21] >> 5 | sbusData[22] << 3) & 0x07FF);
}





int main(void)
{
	
	Servo_Init();
	
	USART_Init(9);
	
	
	SREG |= (1<<7);
	
	//uint16_t channels[16];
	uint8_t dataSbusRaw[SBUS_PACKAGE_SIZE];
	while(1)
	{
		
		
		for (int j =0 ;j<SBUS_PACKAGE_SIZE;j++)
		{
			dataSbusRaw[j] = USART_Receive_Char();
		}
		
		for (int i = 0;i<=SBUS_PACKAGE_SIZE;i++)
		{
			USART_Transmit_Char( dataSbusRaw[i]);
		}
		
		
		
		
	}
	
	

}