import requests
import csv
from bs4 import BeautifulSoup

def escape_sql(value):
    return value.replace("'", "''").strip()

def crawl_pokemondb(gen):
    url = f"https://pokemondb.net/ability?generation={gen}"
    r = requests.get(url)
    soup = BeautifulSoup(r.text, "html.parser")
    rows = soup.select("table.data-table tbody tr")
    results = []
    for tr in rows:
        name = escape_sql(tr.find("a").text)
        desc = escape_sql(tr.find_all("td")[2].text)
        results.append((name, desc, str(gen)))
    return results

def crawl_game8():
    url = "https://game8.co/games/Pokemon-Scarlet-Violet/archives/388972"
    r = requests.get(url)
    soup = BeautifulSoup(r.text, "html.parser")
    results = []
    for letter in soup.select("## List of Abilities"):
        # fallback: scan table rows
        pass
    # Simpler: lấy chung các tr trong mục chính
    for tr in soup.select("table tbody tr"):
        tds = tr.find_all("td")
        if len(tds) >= 2:
            name = tds[0].text.strip()
            desc = tds[1].text.strip()
            results.append((name, desc, "9"))
    return results

def main():
    data = []
    # Crawl Gen 3–8
    data += crawl_pokemondb(3)
    # Crawl Gen 9
#     data += crawl_game8()
    # Viết CSV
    with open("../sql/abilities.csv", "w", newline="", encoding="utf-8") as f:
        f.write("INSERT INTO ability (name, description, generation) VALUES\n")
        for i, (name, desc, gen) in enumerate(data):
            line = f"('{name}', '{desc}', {gen})"
            if i < len(data) - 1:
                line += ",\n"
            else:
                line += ";\n"
            f.write(line)

if __name__ == "__main__":
    main()
