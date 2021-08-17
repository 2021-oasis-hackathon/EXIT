import where
import apppre
import app
import server
import schedule
import time
import webbrowser

schedule.every(30).minute.do(where)

while True:
    schedule.run_pending()
    time.sleep(1)