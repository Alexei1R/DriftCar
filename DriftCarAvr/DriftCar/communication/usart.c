/*
 * usart.c
 *
 * Created: 12/9/2022 11:53:14 AM
 *  Author: alexe
 */ 
#include "usart.h"




//ISR(USART_RX_vect){
//RXreceive_complete = 1;
//}
//
//ISR(USART_TX_vect){
//TXtransmit_complete = 1;
//}
//
//ISR(USART_UDRE_vect){
//USARTDataRegisterEmpty = 1;
//}





void USART_Init(uint16_t speed ){
	//set baud rate 9600
	UBRR0H = (unsigned char)(speed>>8);
	UBRR0L = (unsigned char)(speed);
	//enable uart
	UCSR0B |= (1<<TXEN0) | (1<<RXEN0);
	//Set Frame Format
	UCSR0C = (1<<USBS0)|(3<<UCSZ00);
	UCSR0C = (1<<UPM01);
	
	////Enable iterrupts
	////RX complete // DataRegisterEmpty
	//UCSR0B = (1<<TXCIE0) | (1<<RXCIE0) | (1<<UDRIE0);
	
}


void USART_Transmit_Char(uint8_t data){
	//if ()
	//{
	//UDR0 = (uint8_t)data;
	//USARTDataRegisterEmpty = 0;
	//}
	
	while(!( UCSR0A & (1<<UDRE0)));
	UDR0 = data;
}

void USART_Transmit_String(char *str)
{
	unsigned char j=0;
	
	while (str[j]!=0)		/* Send string till null */
	{
		USART_Transmit_Char(str[j]);
		j++;
	}
}

void USART_Transmit_HEX_8(uint8_t val)
{
	// extract upper and lower nibbles from input value
	uint8_t upperNibble = (val & 0xF0) >> 4;
	uint8_t lowerNibble = val & 0x0F;

	// convert nibble to its ASCII hex equivalent
	upperNibble += upperNibble > 9 ? 'A' - 10 : '0';
	lowerNibble += lowerNibble > 9 ? 'A' - 10 : '0';

	// print the characters
	USART_Transmit_Char(upperNibble);
	USART_Transmit_Char(lowerNibble);
}
void USART_Transmit_HEX_16(uint16_t val)
{
	// transmit upper 8 bits
	USART_Transmit_HEX_8((uint8_t)(val >> 8));
	// transmit lower 8 bits
	USART_Transmit_HEX_8((uint8_t)(val & 0x00FF));
}


uint8_t  USART_Receive_Char(){
	
	while (!(UCSR0A & (1<<RXC0)));
	return (uint8_t)UDR0;
}


void USART_Receive_String(char* buf, uint8_t n)
{
	uint8_t bufIdx = 0;
	char c;

	// while received character is not carriage return
	// and end of buffer has not been reached
	do
	{
		// receive character
		c = USART_Receive_Char();

		// store character in buffer
		buf[bufIdx++] = c;
	}
	while((bufIdx < n) && (c != '\r'));

	// ensure buffer is null terminated
	buf[bufIdx] = 0;
}