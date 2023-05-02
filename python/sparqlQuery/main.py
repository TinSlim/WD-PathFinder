import mkwikidata
import time

def clear_val (x):
    ans = x['value']
    if ( (not "http://www.wikidata.org/prop/direct/" in ans) and (not "http://www.wikidata.org/entity/" in ans or "statement/" in ans) ):
        return None
    ans = ans.replace("http://www.wikidata.org/prop/direct/","")  # P
    ans = ans.replace("http://www.wikidata.org/entity/","")       # Q
    return ans

with open("subsets/randomGroups/"+"latest-truthy_small_random_double.csv","r") as f:

    for group in f.readline().split(";"):
        res = group.split(",")
        val1 = res[0]
        val2 = res[1]
        print(res)
        query_doble = """
SELECT *
WHERE {
  {
    { wd:Q"""+val1+""" ?p ?z1 } UNION { ?z1 ?p wd:Q"""+val1+""" }
    { ?z1 ?q wd:Q"""+val2+""" } UNION { wd:Q"""+val2+""" ?q ?z1 } 
    FILTER (?z1 != wd:Q"""+val1+""" && ?z1 != wd:Q"""+val2+""")
  }
  UNION
  {
    { wd:Q"""+val1+""" ?p wd:Q"""+val2+""" } UNION { wd:Q"""+val2+""" ?p wd:Q"""+val1+""" }
  }
}
"""
        query_result = mkwikidata.run_query(query_doble, params={ })

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
            
            if (p == None and z1 == None and q == None):
                continue
            if p : p_list.append(p)
            if z1: z1_list.append(z1)
            if q : q_list.append(q)

            results.append([p,z1,q])
        
        counter = 0
        z1_copy = []
        p_copy =  []
        q_copy =  []

        print(results)
        for i in range(len(results)):
            if z1_list[i]==None and q_list[i]==None:
                counter+=1
            elif z1_list[i] in z1_copy:
                index = z1_copy.index(z1_list[i])
                if p_list[i] not in p_copy[index]:
                    p_copy[index].append(p_list[i])
                    counter+=1
                if q_list[i] not in q_copy[index]:
                    q_copy[index].append(q_list[i])
                    counter+=1
            else:
                z1_copy.append(z1_list[i])
                p_copy.append([p_list[i]])
                q_copy.append([q_list[i]])
                counter+=2
        print("===")
        print(counter)
        print( len(set(p_list)) + len(set(q_list)) )
        print("===")
        
        time.sleep(2)


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



# Alexis:  Q180553
# Chile:   Q298

#print(query_result['results'])




big_query = """
SELECT *
WHERE {
  {
    { wd:Q64593981 ?p ?z1 } UNION { ?z1 ?p wd:Q64593981 }
    { ?z1 ?q ?z2 } UNION { ?z2 ?q ?z1 }
    { ?z2 ?r wd:Q315658 } UNION { wd:Q315658 ?r ?z2 }
    FILTER (?z1 != wd:Q64593981 && ?z1 != wd:Q315658 &&
            ?z2 != wd:Q64593981 && ?z2 != wd:Q315658 &&
            ?z1 != ?z2)
  }
  UNION
  {
    { wd:Q64593981 ?p ?z1 } UNION { ?z1 ?p wd:Q64593981 }
    { ?z1 ?q wd:Q315658 } UNION { wd:Q315658 ?q ?z1 }
    FILTER (?z1 != wd:Q64593981 && ?z1 != wd:Q315658)
  }
  UNION
  {
    { wd:Q64593981 ?p wd:Q315658 } UNION { wd:Q315658 ?p wd:Q64593981 }
  }
}"""