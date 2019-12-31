import csv


with open('movies_metadata.csv', encoding="utf8") as csvfile:
    reader = csv.reader(csvfile, delimiter=',')
    lista = []

    for row in reader:
        lista.append(row)

with open('movies_metadata.tsv', mode= 'w', newline= '', encoding="utf8") as tsv_file:
    TSVWriter = csv.writer(tsv_file, dialect='excel-tab')
    TSVWriter.writerows(lista)