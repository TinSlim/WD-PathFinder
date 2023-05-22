import requests

def get_images_from_wikidata(entity_id):
    # URL de la API de Wikidata
    url = "https://www.wikidata.org/w/api.php"

    # Propiedades que pueden contener imágenes
    image_properties = ["P18", "P41", "P94", "P154", "P158", "P242", "P291", "P2910"]
    # emoji: "P163"
    
    # Formatear las propiedades para la solicitud
    props = "|".join(image_properties)

    # Parámetros de la solicitud
    params = {
        "action": "wbgetentities",
        "ids": entity_id,
        "props": "claims",
        "format": "json"
    }

    # Realizar la solicitud a la API de Wikidata
    response = requests.get(url, params=params)
    data = response.json()

    # Obtener las imágenes del resultado
    images = []
    entity_data = data["entities"].get(entity_id)
    if entity_data and "claims" in entity_data:
        claims = entity_data["claims"]
        for prop in image_properties:
            if prop in claims:
                for claim in claims[prop]:
                    if "mainsnak" in claim and "datavalue" in claim["mainsnak"]:
                        image_url = claim["mainsnak"]["datavalue"]["value"]
                        images.append(image_url)

    return images

# ID de ejemplo (puedes reemplazarlo por el que desees)
entity_id = "Q298" # "Q42"

# Obtener las imágenes del ID especificado
images = get_images_from_wikidata(entity_id)

##
## https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Michael_Jackson_Dangerous_World_Tour_1993.jpg/300px-Sample.png
##
# Imprimir las URLs de las imágenes
for image in images:
    print(image)