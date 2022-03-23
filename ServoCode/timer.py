import time
import RPi.GPIO as GPIO
from gpiozero import AngularServo
import requests
from time import sleep

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
BUZZER= 26
buzzState = False
GPIO.setup(BUZZER, GPIO.OUT)

timerList = []
# totalList = []
timeToMakeCoffee = 55  #all times in seconds


def get_time():
    timerList.clear()
    check = requests.get("https://studev.groept.be/api/a21ib2a04/timerAll")
    y = check.json()
    for value in y:
        x = int(value["timerValue"])*60
        z = int(value["Active"])
        timerList.append([z,x])
        # totalList.append(x)
    return timerList

def turn_servo (gpio):
    servo = AngularServo(gpio, min_angle=-90, max_angle=90)
    servo.angle = -90
    sleep(0.5)
    servo.angle = 90
    sleep(0.5)


while True:
    get_time()
    for timer in timerList:
        if timer[0] == 1:
            time.sleep(timer[1]-timeToMakeCoffee)
            turn_servo(27) #turn on machine
            turn_servo(22) #???
            turn_servo(17) #make coffee
            time.sleep(2)#timeToMakeCoffee)
            while True:
                buzzState = not buzzState
                GPIO.output(BUZZER, buzzState)
                time.sleep(1)





#def new_timer (minutes,seconds): #set new timer
    #    total = minutes * 60 + seconds
    #new_time = [0,total]
    #if total > timeToMakeCoffee:
    #   if total not in totalList:
    #       timerList.append(new_time)
    #   else:
    #       for timerAlarm in timerList:
    #           if timerAlarm[1] == total:
    #               timerAlarm[0] = 1
    #else:
    #   print ("You cannot make coffee that fast!")

    #def set_timer(index): #set timer
    #for timerAlarm in timerList:
    #   timerAlarm[0] = 0
    #timerList[index][0] = 1

    #def delete_timer (index):
    #del timerList[index]






