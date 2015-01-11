(defproject cah-clone "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :main cah-clone.core

  ; :aot :all

  ; :global-vars {*warn-on-reflection* true}

  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}}

  :source-paths ["src/clj" "src/cljs" "target/generated"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha" :exclusions [[org.clojure/clojure]]]

                 [org.clojure/core.match "0.3.0-alpha4"]
                 [com.taoensso/encore "1.19.1"]
                 [com.cemerick/piggieback "0.1.5-SNAPSHOT"]
                 [com.palletops/leaven "0.2.1"]
                 [bidi "1.14.0" :exclusions [org.clojure/clojure]]
                 [http-kit "2.1.18"]
                 [prismatic/schema "0.3.3"]
                 [liberator "0.12.2"]
                 [com.andrewmcveigh/cljs-time "0.2.4"]
                 [clj-time "0.9.0"]
                 [om "0.8.0-beta5"]
                 [sablono "0.2.22"]
                 [kioo "0.4.0"]
                 [optimus "0.15.1"]
                 [optimus-sass "0.0.3"]
                 [com.taoensso/timbre "3.3.1-1cd4b70"]
                 [com.palletops/log-config "0.1.4"]
                 [ring.middleware.logger "0.5.0"]
                 [ring "1.3.0"]
                 [ring/ring-headers "0.1.0"]
                 [ring/ring-anti-forgery "1.0.0"]
                 [ring/ring-devel "1.3.0"]
                 [ring/ring-core "1.3.0"]
                 [com.taoensso/sente "1.3.0-RC1"]
                 ; [com.taoensso/sente "1.2.0"]
                 [io.clojure/liberator-transit "0.3.0"]
                 [com.cognitect/transit-clj "0.8.259"]
                 [com.cognitect/transit-cljs "0.8.194"]
                 [org.clojure/tools.cli "0.3.1"]
                 [prismatic/om-tools "0.3.9"]
                 [com.palletops/bakery-httpkit "0.2.0"]
                 [com.palletops/bakery-weasel "0.2.0"]
                 [com.palletops/bakery-sente "0.2.0"]
                 [com.palletops/bakery-secretary "0.2.0"]
                 [com.palletops/bakery-local-storage-atom "0.2.0"]
                 [com.palletops/bakery-om-root "0.2.0"]
                 [environ "1.0.0"]
                 [org.clojure/tools.namespace "0.2.8"]
                 [com.stuartsierra/component "0.2.2"]
                 [com.taoensso/carmine "2.9.0"]
                 [com.datomic/datomic-pro "0.9.5078" :exclusions [joda-time]]
                 [org.clojure/tools.nrepl "0.2.5"]
                 [joplin.core "0.2.5"]]

  :plugins [[lein-typed "0.3.5"]
            [com.keminglabs/cljx "0.5.0"]
            [lein-pdo "0.1.1"]
            [lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.13"]
            [lein-bower "0.5.1"]
            [lein-environ "1.0.0"]
            [joplin.lein "0.2.5"]]
            ;[lein-git-deps "0.0.2-SNAPSHOT"]]

  ;:git-dependencies [["https://github.com/cemerick/piggieback.git"]]

  :bower {:directory "resources/public/lib"}

  :bower-dependencies [[foundation "5.4.7"]
                       [font-awesome "3.0.2"]]

  ; :hooks [cljx.hooks leiningen.cljsbuild]

  :prep-tasks [["cljx" "once"]]

  :jar-exclusions [#"\.cljx|\.swp|\.swo|\.DS_Store"]

  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "target/generated/src/clj"
                   :rules :clj}

                  {:source-paths ["src/cljx"]
                   :output-path "target/generated/src/cljs"
                   :rules :cljs}]}

                  ; {:source-paths ["test/cljx"]
                  ;  :output-path "target/generated/test/clj"
                  ;  :rules :clj}

                  ; {:source-paths ["test/cljx"]
                  ;  :output-path "target/generated/test/cljs"
                  ;  :rules :cljs}]}

  :cljsbuild {
    :builds [{ :id "dev"
               :source-paths ["src/cljs"] ; "src/generated/src/cljs"]
               :compiler {
                 :output-to "resources/public/js/cah-clone.js"
                 :output-dir "resources/public/js/out"
                 :optimizations :none
                 :pretty-print true
                 :source-map true}}]}
                 ;:externs ["react/externs/react.js"]}}}]}

              ;:test { :source-paths ["src/clj" "test/clj" "src/cljs" "test/cljs" "target/generated/src/clj" "target/generated/src/cljs" "target/generated/test/clj" "target/generated/test/cljs"]
              ;        :compiler {
              ;          :output-to "resources/public/js/scrabble.js"
              ;          :output-dir "resources/public/js/out"
              ;          :optimizations :none
              ;          :source-map true
              ;          :externs ["react/externs/react.js"]}}}}

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}
             :build {}}

  :aliases {"dev" ["pdo" "cljx" "cljsbuild" "auto" "dev,"]})
