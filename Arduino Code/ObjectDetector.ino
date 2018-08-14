
int orangeLED = 3; // sensor blocked
int whiteLED = 5; // on pin
int COUNT =0;
int detectPin = 2;
boolean hit = false;

void setup() {
 
  pinMode(detectPin,INPUT);
  pinMode(orangeLED,OUTPUT);
  pinMode(whiteLED,OUTPUT);
 Serial.begin(9600);

attachInterrupt(digitalPinToInterrupt(detectPin), update, CHANGE);
analogWrite(whiteLED, 10);
 
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
 Serial.println(COUNT);
  delay(1000);
 
  
}
