#!/usr/bin/env sh
test -e ~/.coursier/coursier || ( \
  mkdir -p ~/.coursier && \
  curl -Lso ~/.coursier/coursier https://git.io/vgvpD && \
  chmod +x ~/.coursier/coursier \
)

~/.coursier/coursier launch -q -P \
  com.lihaoyi:ammonite_2.12.4:1.0.3 \
  -E org.scala-lang:scala-library \
  -E org.scala-lang:scala-compiler \
  -E org.scala-lang:scala-reflect \
  org.typelevel:scala-compiler:2.12.4-bin-typelevel-4 \
  org.typelevel:scala-library:2.12.4-bin-typelevel-4 \
  org.typelevel:scala-reflect:2.12.4-bin-typelevel-4 \
  is.cir:ciris-cats_2.12:0.7.1 \
  is.cir:ciris-cats-effect_2.12:0.7.1 \
  is.cir:ciris-core_2.12:0.7.1 \
  is.cir:ciris-enumeratum_2.12:0.7.1 \
  is.cir:ciris-generic_2.12:0.7.1 \
  is.cir:ciris-refined_2.12:0.7.1 \
  is.cir:ciris-spire_2.12:0.7.1 \
  is.cir:ciris-squants_2.12:0.7.1 \
  -- --predef-code "\
        repl.compiler.settings.YliteralTypes.value = true;\
        repl.compiler.settings.YpartialUnification.value = true;\
        import ciris._,\
        ciris.syntax._,\
        ciris.enumeratum._,\
        ciris.generic._,\
        ciris.refined._,\
        ciris.refined.syntax._,\
        ciris.spire._,\
        ciris.squants._\
      " < /dev/tty
