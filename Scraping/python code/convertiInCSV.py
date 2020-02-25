import csv

with open('FollowFilms.tsv', encoding='utf-8') as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab')
  
    lista = []

    for row in reader:
        lista.append(row)

with open('FollowFilms.csv', mode='w' , newline = '', encoding="utf-8") as csv_file:
    CSVwriter = csv.writer(csv_file, delimiter=',')

    CSVwriter.writerows(lista)
    
