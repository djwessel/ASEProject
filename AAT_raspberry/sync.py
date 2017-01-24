import urllib2
import storage

# Checks to see if a connection can be made to GAE
def internet_on():
  try:
    urllib2.urlopen('http://216.58.192.142', timeout=1)
    return True
  except urllib2.URLError as err: 
    return False

# Syncs data, if connected to internet. Otherwise it will store data locally
def sync_data(data):
  if internet_on():
    sync(data)
  else:
    storage.insert(data)

# Calls REST API to update student attendance info
# TODO: finish the implementation
def sync(data):
  print "syncing: ", data

