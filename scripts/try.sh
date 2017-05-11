#!/usr/bin/env sh
test -e ~/.coursier/coursier || ( \
  mkdir -p ~/.coursier && \
  curl -Lso ~/.coursier/coursier https://git.io/vgvpD && \
  chmod +x ~/.coursier/coursier \
)

~/.coursier/coursier launch -q -P \
  com.lihaoyi:ammonite_2.12.2:0.8.4 \
  is.cir:ciris-core_2.12:0.2.0 \
  is.cir:ciris-enumeratum_2.12:0.2.0 \
  is.cir:ciris-refined_2.12:0.2.0 \
  is.cir:ciris-squants_2.12:0.2.0 \
  -- --predef 'import ciris._,ciris.enumeratum._,ciris.refined._,ciris.squants._' < /dev/tty
