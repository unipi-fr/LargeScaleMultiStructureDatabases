import csv


with open('../Data/wiki_movie_base.csv', encoding="utf-8") as csvfile:
    reader = csv.reader(csvfile, delimiter=',')
    lista = []

    for row in reader:
        lista.append(row)

with open('../Data/wiki_movie_base.tsv', mode= 'w', newline= '', encoding="utf8") as tsv_file:
    TSVWriter = csv.writer(tsv_file, dialect='excel-tab')
    TSVWriter.writerows(lista)