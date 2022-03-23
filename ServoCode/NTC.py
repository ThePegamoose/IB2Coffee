from gpiozero import MCP3008
import mysql.connector
from numpy import log as ln
A = 2.132014369*(10**-3)   # constants for equation
B = 0.5655571146*(10**-4)
C = 12.04690807*(10**-7)
tmp = MCP3008(channel=0, device=0)
R1 = 10000
V = 3.3*tmp
R = (V*R1)/(3.3-V)
print(R)
Tk = 1/(A+B*ln(R)+C*((ln(R)) ** 3))
Tc = Tk-273
round_temperature = round(Tc)
print(round_temperature)

mydb = mysql.connector.connect(
    host="mysql.studev.groept.be",
    user="a21ib2a04",
    password="secret"
)


def getTemp():
    cursor = mydb.cursor()
    command = "REPLACE INTO `a21ib2a04`.`Settings` (`id`, `Setting`, `Value`) VALUES (%s, %s, %s);"
    value = (2, "temperature", round_temperature)
    cursor.execute(command, value)
    mydb.commit()



