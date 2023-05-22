# --------------------------
# @author Cristóbal Torres G.
# @github Tinslim
# --------------------------

import gzip

INPUT = "latest-truthy.nt.gz"
SUBSET = 100000000
OUTPUT = f"subsets/subset{SUBSET}.nt.gz"

# Abrir gzip
line_counter = 0
line_added = 0
max_id = 0

cache_file = gzip.open(OUTPUT,"wb")
with gzip.open(INPUT,'r') as f:
	for line in f:
		line_decode = line.decode()
		line_split = line_decode.split(" ")
		line_counter += 1
		if ('<http://www.wikidata.org/entity/Q' != line_split[0][:33] or
			'<http://www.wikidata.org/prop/direct/P' != line_split[1][:38] or
			'<http://www.wikidata.org/entity/Q' != line_split[2][:33]):
			continue

		# Obtiene valores de la tripleta
		subj = int(line_split[0][33:-1])
		pred = int(line_split[1][38:-1])
		obj = int(line_split[2][33:-1])

		if (subj <= SUBSET and obj <= SUBSET):
			if (subj > max_id):
				max_id = subj
			if (obj > max_id):
				max_id = obj
			cache_file.write(line)
			line_added += 1

print(f"Total {line_counter} lineas")
print(f"Total {line_added} lineas agregadas")
print(f"Max id: {max_id}")
