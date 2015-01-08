import shutil as sh
import os

path = os.getcwd()
out_path = os.getcwd()

dirs = [os.path.join(path, _file) for _file in os.listdir(path) if os.path.isdir(os.path.join(path, _file))]

mp3s = list()

for d in dirs:
	mp3s.extend([os.path.join(d, _file) for _file in os.listdir(d) if _file.endswith('.mp3')])

for i in range(0,len(mp3s)):
	new_file = os.path.join(out_path, str(i) + '.mp3')
	sh.copyfile(mp3s[i], new_file)

