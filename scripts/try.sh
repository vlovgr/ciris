#!/usr/bin/env sh
test -e ~/.coursier/coursier || ( \
  mkdir -p ~/.coursier && \
  curl -Lso ~/.coursier/coursier https://git.io/vgvpD && \
  chmod +x ~/.coursier/coursier \
)

~/.coursier/coursier launch -q -P \
  com.lihaoyi:ammonite_2.12.4:1.0.3 \
  is.cir:ciris-core_2.12:0.5.0 \
  is.cir:ciris-enumeratum_2.12:0.5.0 \
  is.cir:ciris-generic_2.12:0.5.0 \
  is.cir:ciris-refined_2.12:0.5.0 \
  is.cir:ciris-spire_2.12:0.5.0 \
  is.cir:ciris-squants_2.12:0.5.0 \
  -- --predef-code 'import ciris._,ciris.enumeratum._,ciris.generic._,ciris.refined._,ciris.spire._,ciris.squants._' < /dev/tty
