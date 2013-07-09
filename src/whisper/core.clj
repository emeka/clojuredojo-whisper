(ns whisper.core
  (:require [clojure.string :as st]))

(defn get-stopwords []
  (set
  (clojure.string/split  (slurp "resources/stopwords.txt") #"," )))

(def stop-words
  (get-stopwords))

(defn hyph [word]
  (st/replace word " " "-"))

(defn get-fish []
  (remove empty? (map hyph (clojure.string/split-lines (slurp "resources/fish.txt")  ) )))


(def words (atom (set (get-fish))))

(defn add-word [word]
  (swap! words #(conj % word)))

(defn replaceword [word]
  (if (not (stop-words word))
    (do
      (add-word word)
      (rand-nth (vec @words)))
    word))

(defn split-sentence [sentence]
  (st/split sentence #" " ))

(defn alter-sentence [sentence]
  (let [sent (vec (split-sentence sentence))
         idx (rand-int (count sent))]
    (st/join " " (assoc sent idx (replaceword (sent idx))))))

(defn whisper [count sentence]
  (take count
  (iterate alter-sentence sentence)))

(def clojureblurb "Clojure is a dynamic programming language that targets the Java Virtual Machine (and the CLR, and JavaScript). It is designed to be a general-purpose language, combining the approachability and interactive development of a scripting language with an efficient and robust infrastructure for multithreaded programming. Clojure is a compiled language - it compiles directly to JVM bytecode, yet remains completely dynamic. Every feature supported by Clojure is supported at runtime. Clojure provides easy access to the Java frameworks, with optional type hints and type inference, to ensure that calls to Java can avoid reflection.")

(defn whisper-lots [n sentence]
  (last (whisper n sentence))
  )