/*
 Stepper Motor Control - one revolution

 This program drives a unipolar or bipolar stepper motor.
 The motor is attached to digital pins 8 - 11 of the Arduino.

 The motor should revolve one revolution in one direction, then
 one revolution in the other direction.


 Created 11 Mar. 2007
 Modified 30 Nov. 2009
 by Tom Igoe

 */

#include <Stepper.h>


const int stepsPerRevolution = 200;  // change this to fit the number of steps per revolution for your motor
int switchgate=7;//Set the input pin
int ledPin=6;//display the state
int gate;//the state of switch
double weight;
double bowl=123;//weight of the bowl

const int FSR_PIN = A3; // Pin connected to FSR/resistor divider
 
// Measure the voltage at 5V and resistance of your 3.3k resistor, and enter
// their value's below:
const float VCC = 4.98; // Measured voltage of Ardunio 5V line
const float R_DIV = 3230.0; // Measured resistance of 3.3k resistor
int fsrADC;
float fsrV;
float fsrR;
float force;
float fsrG;
float weight_total;
int weight_food;
int weight_record;
float weight_i;
float counter=0;
int amount;

String readamount;
// initialize the stepper library on pins 8 through 11:
Stepper myStepper(stepsPerRevolution, 8, 9, 10, 11);

int sensorpin = 4;                 // analog pin used to connect the sharp sensor
int val = 0;                 // variable to store the values from sensor(initially zero)
double dist= 0;                 // variable to store the vales of distance
int lightup=2;

void setup() {
  // set the speed at 60 rpm:
  myStepper.setSpeed(60);
  // initialize the serial port:
  Serial.begin(9600);
  //set the input pin
  pinMode(FSR_PIN, INPUT);
  pinMode(ledPin, OUTPUT);
}

void clockwise(){
  // step one revolution  in one direction:
  //Serial.println("clockwise");
  myStepper.step(-stepsPerRevolution);
  //delay(500);
}
  
void counterclockwise(){
   // step one revolution in the other direction:
  //Serial.println("counterclockwise");
  myStepper.step(stepsPerRevolution);
  //delay(500);
}



void addfood(int counter){
  String read_amount;
  while(!Serial.available());
  read_amount = Serial.readString();
  int amount = 0;
  amount = read_amount.toInt();
  while(weight_record <amount){
 // gate=digitalRead(switchgate);
  //digitalWrite(ledPin,gate);
   clockwise();
   counterclockwise();
   delay(7000);
  weight_record=weight_r(counter);
  //weight_record=weight_record/5;
  //Serial.print("Weight of food: ");
  Serial.print(weight_record);
  Serial.println(" g");
  //Serial.println();
  }
}

float weight_f(){
   //Serial.print(scale.getGram(), 1);
  fsrADC = analogRead(FSR_PIN);
  // If the FSR has no pressure, the resistance will be
  // near infinite. So the voltage should be near 0.
  if (fsrADC != 0) // If the analog reading is non-zero
  {
    // Use ADC reading to calculate voltage:
     fsrV = fsrADC * VCC / 1023.0;
    // Use voltage and static resistor value to
    // calculate FSR resistance:
     fsrR = R_DIV * (VCC / fsrV - 1.0);

    // Guesstimate force based on slopes in figure 3 of FSR datasheet:float force;
    fsrG = 1.0 / fsrR; // Calculate conductance
    // Break parabolic curve down into two linear slopes:
    if (fsrR <= 600)
      force = (fsrG - 0.00075) / 0.00000032639;
    else
      force =  fsrG / 0.000000642857;  }
   else{
    // The pressure is zero, the weight us zero
    force=10;
   // delay(300);//
  }
  return force;
}

int weight_r(float counter){
   //Serial.print(scale.getGram(), 1);
  fsrADC = analogRead(FSR_PIN);
  // If the FSR has no pressure, the resistance will be near infinite. So the voltage should be near 0.
  if (fsrADC != 0) // If the analog reading is non-zero
  {
    // Use ADC reading to calculate voltage:
    float fsrV = fsrADC * VCC / 1023.0;
    // Use voltage and static resistor value to calculate FSR resistance:
    float fsrR = R_DIV * (VCC / fsrV - 1.0);

    // Guesstimate force based on slopes in figure 3 of FSR datasheet:float force;
    fsrG = 1.0 / fsrR; // Calculate conductance
    // Break parabolic curve down into two linear slopes:
    if (fsrR <= 600)
      force = (fsrG - 0.00075) / 0.00000032639;
    else
      force =  fsrG / 0.000000642857;
    weight_total=force/counter;
    weight_food=weight_total-123;
    //weight_food=-weight_food;
  }
   else{
    // The pressure is zero, the weight us zero
    weight_food=0;
   //delay(300);
  }
  int weight_int;
  weight_int = int(weight_food);
  return weight_int;
}

float calibrate(){
  weight_i=weight_f();
  if(weight_i!=0){
  counter=weight_i/123.00;
  }
  else{
    counter=1;
  }
  return counter;
}

void distance()
{
  val = analogRead(sensorpin);       // reads the value of the sharp sensor
  //Serial.println(val);            // prints the value of the sensor to the serial monitor
  //delay(100);                    // wait for this much time before printing next value
  dist= 4800/(val - 20);//calculate the distance (cm)
  //Serial.println(dist);
  if (dist>15 && dist <35){
   Serial.println(1);
   digitalWrite(lightup, HIGH);   // sets the LED on
   delay(3500);
   digitalWrite(lightup, LOW);   // sets the LED off
   delay(100);
  }
  else{
   Serial.println(0);
   digitalWrite(lightup, LOW);   // sets the LED off
  }
  delay(100);  
}
  
void loop() {
    if(Serial.available()>0)
    {
      switch(Serial.read())
      {
        case 'f':addfood(counter);break;
        case 'g':Serial.println("hello");break;
        case 'q':while(Serial.read()!='w'){clockwise();counterclockwise();}break;
        case 'c':counter=calibrate();break;
        case 'w':Serial.print(weight_r(counter));Serial.println("g");break;
        case 'd':distance();break;
      }
    }
    //weight_i=weight_f();
    //counter=calibrate();
    weight_record=weight_r(counter);
    //Serial.print("Weight  ");
    //Serial.print(force);
    //Serial.println(" g");
    //Serial.println();
    //Serial.print("counter ");
    //Serial.print(counter);
    //Serial.println(" g");
    //Serial.println();
    //Serial.print("Weight of food: ");
    //Serial.print(weight_record);
    //Serial.println(" g");
    //Serial.println();
    delay(300);

  //callibrate(error:10g)
   //addfood(40);
}

