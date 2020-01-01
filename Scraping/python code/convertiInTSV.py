import csv


with open('UsersUnpolished.csv', encoding="utf8") as csvfile:
    reader = csv.reader(csvfile, delimiter=',')
    lista = []

    for row in reader:
        lista.append(row)

with open('UsersUnpolished.tsv', mode= 'w', newline= '', encoding="utf8") as tsv_file:
    TSVWriter = csv.writer(tsv_file, dialect='excel-tab')
    TSVWriter.writerows(lista)