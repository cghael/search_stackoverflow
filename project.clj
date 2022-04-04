(defproject stacksearch "0.1.0"
  :description "Test project for Rostelekom-Solar"
  :url ""
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[cheshire "5.10.2"]
                 [com.climate/claypoole "1.1.4"]
                 [compojure "1.6.2"]
                 [clj-http "3.12.3"]
                 [org.clojure/clojure "1.11.0"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [ring/ring-defaults "0.3.3"]]
  :main ^:skip-aot stacksearch.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
