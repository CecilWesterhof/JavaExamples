#!/usr/bin/env bash

set -o errexit
set -o nounset


mkdir -p src/main/java/annotations
mkdir -p src/test/java/annotations

ln -f NotEmpty.java     src/main/java/annotations/
ln -f NotEmptyTest.java src/test/java/annotations/

printf "Project installed\n"
printf "If you installed Maven you can run:\n"
printf "\tmvn compile\n\tmvn test\n"
