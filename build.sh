#!/usr/bin/env bash

set -exo errexit
dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd ${dir}
sbt fullOptJS

rm -Rf ${dir}/dist && mkdir ${dir}/dist
cp ${dir}/target/scala-2.12/consensus-opt.js ${dir}/dist/consensus.js
sed 's/.\/target\/scala-2.12\/consensus-opt/consensus/' <${dir}/index.html >${dir}/dist/index.html

