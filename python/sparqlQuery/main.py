import mkwikidata

query_doble = """
SELECT *
WHERE {
  {
    { wd:Q180553 ?p ?z1 } UNION { ?z1 ?p wd:Q180553 }
    { ?z1 ?q wd:Q298 } UNION { wd:Q298 ?q ?z1 } 
    FILTER (?z1 != wd:Q180553 && ?z1 != wd:Q298)
  }
  UNION
  {
    { wd:Q180553 ?p wd:Q298 } UNION { wd:Q298 ?p wd:Q180553 }
  }
}
"""

query_triple = """
SELECT *
WHERE {
  {
    { wd:Q180553 ?p ?z1 } UNION { ?z1 ?p wd:Q180553 }
    { ?z1 ?q wd:Q298 } UNION { wd:Q298 ?q ?z1 }
    FILTER (?z1 != wd:Q180553 && ?z1 != wd:Q298)
  }
  UNION
  {
    { wd:Q17 ?p ?z1 } UNION { ?z1 ?p wd:Q17 }
    { ?z1 ?q wd:Q298 } UNION { wd:Q298 ?q ?z1 }
    FILTER (?z1 != wd:Q17 && ?z1 != wd:Q298)
  }
  UNION
  {
    { wd:Q17 ?p ?z1 } UNION { ?z1 ?p wd:Q17 }
    { ?z1 ?q wd:Q180553 } UNION { wd:Q180553 ?q ?z1 }
    FILTER (?z1 != wd:Q17 && ?z1 != wd:Q180553)
  } # TRIOS
  UNION
  {
    { wd:Q180553 ?p wd:Q298 } UNION { wd:Q298 ?p wd:Q180553 }
  }
  UNION
  {
    { wd:Q17 ?p wd:Q298 } UNION { wd:Q298 ?p wd:Q17 }
  }
  UNION
  {
    { wd:Q180553 ?p wd:Q17 } UNION { wd:Q17 ?p wd:Q180553 }
  }
}"""

def clear_val (x):
    ans = x['value']
    if ( (not "http://www.wikidata.org/prop/direct/" in ans) and (not "http://www.wikidata.org/entity/" in ans or "statement/" in ans) ):
        return None
    ans = ans.replace("http://www.wikidata.org/prop/direct/","")  # P
    ans = ans.replace("http://www.wikidata.org/entity/","")       # Q
    return ans

# Alexis:  Q180553
# Chile:   Q298
query_result = mkwikidata.run_query(query_doble, params={ })
#print(query_result['results'])
results = []
p_list = []
z1_list = []
q_list = []

for x in query_result['results']['bindings']:
    ans = []
    keys = x.keys()
    
    p  = clear_val( x['p'])  if 'p' in keys  else ans.append(None)
    z1 = clear_val( x['z1']) if 'z1' in keys else ans.append(None)
    q  = clear_val( x['q'])  if 'q' in keys  else ans.append(None)
    
    if p : p_list.append(p)
    if z1: z1_list.append(z1)
    if q : q_list.append(q)

    results.append([p,z1,q])

print( len(set(p_list)) + len(set(q_list)) )



