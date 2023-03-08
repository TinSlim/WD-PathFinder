#

Caminos = 4


-> -> 55

SELECT *
WHERE {
   wd:Q298 ?r1 ?y1 .
   ?y1 ?r2 wd:Q414 .
}


-> <- 133

SELECT *
WHERE {
   wd:Q298 ?r1 ?y1 .
   wd:Q414 ?r2 ?y1 .
}

<- -> 1652

SELECT *
WHERE {
   ?y1 ?r1 wd:Q298 .
   ?y1 ?r2 wd:Q414 .
}

<- <- 57

SELECT *
WHERE {
   ?y1 ?r1 wd:Q298 .
   wd:Q414 ?r2 ?y1 .
}

TOTAL = 1.897 FILAS

### FORMA 1 / HECHO



#### EJEMPLO

SELECT *
WHERE {
{ ?x :father ?y . ?y :father ?z }
UNION { ?x :father ?y . ?y :mother ?z }
UNION { ?x :mother ?y . ?y :father ?z }
UNION { ?x :mother ?y . ?y :mother ?z }
}

#### uso

SELECT *
WHERE {
  { wd:Q298 ?r1 ?y1 . ?y1 ?r2 wd:Q414 } UNION # -> ->
  { wd:Q298 ?r1 ?y1 . wd:Q414 ?r2 ?y1 } UNION # -> <-
  { ?y1 ?r1 wd:Q298 . ?y1 ?r2 wd:Q414 } UNION # <- ->
  { ?y1 ?r1 wd:Q298 . wd:Q414 ?r2 ?y1 } # <- <-
}






TOTAL = 1897 


### FORMA 2 NO SIRVE

#### EJEMPLO

SELECT *
WHERE {
{ ?x :father ?y } UNION { ?x :mother ?y }
   # JOIN
{ ?y :father ?z } UNION { ?y :mother ?z }
   # JOIN
{ ?z :father ?w } UNION { ?z :mother ?w }
}

#### USO

SELECT *
WHERE {
{ wd:Q298 ?r1 ?y1 } UNION { ?y1 ?r2 wd:Q414 }
   # JOIN
{ ?y1 ?r1 wd:Q298 } UNION { ?y1 ?r2 wd:Q414}
   # JOIN
}

TOTAL= 272688




### FORMA 3

#### EJEMPLO

SELECT *
WHERE {
{ ?x ?p ?z1 } UNION { ?z1 ?p ?x }
{ ?z1 ?q ?z2 } UNION { ?z2 ?q ?z1 }
...

}

#### USO

TOTAL = 1897

SELECT *
WHERE {
	{ wd:Q298 ?p ?z1 } UNION { ?z1 ?p wd:Q298 }
	{ ?z1 ?q wd:Q414 } UNION { wd:Q414 ?q ?z1 }
}