import pifacecad
import sys
import signal
from display import write
from sync import Sync 
from QRScanner import scan

def signal_handler(signal, frame):
	print "exiting"
	listener.deactivate()
	sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)

# Callback for when the scan button is pressed
def scan_and_sync(event):
	try:
		data = scan_code()
		if data:
			print data
			write("Successfully scanned")
			sync(data)
		else:
			write("Error scanning, please try again")
	except:
		print "Unexpected error:", sys.exc_info()[0]
		write("Error scanning, please try again")

# Callback to swich between modes when the change mode button is pressed
def switch_modes(event):
	global present_mode
	present_mode = not present_mode
	if present_mode:
		write("Present mode")
	else:
		write("Attendance mode")

# Calls the sync_data method and alerts user on success or failure
def sync(data):
	global present_mode
	write("Syncing...")
	if s.sync_data(data + ',' + ('P' if present_mode else 'A')):
		write("Successfully    synced data")
	else:
		write("Error syncing, please try again")

# Calls the scan method and returns the found data
def scan_code():
	write("Please display  code")
	return scan()

write("System starting up...")
present_mode = False
s = Sync()

cad = pifacecad.PiFaceCAD()
listener = pifacecad.SwitchEventListener(chip=cad)
listener.register(4, pifacecad.IODIR_FALLING_EDGE, scan_and_sync)
listener.register(0, pifacecad.IODIR_FALLING_EDGE, switch_modes)
listener.activate()
write("Startup complete...")
