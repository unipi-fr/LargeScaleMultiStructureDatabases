import csv

with open('movies_metadata.tsv', encoding='utf-8') as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab')
  
    lista = []

    for row in reader:
        lista.append(row)

with open('MetadatiFilm.csv', mode='w' , newline = '') as csv_file:
    CSVwriter = csv.writer(csv_file, delimiter=',')

    CSVwriter.writerows(lista)
    
