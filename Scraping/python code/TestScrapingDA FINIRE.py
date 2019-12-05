import bs4

# con questa riga posso chiamare la urllib.request.urlopen semplicemente chiamandola uReq
from urllib.request import urlopen as uReq
from bs4 import BeautifulSoup as soup

my_url = 'https://www.imdb.com/chart/moviemeter/?ref_=nv_mv_mpm'

# creo connessione alla pagina my_url 
uClient = uReq(my_url)
# assegno a una string l'html scaricato
page_html  = uClient.read()

uClient.close()

# da finire: segui https://youtu.be/XQgXKtPSzUI?t=579

