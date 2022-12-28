/*
* servo.c
*
* Created: 12/7/2022 9:45:21 AM
*  Author: alexe
*/
#include "servo.h"

volatile uint8_t update_pwm_ready = 0;
ISR(TIMER1_OVF_vect){
	update_pwm_ready = 1;
}


void update_pwm(uint16_t i){
	
	if(update_pwm_ready == 1){
		OCR1AH = (i & 0xFF00) >> 8;
		OCR1AL = (i & 0x00FF);
		update_pwm_ready = 0;
	}
	
}




void Servo_Init()
{
	
	//make pins as output
	DDRB = (1<<1) | (1<<2);
	//PORTB = 0xff;
	//Set fast mode (WGM) && compare output mode (COM1A1)
	TCCR1A = (1 << COM1A1) | (1 << WGM11);

	//Enable Interrupt for compare output A && overflow enable
	TIMSK1 = (1 << TOIE1) ;//| (1 << OCIE1A)  ;//| (1 << OCIE1B);
	//Start
	OCR1AH = (SEVRO_MIN & 0xFF00) >> 8;		OCR1AL = (SEVRO_MIN & 0x00FF);

	//Top value
	ICR1H = (PWM_TOP & 0xFF00) >> 8;
	ICR1L = (PWM_TOP & 0x00FF);

	//enable fast pwm
	//set prescaler 8
	TCCR1B = (1 << 4) | (1 << 3) | (1<<1);

};



void Servo_Write_Value(uint16_t deg)
{
	float duty = (float)deg / (float)180;
	float ocr_val = (((float)SEVRO_MAX-(float)SEVRO_MIN)*duty) + (float)SEVRO_MIN;
	update_pwm((uint16_t)ocr_val);
};




