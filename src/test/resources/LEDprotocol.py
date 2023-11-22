import sys
import bluetooth

mac = sys.argv[1]
cmd = sys.argv[2]
port = 1
sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
sock.connect(mac,port)
sock.send(cmd.encode())
