/*
 * servo.h
 *
 * Created: 12/7/2022 9:45:33 AM
 *  Author: alexe
 */ 
#ifndef _SERVO_IMPL
#define  _SERVO_IMPL

#include <avr/io.h>
#include <avr/interrupt.h>

#define PWM_TOP (39999u)
#define SEVRO_MIN (1999u)
#define SEVRO_MAX  (4999u)



void Servo_Init();
void Servo_Write_Value();











#endif
