filename = 'store'

# inserts val to end of store file
def insert(val):
  with open(filename, 'a') as f:
    f.write(val + '\n')

# gets all stored values
def get():
  with open(filename, 'r') as f:
    return map(str.strip, f.readlines())

# deletes first numLines number of values
def delete(numLines):
  lines = []
  with open(filename, 'r') as f:
    lines = f.readlines()

  with open(filename, 'w') as f:
    f.writelines(lines[numLines:])
