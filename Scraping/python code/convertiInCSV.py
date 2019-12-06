import csv

with open('DatiFilm.tsv') as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab')
  
    lista = []

    for row in reader:
        lista.append(row)

with open('DatiFilmOrganizzati.csv', mode='w' , newline = '') as csv_file:
    CSVwriter = csv.writer(csv_file, delimiter=',', quoting=csv.QUOTE_MINIMAL)

    CSVwriter.writerows(lista)
    
