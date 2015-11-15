#include <SoftwareSerial.h>
 
#define BUTTON_STOP  0
#define BUTTON_LEFT  1
#define BUTTON_RIGHT 2
#define BUTTON_UP    3
#define BUTTON_DOWN  4

int rxPin = 7;
int txPin = 12;

SoftwareSerial debug(rxPin, txPin); 

int pwm_speed = 255;
int trig = 2;
int echo = 4;
int data;

int enA = 5;
int in1 = 11;
int in2 = 10;
int enB = 3;
int in3 = 9;
int in4 = 6;

void setup()
{
  Serial.begin(4800);
  debug.begin(9600);
  
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  
  pinMode(trig, OUTPUT);
  pinMode(echo,INPUT);
}

void loop()
{ 
  if (debug.available()) {
    data = debug.read();
    Serial.println(data);
    
    switch (data) {
      case BUTTON_LEFT:
        left(100);
        break;
      case BUTTON_RIGHT:
        right(100);
        break;
      case BUTTON_UP:
        forward(100);
        break;
      case BUTTON_DOWN:
        backward(100);
        break;
      case BUTTON_STOP:
        motors_stop(100);
        break;
    }
  }
}

void forward(int delay_time)
{
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);

  analogWrite(enA, pwm_speed);
  analogWrite(enB, pwm_speed);
  delay(delay_time);
}

void backward(int delay_time)
{
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);

  analogWrite(enA, pwm_speed);
  analogWrite(enB, pwm_speed);
  delay(delay_time);
}

void left(int delay_time)
{
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);

  analogWrite(enA, pwm_speed);
  analogWrite(enB, 0);
  delay(delay_time);
}

void right(int delay_time)
{
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);

  analogWrite(enA, 0);
  analogWrite(enB, pwm_speed);
  delay(delay_time);
}

void motors_stop(int delay_time)
{
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  
  digitalWrite(in3,LOW);
  digitalWrite(in4, LOW);

  analogWrite(enA, 0);
  analogWrite(enB, 0);
  delay(delay_time);
}
