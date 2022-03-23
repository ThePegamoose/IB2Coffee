from gpiozero import DigitalInputDevice
import requests

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

checkwaterlevel()

