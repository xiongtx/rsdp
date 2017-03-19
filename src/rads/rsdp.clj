(ns rads.rsdp
  (:require
    [clojure.core.async :as async :refer [go >!]]
    [clojure.core.match :refer [match]]
    [rads.rsdp.util :as util]
    [com.stuartsierra.component :as component]))

(defn- fair-loss-link-handler []
  (fn [{fll :pid} event trigger]
    (go
      (match [event]
        [[fll :send p m]] (when (= 1 (rand-int 2))
                            (>! trigger [fll :deliver p m]))
        :else nil))))

(defn new-fair-loss-link [pid events trigger]
  (let [handle (fair-loss-link-handler)]
    (util/new-async-process handle events trigger)))

(defn- stubborn-link-handler [sent delta]
  (fn [{sl :pid {fll :pid} :fll} event trigger]
    (go
      (match [event]
        [[sl :init]] (do
                       (reset! sent #{})
                       (util/start-timer delta))
        [[:timeout]] (do
                       (doseq [[q m] @sent]
                         (>! trigger [fll :send q m]))
                       (util/start-timer delta))
        [[sl :send q m]] (do
                           (>! trigger [fll :send q m])
                           (swap! sent conj [q m]))
        [[fll :deliver p m]] (>! trigger [sl :deliver p m])
        :else nil))))

(defn new-stubborn-link [pid events trigger]
  (let [handle (stubborn-link-handler (atom nil) 1000)]
    (util/new-async-process handle events trigger)))

(defn new-channels []
  (let [trigger-chan (async/chan 100)
        events-chan (async/chan 100)
        events-mult (async/mult events-chan)
        fll-events (async/chan 100)
        sl-events (async/chan 100)]
    (async/pipe trigger-chan events-chan)
    (async/tap util/timeouts-mult events-chan)
    (async/tap events-mult fll-events)
    (async/tap events-mult sl-events)
    {:trigger-chan trigger-chan 
     :events-chan events-chan
     :events-mult events-mult
     :fll-events fll-events
     :sl-events sl-events}))

(defn new-system [{:keys [trigger-chan fll-events sl-events] :as channels}]
  (-> (component/system-map
        :fll (new-fair-loss-link "fll" fll-events trigger-chan)
        :sl (new-stubborn-link "sl" sl-events trigger-chan))
      (component/system-using
        {:sl [:fll]})))

(comment
  (require '[rads.rsdp.util :as util] :reload)
  (require '[rads.rsdp :as rsdp] :reload)
  (require '[clojure.core.async :as async])
  (require '[com.stuartsierra.component :as component])
  (def channels (rsdp/new-channels))
  (def s (async/chan (async/dropping-buffer 100)))
  (async/tap (:events-mult channels) s)
  (async/go-loop [] (when-let [m (async/<! s)] (println m) (recur)))
  (def sys (component/start (rsdp/new-system channels))))
