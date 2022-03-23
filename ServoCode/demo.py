import time
import RPi.GPIO as GPIO
from gpiozero import AngularServo
from time import sleep
# to turn one servo

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
BUZZER = 26
buzzState = False
GPIO.setup(BUZZER, GPIO.OUT)

def turn_servo (gpio):
    servo = AngularServo(gpio, min_angle=-90, max_angle=90)
    servo.angle = -90
    sleep(0.5)
    servo.angle = 90
    sleep(0.5)



while True:
    turn_servo(27) #turn on machine
    turn_servo(22) #???
    turn_servo(17) #make coffee

    buzzState = not buzzState
    GPIO.output(BUZZER, buzzState)
    time.sleep(1)
    buzzState = False
    GPIO.output(BUZZER, buzzState)
