import os
import csv

csvfile = open("timeResults.csv","w",newline='')
writer =  csv.writer(csvfile,delimiter=';')

add_file = []
sizes_file = []


for filename in os.listdir("timeResults"):
    if "tiempos_" in filename:
        add_file.append("timeResults/" + filename)
    if "tiemposGrafo" in filename:
        sizes_file.append("timeResults/" + filename)

writer.writerow(['Estructura','Tiempos','ID','SetDatos'])

for filename in add_file:
    with open(filename) as csvreadfile:
        spamreader = csv.reader(csvreadfile, delimiter=';')
        row1 = next(spamreader)
        for row in spamreader:
            writer.writerow(row)

#------------
#------------

csvfile_2 = open("sizeResults.csv","w",newline='')
writer_2 =  csv.writer(csvfile_2,delimiter=';')

writer_2.writerow(["Estructura","SetDatos","TiempoCreacion","UsoMemoria"])

for filename in sizes_file:
    with open(filename) as csvreadfile:
        spamreader = csv.reader(csvreadfile, delimiter=';')
        row1 = next(spamreader)
        for row in spamreader:
            writer_2.writerow(row)