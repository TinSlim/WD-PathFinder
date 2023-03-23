import gzip
from itertools import islice

#TOTAL_LINES_FILE = 45
TOTAL_LINES_FILE = 702544222

# 219746500

line_counter = 0
line_added = 0

import csv
rel = {}
act_part = 0


def structToText(key, value):
	groups = list(map(lambda x: '.'.join( list(map(lambda y: str(y),x)) ),value))
	return [key] + groups


with gzip.open('cached_file.nt.gz','r') as f:
	for line in f:
		line_decode = line.decode()
		line_split = line_decode.split(" ")
		
		#cache_file.write(line)
		subj = int(line_split[0][33:-1].replace('Q',''))
		pred = int(line_split[1][38:-1].replace('P',''))
		obj = int(line_split[2][33:-1].replace('Q',''))
		
		if subj in rel:
			done = False
			for relation in rel[subj]:
				if relation[0] == pred:
					relation.append(obj)
					done = True
					break
			if not done:
				rel[subj].append([pred,obj])

		else:
			rel[subj] = [[pred,obj]]

		if obj in rel:
			done = False
			for relation in rel[obj]:
				if relation[0] == pred * -1:
					relation.append(subj)
					done = True
					break
			if not done:
				rel[obj].append([pred * -1,subj])

		else:
			rel[obj] = [[pred * -1,subj]]
		
		line_added += 1
		line_counter += 1

		if line_added > 100000000:
			line_added = 0
			file_open = open(f"parts2/part_{act_part}.csv",'w',newline='')
			file_writer = writer = csv.writer(file_open,delimiter=' ', quotechar='|')

			for data in sorted(rel.keys()):
				#result = [data,len(rel[data])] + sum(rel[data],[])
				result = structToText(data,rel[data])
				file_writer.writerow(result)
			
			file_open.close()
			rel = {}
			act_part += 1

		print(f"{line_counter} / {TOTAL_LINES_FILE} | {(line_counter * 100) // TOTAL_LINES_FILE}%",end='\r')

if line_added > 0:
			line_added = 0
			file_open = open(f"parts2/part_{act_part}.csv",'w',newline='')
			file_writer = writer = csv.writer(file_open,delimiter=' ', quotechar='|')
			
			for data in sorted(rel.keys()):
				#result = [data,len(rel[data])] + sum(rel[data],[])
				result = structToText(data,rel[data])
				file_writer.writerow(result)
			
			file_open.close()
			rel = {}
			act_part += 1

print("\n")

print(f"Total {line_counter} lineas")
print(f"Total {line_added} lineas agregadas")

