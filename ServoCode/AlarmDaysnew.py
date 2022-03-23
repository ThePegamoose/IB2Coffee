import datetime
import mysql.connector

activeAlarm = []
alarmArray = []
buzzer = 0
activateServo = 0
alarmActive = True
coffeeOn = True
minutesCoffee = 10  # time it takes to make coffee
status = ""


def sync_down():  # data is synced from db to list
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


while alarmActive:  # actual alarm
    sync_down()  # alarm is synced with db
    currentTime = date_to_string(datetime.datetime.now())  # current time, converted to weekday format
    coffeeTime = datetime.datetime.now() + datetime.timedelta(minutes=minutesCoffee)  # before alarm time to make coffee
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





