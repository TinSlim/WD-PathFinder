# WD-PathFinder (python)

In this package we prepare data for loading and further processing. We assume data from a Wikidata truthy dump. We first filter triples that are not used. We then build different types of caches, which are files that transform the graph into formats that are more efficient to load later into the path-finding algorithm. There are different types of cache, but the one that we will mainly use is the Adjacency Cache (which was shown to be the most efficient alternative in time and space for finding paths).

## Generating data
### Initial file

We expect as input a Wikidata truthy dump file in format `.nt.gz`, i.e., a GZipped N-Triples file containing lines like:

```
<https://wikidata.org/entity/Q9> <https://wikidata.org/porp/direct/P36> <https://wikidata.org/entity/Q3> .
<https://wikidata.org/entity/Q9> <https://wikidata.org/porp/direct/P36> <https://wikidata.org/entity/Q8> .
<https://wikidata.org/entity/Q3> <https://wikidata.org/porp/direct/P6> <https://wikidata.org/entity/Q9> .
```

WoolNet only works with edges between entities with Q ids. To eliminate edges that Woolnet does not use, run the script `make_subset.py`.

### Adjacency Cache

This cache can be obtained by running the script `make_pre_compressed.py`, in which you should replace the filename to point to the filtered Wikidata truthy dump produced previously. The cache indicates, for each node ID, the edges that connect it to other nodes.

Based on the previous example of N-triples, the adjacency cache would contain:

```
3 -36.9 6.9
8 -36.9 
9 36.3.8 -6.3
```

The first line indicates that node (Q)3 has an inward edge (-) from node (Q)9 via property (P)36 and an outward edge (+) to node (Q)6 via property (P)9.

### Triple Cache

This cache is obtained by running the script `make_pre_native.py`, in which you should replace the filename to point to the filtered Wikidata truthy dump produced prevoiusly. The cache indicates, for each node ID, the IDs of its incident edges.

Based on the previous example of N-triples, the triple cache would contain in a first table:

```
3 0 2
8 1
9 0 1 2
```

and in a second table:

```
9 36 3
9 36 8
3 6 9
```

where the first file indicates that node 3 participates in the 0th (first) and 2nd (third) triple per the order indicated in the second table.


## Random weighted sample of nodes for testing (randomTestAdjTime)

The script `mainRTAT.py` receives a file in the adjacency format, from which it generates a file with random nodes where the probability of selecting a node is proportional to the number of incident edges it has.

## Generating sets of nodes for testing (randomTestPathTime)

The script `mainRTPT.py` receives the random nodes from `mainRTAT.py` and generates random sets of nodes (of cardinality 2, 3, 4 and 5).


