#java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="penta" -DmaxEdgeSize="-1" testPathFindOptMem.jar > testPathFindResults/OptMemLimExc/lim-1/gC_penta_latest-truthy.csv && sh sendTg.sh
# Params:
#       -Dsubset        latest-truthy_small
#       -Dgraph         graphGt graphNative graphCompDense graphComp graphNativeFull graphNativeFullDense
#       -Dgroup         double triple cuadra penta
#       -Ddebug         "ok"
#       -DmaxEdgeSize   "10000"

java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="double" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gC_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="triple" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gC_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="cuadra" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gC_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphComp" -Dgroup="penta" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gC_penta_latest-truthy.csv && sh sendTg.sh &&

java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphCompDense" -Dgroup="double" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gCD_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphCompDense" -Dgroup="triple" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gCD_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphCompDense" -Dgroup="cuadra" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gCD_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphCompDense" -Dgroup="penta" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gCD_penta_latest-truthy.csv && sh sendTg.sh &&

java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphNativeFull" -Dgroup="double" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gNF_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphNativeFull" -Dgroup="triple" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gNF_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphNativeFull" -Dgroup="cuadra" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gNF_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphNativeFull" -Dgroup="penta" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gNF_penta_latest-truthy.csv && sh sendTg.sh &&

java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphNativeFullDense" -Dgroup="double" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gNFD_double_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphNativeFullDense" -Dgroup="triple" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gNFD_triple_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphNativeFullDense" -Dgroup="cuadra" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gNFD_cuadra_latest-truthy.csv && sh sendTg.sh &&
java -jar -Xmx59g -Dsubset="latest-truthy_small" -Dgraph="graphNativeFullDense" -Dgroup="penta" -DmaxEdgeSize="-1" testPathFindOptMem.jar > lastResults/structs/gNFD_penta_latest-truthy.csv && sh sendTg.sh &&

&& sh sendTg.sh && sh sendTg.sh && sh sendTg.sh && sh sendTg.sh 