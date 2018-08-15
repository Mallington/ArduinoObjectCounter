
int orangeLED = 3; // sensor blocked
int whiteLED = 5; // on pin
int COUNT =0;
int detectPin = 2;
boolean hit = false;
boolean RUN = false;
boolean once = true;
void setup() {
  Serial.begin(9600);
  Serial.print("HEADER");
  Serial.println("Counter V1.0");
  pinMode(detectPin,INPUT);
  pinMode(orangeLED,OUTPUT);
  pinMode(whiteLED,OUTPUT);
 

attachInterrupt(digitalPinToInterrupt(detectPin), update, CHANGE);
analogWrite(whiteLED, 10);
 RUN = true;
}

void update(){

    int r = digitalRead(2);
  if(hit ==false && r ==0){
    hit = true;
    COUNT++;
    digitalWrite(orangeLED, HIGH);
  }
  if(r==1){
    hit = false;
     digitalWrite(orangeLED,LOW);
  }
  
}


void loop() {
 
  if(RUN){
 Serial.write(COUNT);
  delay(100);
  
  }
  
}
