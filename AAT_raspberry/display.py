import pifacecad

def write(data):
	cad = pifacecad.PiFaceCAD()
	cad.lcd.set_cursor(0, 0)
	cad.lcd.clear()
	cad.lcd.write(data)
	if len(data) >= 16:
		cad.lcd.set_cursor(0, 1)
		cad.lcd.write(data[16:])
	print data
