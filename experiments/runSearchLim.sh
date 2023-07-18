# java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="penta" -DmaxEdgeSize="-1" testPathFindOptMem.jar > testPathFindResults/OptMemLimExc/lim-1/gC_penta_latest-truthy.csv && sh sendTg.sh
# Params:
#       -Dsubset        latest-truthy_small
#       -Dgraph         graphGt graphNative graphCompDense graphComp graphNativeFull graphNativeFullDense
#       -Dgroup         double triple cuadra penta
#       -Ddebug         "ok"
#       -DmaxEdgeSize   "10000"

# lim-1
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="double" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/lim/l1_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="triple" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/lim/l1_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="cuadra" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/lim/l1_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="penta" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/lim/l1_penta_latest-truthy.csv && sh sendTg.sh &&

# lim1000
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="double" -DmaxEdgeSize="1000" testPathFindOptMem.jar > lastResults/lim/l1000_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="triple" -DmaxEdgeSize="1000" testPathFindOptMem.jar > lastResults/lim/l1000_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="cuadra" -DmaxEdgeSize="1000" testPathFindOptMem.jar > lastResults/lim/l1000_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="penta" -DmaxEdgeSize="1000" testPathFindOptMem.jar > lastResults/lim/l1000_penta_latest-truthy.csv && sh sendTg.sh &&

# lim10000
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="double" -DmaxEdgeSize="10000" testPathFindOptMem.jar > lastResults/lim/l10000_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="triple" -DmaxEdgeSize="10000" testPathFindOptMem.jar > lastResults/lim/l10000_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="cuadra" -DmaxEdgeSize="10000" testPathFindOptMem.jar > lastResults/lim/l10000_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="penta" -DmaxEdgeSize="10000" testPathFindOptMem.jar > lastResults/lim/l10000_penta_latest-truthy.csv && sh sendTg.sh &&

# lim100000
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="double" -DmaxEdgeSize="100000" testPathFindOptMem.jar > lastResults/lim/l100000_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="triple" -DmaxEdgeSize="100000" testPathFindOptMem.jar > lastResults/lim/l100000_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="cuadra" -DmaxEdgeSize="100000" testPathFindOptMem.jar > lastResults/lim/l100000_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="penta" -DmaxEdgeSize="100000" testPathFindOptMem.jar > lastResults/lim/l100000_penta_latest-truthy.csv && sh sendTg.sh &&

# lim1000000
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="double" -DmaxEdgeSize="1000000" testPathFindOptMem.jar > lastResults/lim/l1000000_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="triple" -DmaxEdgeSize="1000000" testPathFindOptMem.jar > lastResults/lim/l1000000_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="cuadra" -DmaxEdgeSize="1000000" testPathFindOptMem.jar > lastResults/lim/l1000000_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="penta" -DmaxEdgeSize="1000000" testPathFindOptMem.jar > lastResults/lim/l1000000_penta_latest-truthy.csv && sh sendTg.sh &&

sh sendTg.sh && sh sendTg.sh && sh sendTg.sh && sh sendTg.sh
