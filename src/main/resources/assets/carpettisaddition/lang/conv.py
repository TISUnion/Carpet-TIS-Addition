import json

from ruamel.yaml import YAML

lang = 'zh_tw'

org = json.load(open(lang + '.json', encoding='utf_8_sig'))
data = {}
for key, value in org.items():
    es = key.split('.')
    mp = data
    last_mp = {}
    last_key = None
    for i, e in enumerate(es):
        if i < len(es) - 1:
            if e not in mp:
                mp[e] = {}
            last_mp, last_key = mp, e
            mp = mp[e]
        else:
            if isinstance(mp, str):
                last_mp[last_key] = mp = {'.': mp}
            try:
                mp[e] = value
            except:
                print(key, value)
                print(mp, e)
                raise

YAML().dump(data, open(lang + '.yml', 'w', encoding='utf8'))
