import csv
import datetime

lista = []
listaDaScrivere = []

with open('Film.csv', encoding="utf-8") as csvfile:
    reader = csv.reader(csvfile, delimiter=',')

    for row in reader:
        lista.append(row)

for riga in lista:
    data = datetime.datetime.strptime(riga[0], "%Y-%m-%dT%H:%M:%S")
    listaDaScrivere.append([data.year, riga[1], riga[2]])


with open('FilmCorretti.csv', mode='w' , newline = '', encoding="utf-8") as csv_file:
    CSVwriter = csv.writer(csv_file, delimiter=',')

    CSVwriter.writerows(listaDaScrivere)
