import datetime
import mysql.connector
from gpiozero import DigitalInputDevice
import time
import RPi.GPIO as GPIO
from gpiozero import AngularServo
import requests
from time import sleep
from gpiozero import MCP3008
from numpy import log as ln
import json
import requests


turnOnTime = 0  # time the coffee machine takes to heat
makeCoffeeTime = 0  # time the heated machine takes to make coffee
timeToMakeCoffee = 0  # total time it takes to make coffee
cupSize = 0  # 0 if small , 1 if big
timesBeep = 5
mode = 0


activeAlarm = []
alarmArray = []
buzzer = 0
alarmActive = True
coffeeOn = True

status = ""
mydb = mysql.connector.connect(
    host="mysql.studev.groept.be",
    user="a21ib2a04",
    password="secret"
)
cursor = mydb.cursor()

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
BUZZER = 26
buzzState = False
GPIO.setup(BUZZER, GPIO.OUT)

timerList = []

def updateStats():
    d = datetime.datetime.now();
    date = ""
    if d.day < 10:
        date += "0" + str(d.day)
    else:
        date += str(d.day)
    if d.month < 10:
        date += "0" + str(d.month)
    else:
        date += str(d.month)
    date += str(d.year)
    s = "https://studev.groept.be/api/a21ib2a04/getCount/"
    s += date
    ret = requests.get(s)
    j = ret.json()
    x = 0
    for value in j:
        x = value["count"]
    if x == 0:
        s = "https://studev.groept.be/api/a21ib2a04/addstat/" + date
        requests.get(s)
    else:
        s = "https://studev.groept.be/api/a21ib2a04/inccount/" + date
        requests.get(s)


def getData():
    global turnOnTime
    global makeCoffeeTime
    global timeToMakeCoffee
    global cupSize
    global mode
    request = "https://studev.groept.be/api/a21ib2a04/getSetting/"
    request += "heatingTime"
    ret = requests.get(request)
    y = ret.json()
    for value in y:
        turnOnTime = int(value["Value"])
    request = "https://studev.groept.be/api/a21ib2a04/getSetting/"
    request += "coffeeTime"
    ret = requests.get(request)
    y = ret.json()
    for value in y:
        makeCoffeeTime = int(value["Value"])
    timeToMakeCoffee = makeCoffeeTime + turnOnTime
    request = "https://studev.groept.be/api/a21ib2a04/getSetting/"
    request += "cupSize"
    ret = requests.get(request)
    y = ret.json()
    for value in y:
        cupSize = int(value["Value"])
    request = "https://studev.groept.be/api/a21ib2a04/getSetting/"
    request += "mode"
    ret = requests.get(request)
    y = ret.json()
    for value in y:
        mode = int(value["Value"])


def checkwaterlevel():
    pin = DigitalInputDevice(19)
    val = pin.value
    s = "https://studev.groept.be/api/a21ib2a04/UpdateSetting/"
    if val == 1:
        s += "1"
    elif val == 0:
        s += "0"
    s += "/waterStatus"
    requests.get(s)


def turn_servo(gpio):
    servo = AngularServo(gpio, min_angle=-90, max_angle=90)
    servo.angle = -90
    sleep(0.5)
    servo.angle = 90
    sleep(0.5)


def makecoffee():
    global buzzState
    turn_servo(17)  # 17 27 22
    sleep(turnOnTime)
    if cupSize == 0:
        turn_servo(27)
    elif cupSize == 1:
        turn_servo(22)
    sleep(makeCoffeeTime)
    for i in range(timesBeep):
        buzzState = not buzzState
        GPIO.output(BUZZER, buzzState)
        time.sleep(1)
    buzzState = False
    GPIO.output(BUZZER, buzzState)

def getTemp():
    A = 2.132014369*(10**-3)   # constants for equation
    B = 0.5655571146*(10**-4)
    C = 12.04690807*(10**-7)
    tmp = MCP3008(channel=0, device=0)
    R1 = 10000
    V = 3.3*tmp
    R = (V*R1)/(3.3-V)
    #print(R)
    Tk = 1/(A+B*ln(R)+C*((ln(R)) ** 3))
    Tc = Tk-273
    round_temperature = round(Tc)
    #print(round_temperature)
    cursor = mydb.cursor()
    command = "REPLACE INTO `a21ib2a04`.`Settings` (`id`, `Setting`, `Value`) VALUES (%s, %s, %s);"
    value = (2, "temperature", round_temperature)
    cursor.execute(command, value)
    mydb.commit()



def syncData():  # data is synced from db to list
    mydb = mysql.connector.connect(
        host="mysql.studev.groept.be",
        user="a21ib2a04",
        password="secret"
    )
    cursor = mydb.cursor()
    startcommand = "SELECT time FROM `a21ib2a04`.`alarm`"
    cursor.execute(startcommand)
    result = cursor.fetchall()
    alarmArray = [i[0] for i in result]
    startcommand = "SELECT active FROM `a21ib2a04`.`alarm`"
    cursor.execute(startcommand)
    result = cursor.fetchall()
    activelist = [i[0] for i in result]
    x = 0
    while x < len(alarmArray):
        if activelist[x] == 1:
            if alarmArray[x] not in activeAlarm:
                activeAlarm.append(alarmArray[x])
        else:
            value = alarmArray[x]
            if value in activeAlarm:
                activeAlarm.remove(value)

        x += 1


def sync_up():  # data is synced with from list to db
    mydb = mysql.connector.connect(
        host="mysql.studev.groept.be",
        user="a21ib2a04",
        password="secret"
    )
    cursor = mydb.cursor()
    active = False
    i = 0
    while i < len(alarmArray):
        if alarmArray[i] in activeAlarm:
            active = True
        time = alarmArray[i]
        command = "REPLACE INTO `a21ib2a04`.`alarm` (`index`, `time`, `active`) VALUES (%s, %s, %s);"
        value = (i, time, active)
        cursor.execute(command, value)
        i += 1
        mydb.commit()


def date_to_string(date):  # converts date object to String
    alarm = date.strftime("%A%H%M")
    return alarm


def toggleAlarm():  # toggles the alarm on/off
    global alarmActive
    alarmActive = not alarmActive


def toggleCoffee():  # toggles coffee making on/off
    global coffeeOn
    coffeeOn = not coffeeOn


def get_status():
    return status

def checkalarm():
    syncData()  # alarm is synced with db
    currentTime = date_to_string(datetime.datetime.now())  # current time, converted to weekday format
    coffeeTime = datetime.datetime.now() + datetime.timedelta(minutes=timeToMakeCoffee)  # before alarm time to make coffee
    coffeeTime2 = date_to_string(coffeeTime)  # coffee time, converted to weekday format

    if currentTime in activeAlarm:  # checks regular alarm
        buzzer = 1
        activateServo = 0
        status = "coffee finished, alarm bzz bzz"
        print(status)

    if coffeeTime2 in activeAlarm and activateServo == 0 and coffeeOn:  # checks coffee alarm
        activateServo = 1
        status = "started making coffee"
        print(status)


def checktimer():
    get_time()
    for timer in timerList:
        if timer[0] == 1:
            time.sleep(timer[1]-timeToMakeCoffee)
            makecoffee()

def get_time():
    timerList.clear()
    check = requests.get("https://studev.groept.be/api/a21ib2a04/timerAll")
    y = check.json()
    for value in y:
        x = int(value["timerValue"]) * 60
        z = int(value["Active"])
        timerList.append([z, x])
        # totalList.append(x)
    return timerList


while True:
    checkwaterlevel()
    getData()
    syncData()
    if mode == 0:
        checktimer()
    elif mode == 1:
        checkalarm()
    updateStats()
    getTemp()
