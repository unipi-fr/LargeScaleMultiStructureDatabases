
import csv
import datetime
from random import randint
import string
import dateutil.parser



def remove_non_printable(s):
    return ''.join(c for c in s if c in string.printable)


def parse_date(text):
    
    try:
        return dateutil.parser.parse(text).year
    except ValueError:
        pass
    print(text)
    raise ValueError("Non Funziona nulla!!!!")



with open('Film.tsv', encoding="utf8") as tsvfile:
    reader = csv.reader(tsvfile, dialect='excel-tab')
    setTitoli = set()
    lista = []

    for row in reader:
        setTitoli.add(row[0])

    lista = list(setTitoli)

with open('FilmUnici.tsv', mode='w' , newline = '', encoding="utf8") as movies:
    writer = csv.writer(movies, dialect='excel-tab')

    for titolo in lista:
        writer.writerow([titolo])

