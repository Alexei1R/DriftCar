/*
 * usart.h
 *
 * Created: 12/9/2022 11:53:02 AM
 *  Author: alexe
 */ 
#ifndef _USART_IMPL
#define  _USART_IMPL

#include <avr/io.h>
#include <avr/interrupt.h>


//static volatile uint8_t TXtransmit_complete = 0;
//static volatile uint8_t RXreceive_complete = 0;
//static volatile uint8_t USARTDataRegisterEmpty =


void USART_Init(uint16_t speed);
void USART_Transmit_Char(uint8_t data);
uint8_t  USART_Receive_Char();
void USART_Transmit_String(char *str);
void USART_Transmit_HEX_8(uint8_t val);
void USART_Transmit_HEX_16(uint16_t val);
void USART_Receive_String(char* buf, uint8_t n);


#endif